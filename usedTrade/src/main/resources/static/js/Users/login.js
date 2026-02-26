function login(event) {
    event.preventDefault();

    const username = document.querySelector("#username").value;
    const password = document.querySelector("#password").value;
    const rememberMe =
        document.querySelector("input[name='rememberMe']").checked;

    fetch("/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username, password, rememberMe })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        location.href = "/item-posts";
    })
    .catch(() => alert("로그인 실패"));
}


async function fetchWithAuth(url, options = {}) {
    let response = await fetch(url, {
        ...options,
        credentials: "include"
    });

    // AccessToken 만료
    if (response.status === 401) {
        // 재발급 시도
        const reissue = await fetch("/api/users/auth/reissue", {
            method: "POST",
            credentials: "include"
        });

        if (reissue.ok) {
            // 재시도
            response = await fetch(url, {
                ...options,
                credentials: "include"
            });
        } else {
            // 재발급 실패 → 로그인 페이지
            window.location.href = "/users/login";
            throw new Error("Login expired");
        }
    }

    return response;
}