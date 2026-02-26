document.getElementById("postForm").addEventListener("submit", async e => {

   e.preventDefault();

   const postId = document.getElementById("postId").value;

   console.log("🔥 postId =", postId);
   console.log("🔥 typeof postId =", typeof postId);

   const form = e.target;
   const errorBox = document.getElementById("error-box");
   errorBox.style.display = "none";
   errorBox.innerHTML = "";

   const formData = new FormData();
   formData.append("title", form.title.value);
   formData.append("price", form.price.value);   // ✅ price 있음
   formData.append("content", form.content.value);
   formData.append("categoryId", form.categoryId.value);

   // 파일
   for (let file of form.files.files) {
       formData.append("files", file);
   }

    // 🔥 4️⃣ 삭제할 이미지 ID 추가 (여기가 핵심 위치)
    document
    .querySelectorAll("#deletedImageInputs input[name='deleteImageIds']")
    .forEach(input => {
        formData.append("deleteImageIds", input.value);
    });

    for (let [key, value] of formData.entries()) {
        console.log(key, "=", value);
    }

   const res = await fetch("/api/item-posts/update/" + postId, {
       method: "PUT",
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

   alert("수정 완료!");
   location.href = "/item-posts";
});

function markImageDeleted(btn) {
    const imageItem = btn.closest(".image-item");

    const imageId = imageItem.dataset.imageId;

    // hidden input
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "deleteImageIds";
    input.value = imageId;

    document.getElementById("deletedImageInputs").appendChild(input);

    // UX
    imageItem.remove();
}