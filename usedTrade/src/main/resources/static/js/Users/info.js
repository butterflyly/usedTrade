    document.getElementById("updateBtn").addEventListener("click", function () {

    const form = document.querySelector("form");
    const formData = new FormData(form);

    fetch("/api/users/info", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => Promise.reject(err));
        }
        return response;
    })
    .then(() => {
        alert("회원 정보가 수정되었습니다.");
        location.href = "/users/mypage";
    })
    .catch(errors => {
        const errorBox = document.getElementById("errorBox");
        errorBox.innerHTML = "";

        if (Array.isArray(errors)) {
            errors.forEach(msg => {
                const div = document.createElement("div");
                div.innerText = msg;
                errorBox.appendChild(div);
            });
        } else {
            errorBox.innerText = "처리 중 오류가 발생했습니다.";
        }
    });
});
