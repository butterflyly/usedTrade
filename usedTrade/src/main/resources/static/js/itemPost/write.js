  document.getElementById("postForm").addEventListener("submit", async e => {
    e.preventDefault();

    const form = e.target;
    const errorBox = document.getElementById("error-box");
    errorBox.style.display = "none";
    errorBox.innerHTML = "";

    // DTO 구성
    const dto = {
        title: form.title.value,
        price: form.price.value,
        content: form.content.value,
        categoryId: form.categoryId.value
    };

    const formData = new FormData();

    formData.append("title", form.title.value);
    formData.append("price", form.price.value);
    formData.append("content", form.content.value);
    formData.append("categoryId", form.categoryId.value);

    // 파일
    for (let file of form.files.files) {
        formData.append("files", file);
    }

    const res = await fetch("/api/item-posts", {
        method: "POST",
        body: formData,
        credentials: "include"
    });

    if (!res.ok) {
        let message = "알 수 없는 오류가 발생했습니다.";

        const contentType = res.headers.get("content-type");

        if (contentType && contentType.includes("application/json")) {
            const errors = await res.json();
            message = Array.isArray(errors)
                ? errors.join("<br>")
                : errors;
        } else {
            // 서버에서 HTML / text 응답이 온 경우
            message = await res.text();
        }

        errorBox.style.display = "block";
        errorBox.innerHTML = message;
        return;
    }

    alert("등록 완료!");
    location.href = "/item-posts";
});

const MAX_IMAGES = 5;
const fileInput = document.getElementById("files");
const preview = document.getElementById("preview");

fileInput.addEventListener("change", function () {
    preview.innerHTML = "";

    if (this.files.length > MAX_IMAGES) {
        alert(`이미지는 최대 ${MAX_IMAGES}장까지 업로드할 수 있습니다.`);
        this.value = "";
        return;
    }

    [...this.files].forEach(file => {
        const reader = new FileReader();

        reader.onload = e => {
            const div = document.createElement("div");
            div.className = "image-item";

            div.innerHTML = `
                <img src="${e.target.result}" style="width:120px; height:120px; object-fit:cover;">
            `;

            preview.appendChild(div);
        };

        reader.readAsDataURL(file);
    });
});