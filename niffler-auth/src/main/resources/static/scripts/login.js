const passwordButton = document.querySelector(".form__password-button");
const passwordInput = document.querySelector("[name=password]");
const loginForm = document.getElementById("login-form");
const loginButton = document.getElementById("login-button");
const registerButton = document.getElementById("register-button");
const loginButtonText = loginButton.querySelector(".button-text");
const loginLoader = loginButton.querySelector(".loader");
const registerLoader = registerButton.querySelector(".loader");
const loginWithPasskeyButton = document.getElementById("login-with-passkey-button");

function getCsrf() {
    const m = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return m ? decodeURIComponent(m[1]) : null;
}

function renderError(message) {
    document.querySelectorAll('.js-error').forEach(el => el.remove());

    const div = document.createElement('div');
    div.className = 'form__error-container js-error';
    div.setAttribute('role', 'alert');
    div.setAttribute('aria-live', 'assertive');

    const p = document.createElement('p');
    p.className = 'form__error';
    p.textContent = message;

    div.appendChild(p);
    const headerEl = loginForm.querySelector('.header');
    if (headerEl && headerEl.parentNode === loginForm) {
        headerEl.insertAdjacentElement('afterend', div);
    } else {
        loginForm.insertAdjacentElement('afterbegin', div);
    }
}

async function readErrorMessage(res, fallback = 'Request failed') {
    let msg = `${fallback}: ${res.status} ${res.statusText}`;
    const ct = res.headers.get('content-type') || '';
    try {
        if (ct.includes('application/json')) {
            const data = await res.json();
            msg = data?.message || data?.error || msg;
        } else {
            const txt = await res.text();
            if (txt) msg = txt.slice(0, 500);
        }
    } catch (_) { /* silence */ }
    return msg;
}

async function postJson(path, body) {
    const url = path.startsWith('http') ? path : (AUTH_BASE + path);
    const csrf = getCsrf();
    return fetch(url, {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            ...(csrf ? { 'X-XSRF-TOKEN': csrf } : {})
        },
        body: body ? JSON.stringify(body) : null
    });
}

const togglePasswordInputType = (inputType) => {
    return inputType === "password" ? "text" : "password";
}

const togglePasswordButtonClass = () => {
    passwordButton.classList.toggle("form__password-button_active");
}

const handlePasswordButtonClick = () => {
    passwordInput.setAttribute("type", togglePasswordInputType(passwordInput.getAttribute("type")));
    togglePasswordButtonClass();
}

const handleLoginWithPasskeyButtonClick = async (e) => {
    e.preventDefault();
    try {
        const optRes = await postJson('/webauthn/authenticate/options');
        if (!optRes.ok) throw new Error('Failed to get WebAuthn options');
        const requestOptionsJSON = await optRes.json();
        const publicKey = PublicKeyCredential.parseRequestOptionsFromJSON(requestOptionsJSON);
        const cred = await navigator.credentials.get({ publicKey });
        if (!cred) throw new Error('No credential assertion returned');
        const assertionJSON = cred.toJSON();
        const authRes = await postJson('/login/webauthn', assertionJSON);
        if (authRes.ok) {
            window.location.replace(FRONT_BASE);
        } else {
            const m = await readErrorMessage(authRes, 'Passkey request failed');
            renderError(m);
        }
    } catch (e) {
        renderError(e.message || String(e));
    }
}

passwordButton.addEventListener("click", handlePasswordButtonClick);
loginWithPasskeyButton.addEventListener("click", handleLoginWithPasskeyButtonClick);

document.addEventListener("DOMContentLoaded", function() {
    loginForm.addEventListener("submit", function() {
        loginButton.classList.add("disabled");
        loginButtonText.textContent = "Logging in...";
        loginLoader.classList.remove("hidden");
    });

    registerButton.addEventListener("click", function() {
        registerButton.classList.add("disabled");
        registerLoader.classList.remove("hidden");
    });
});

window.addEventListener("pageshow", (e) => {
    loginButtonText.textContent = "Log in";
    registerButton.classList.remove("disabled");
    loginButton.classList.remove("disabled");
    registerLoader.classList.add("hidden");
    loginLoader.classList.add("hidden");
});