function validateAndLogin() {
    let valid = true;

    const username = document.getElementById('usernameInput').value.trim();
    const password = document.getElementById('passwordInput').value;

    // 1. 아이디 유효성 검사
    // 조건 : 4~20자 영문/숫자만 허용
    // 정규식 : /^[a-zA-Z0-9]{4,20}$/
    // 실패 시 : showError('usernameInput', 'usernameMsg', '오류 메시지')
    // 성공 시 : clearError('usernameInput')
    const usernameRegex = /^[a-zA-Z0-9]{4,20}$/;

    if (!usernameRegex.test(username)) {
        showError (
            'usernameInput', 'usernameMsg', '아이디는 4~20자의 영문/숫자만 가능합니다.'
        );
        valid = false;
    }
    else {
        clearError('usernameInput', 'usernameMsg');
    }
    // 2. 패스워드 유효성 검사
    // 조건 : 8자 이상, 영문 + 숫자 + 특수문자(!@#$%^&*) 포함
    // 정규식 : /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/
    // 실패 시 : showError('passwordInput', 'passwordMsg', '오류 메시지')
    // 성공 시 : clearError('passwordInput')
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

    if (!passwordRegex.test(password)) {
        showError(
            'passwordInput', 'passwordMsg', '비밀번호는 8자 이상 영문+숫자+특수문자를 포함해야 합니다.'
        );
        valid = false;
    }
    else {
        clearError('passwordInput', 'passwordMsg');
    }
    // 3. 두 항목 모두 통과 시 로그인 진행
    if (valid) {
        submitLogin();
    }
}
async function submitLogin() {
    const password = document.getElementById('passwordInput').value;
    const hashed = await hashPassword(password);

    document.getElementById('password').value = hashed;
    document.getElementById('loginForm').submit();
}
function clearError(inputId, msgId) {
    document.getElementById(inputId).classList.remove('is-invalid');
    document.getElementById(msgId).textContent = '';
}