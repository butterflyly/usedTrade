window.onload = async function () {
    console.log("JS 실행됨");

    const container = document.getElementById("auth-buttons");
    const notificationBox = document.getElementById("nav-notification");

    if (!container) return;

    function showLoginButtons() {
        container.innerHTML = `
            <a href="/users/login">로그인</a>
            <a href="/oauth2/authorization/google">구글 로그인</a>
            <a href="/oauth2/authorization/naver">네이버 로그인</a>
            <a href="/users/register" style="margin-left:10px;">회원가입</a>
        `;

       if (notificationBox) {
            notificationBox.classList.add("hidden");
        }
    }

    try {
        const res = await fetch("/api/users/me", {
            method: "GET",
            credentials: "include"
        });

        // 🔴 비로그인
        if (!res.ok) {
            showLoginButtons();
            return;
        }

        const data = await res.json();
        const user = data.user;

        if (!user) {
            showLoginButtons();
            return;
        }

        // 로그인 UI
        container.innerHTML = "";

        // 🔔 알림 보이기
        if (notificationBox) {
            notificationBox.classList.remove("hidden");
        }

        const nickSpan = document.createElement("span");

        nickSpan.textContent = `${user.nickname} 님 환영합니다!`;
        nickSpan.style.marginRight = "15px";
        container.appendChild(nickSpan);

        // 관리자일 경우 관리자 페이지 링크 추가
        if (user.userRole === "ADMIN") {
            const adminLink = document.createElement("a");
            adminLink.href = "/admin/report";
            adminLink.textContent = "관리자페이지";
            adminLink.style.marginRight = "15px";
            container.appendChild(adminLink);
        }

        const myPageLink = document.createElement("a");
        myPageLink.href = "/users/mypage";
        myPageLink.textContent = "마이페이지";
        myPageLink.style.marginRight = "15px";
        container.appendChild(myPageLink);

        const logoutBtn = document.createElement("button");
        logoutBtn.textContent = "로그아웃";
        logoutBtn.addEventListener("click", async () => {
            await fetch("/api/users/logout", {
                method: "POST",
                credentials: "include"
            });
            location.reload();
        });

        container.appendChild(logoutBtn);

    } catch (err) {
        console.error("auth check error:", err);
        showLoginButtons();
    }
};
