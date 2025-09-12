const passwordButton = document.querySelector(".form__password-button");
const passwordInput = document.querySelector("[name=password]");
const loginForm = document.getElementById("login-form");
const loginButton = document.getElementById("login-button");
const registerButton = document.getElementById("register-button");
const loginButtonText = loginButton.querySelector(".button-text");
const loginLoader = loginButton.querySelector(".loader");
const registerLoader = registerButton.querySelector(".loader");

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

passwordButton.addEventListener("click", handlePasswordButtonClick);

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