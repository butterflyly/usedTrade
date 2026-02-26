// ==============================
// 검증 상태 관리
// ==============================
const validationStatus = {
    username: false,
    nickname: false,
    email: false,
    passwordFormat: false,
    passwordMatch: false
};

// ==============================
// DOM 헬퍼
// ==============================
const qs = (selector) => document.querySelector(selector);

// ==============================
// 아이디 중복 확인
// ==============================
async function checkDuplicateUsername() {
    const username = qs("#username").value.trim();

    if (!username) {
        alert("아이디를 입력해주세요.");
        return;
    }

    const idRegex = /^[a-zA-Z0-9]{5,15}$/;
    if (!idRegex.test(username)) {
        alert("아이디는 영문/숫자 5~15자리로 입력해주세요.");
        return;
    }

    try {
        const res = await fetch("/api/users/create/check-duplicate-id", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ username })
        });

        if (!res.ok) throw new Error("서버 오류");

        const isAvailable = await res.json();

        if (isAvailable && confirm("사용 가능한 아이디입니다. 사용하시겠습니까?")) {
            qs("#username").readOnly = true;
            qs("#checkUsernameBtn").style.display = "none";
            qs("#changeUsernameBtn").style.display = "inline-block";
            validationStatus.username = true;
        } else if (!isAvailable) {
            alert("이미 사용 중인 아이디입니다.");
        }
    } catch (err) {
        console.error(err);
        alert("아이디 중복 확인 중 오류 발생");
    }
}

// ==============================
// 닉네임 중복 확인
// ==============================
async function checkDuplicateNickname() {
    const nickname = qs("#nickname").value.trim();

    if (!nickname) {
        alert("닉네임을 입력해주세요.");
        return;
    }

    const regex = /^[a-zA-Z0-9가-힣]{2,10}$/;
    if (!regex.test(nickname)) {
        alert("닉네임은 2~10자리로 입력해주세요.");
        return;
    }

    try {
        const res = await fetch("/api/users/create/check-duplicate-nickname", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ nickname })
        });

        if (!res.ok) throw new Error("서버 오류");

        const isAvailable = await res.json();

        if (isAvailable && confirm("사용 가능한 닉네임입니다. 사용하시겠습니까?")) {
            qs("#nickname").readOnly = true;
            qs("#checkNicknameBtn").style.display = "none";
            qs("#changeNicknameBtn").style.display = "inline-block";
            validationStatus.nickname = true;
        } else if (!isAvailable) {
            alert("이미 사용 중인 닉네임입니다.");
        }
    } catch (err) {
        console.error(err);
        alert("닉네임 중복 확인 중 오류 발생");
    }
}

// ==============================
// 이메일 중복 확인
// ==============================
async function checkDuplicateEmail() {
    const email = qs("#email").value.trim();
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    if (!regex.test(email)) {
        alert("이메일 형식을 확인해주세요.");
        return;
    }

    try {
        const res = await fetch("/api/users/create/check-duplicate-email", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ email })
        });

        if (!res.ok) throw new Error("서버 오류");

        const isAvailable = await res.json();

        if (isAvailable && confirm("사용 가능한 이메일입니다. 사용하시겠습니까?")) {
            qs("#email").readOnly = true;
            qs("#checkEmailBtn").style.display = "none";
            qs("#changeEmailBtn").style.display = "inline-block";
            validationStatus.email = true;
        } else if (!isAvailable) {
            alert("이미 사용 중인 이메일입니다.");
        }
    } catch (err) {
        console.error(err);
        alert("이메일 중복 확인 중 오류 발생");
    }
}

// ==============================
// 비밀번호 검증
// ==============================
function validatePassword() {
    const pw = qs("#password").value;
    const pw2 = qs("#password2").value;

    const pwError = qs("#passwordError");
    const matchError = qs("#passwordMatchError");

    const regex = /^(?=.*[a-zA-Z])(?=.*\d).{12,}$/;

    // 초기화
    pwError.style.display = "none";
    matchError.style.display = "none";

     // 1️⃣ 비밀번호 조건 체크
    if (pw && !regex.test(pw)) {
        pwError.textContent =
            "비밀번호는 영문 + 숫자를 포함해 최소 12자 이상이어야 합니다.";
        pwError.style.display = "block";
        validationStatus.passwordFormat = false;
    } else {
        validationStatus.passwordFormat = pw.length > 0;
    }

    // 2️⃣ 비밀번호 일치 체크
    if (pw2 && pw !== pw2) {
        matchError.textContent = "비밀번호가 일치하지 않습니다.";
        matchError.style.display = "block";
        validationStatus.passwordMatch = false;
    } else {
        validationStatus.passwordMatch = pw && pw === pw2;
    }

}

// ==============================
// 변경 버튼
// ==============================
function resetField(field, checkBtn, changeBtn, key) {
    qs(field).readOnly = false;
    qs(checkBtn).style.display = "inline-block";
    qs(changeBtn).style.display = "none";
    validationStatus[key] = false;
}

// ==============================
// 폼 제출
// ==============================
document.addEventListener("DOMContentLoaded", () => {

    qs("#password").addEventListener("input", validatePassword);
    qs("#password2").addEventListener("input", validatePassword);

    qs("#registerForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        if (!Object.values(validationStatus).every(Boolean)) {
            alert("모든 검증을 완료해주세요.");
            return;
        }

        const formData = new FormData(e.target);

        try {
            const res = await fetch("/api/users/register", {
                method: "POST",
                body: formData
            });

            if (res.ok) {
                alert("회원가입 완료");
                location.href = "/item-posts";
            } else {
                const msg = await res.text();
                qs("#error-box").style.display = "block";
                qs("#error-box").innerHTML = msg;
            }
        } catch (err) {
            console.error(err);
            alert("서버 오류 발생");
        }
    });
});
