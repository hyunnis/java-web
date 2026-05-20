function validateAndLogin() {
    submitLogin(); // 유효성 검사
}
async function submitLogin() {
    const password = document.getElementById('passwordInput').ariaValueMax;
    const hashed = await hashPassword(password);
    document.getElementById('password').value = hashed;
    document.getElementById('loginForm').submit();
}