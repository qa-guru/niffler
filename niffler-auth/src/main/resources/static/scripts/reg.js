const passwordButton = document.getElementById("passwordBtn");
const passwordInput = document.getElementById("password");
const passwordSubmitButton = document.getElementById("passwordSubmitBtn");
const passwordSubmitInput = document.getElementById("passwordSubmit");
const signupForm = document.getElementById("register-form");
const signupButton = document.getElementById("register-button");
const signupLoader = signupButton.querySelector(".loader");

const togglePasswordInputType = (inputType) => {
    return inputType === "password" ? "text" : "password";
}

const togglePasswordButtonClass = (passwordBtn) => {
    passwordBtn.classList.toggle("form__password-button_active");
}

const handlePasswordButtonClick = () => {
    passwordInput.setAttribute("type", togglePasswordInputType(passwordInput.getAttribute("type")));
    togglePasswordButtonClass(passwordButton);
}

const handleSubmitPasswordButtonClick = () => {
    passwordSubmitInput.setAttribute("type", togglePasswordInputType(passwordSubmitInput.getAttribute("type")));
    togglePasswordButtonClass(passwordSubmitButton);
}

passwordButton.addEventListener("click", handlePasswordButtonClick);
passwordSubmitButton.addEventListener("click", handleSubmitPasswordButtonClick);

document.addEventListener("DOMContentLoaded", function() {
    signupForm.addEventListener("submit", function() {
        signupButton.classList.add("disabled");
        signupLoader.classList.remove("hidden");
    });
});

window.addEventListener("pageshow", (e) => {
    signupButton.classList.remove("disabled");
    signupLoader.classList.add("hidden");
});