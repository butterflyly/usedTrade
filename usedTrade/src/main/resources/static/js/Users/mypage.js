 document.getElementById("deleteForm").addEventListener("submit", function(e) {
    e.preventDefault(); // 기본 submit 막기

    if (!confirm("정말 탈퇴하시겠습니까?")) {
        return;
    }

    fetch("/api/users/delete", {
        method: "DELETE",
        credentials: "include" // 쿠키 포함
    })
    .then(response => {
        if (response.ok) {
            alert("탈퇴가 완료되었습니다.");

            // 🔥 쿠키 삭제
            document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "refreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            // 🔥 메인 페이지로 이동
            window.location.href = "/item-posts";
        } else {
            alert("탈퇴 실패");
        }
    })
    .catch(err => {
        alert("오류 발생");
    });

});

document.querySelectorAll(".tab-btn").forEach(btn => {
    btn.addEventListener("click", () => {

        // 버튼 active 토글
        document.querySelectorAll(".tab-btn")
            .forEach(b => b.classList.remove("active"));
        btn.classList.add("active");

        // 콘텐츠 전환
        const tab = btn.dataset.tab;
        document.querySelectorAll(".tab-content")
            .forEach(c => c.classList.remove("active"));
        document.getElementById("tab-" + tab).classList.add("active");
    });
});


// 채팅방
const badge = document.getElementById("chat-badge");

fetch("/api/chat/unread-count")
  .then(res => res.text())
  .then(count => {
      const unread = parseInt(count, 10);
      if (unread > 0) {
          badge.textContent = unread;
          badge.style.display = "inline-block";
      } else {
          badge.style.display = "none";
      }
  });

const params = new URLSearchParams(window.location.search);
const activeTab = params.get("tab") || "liked";

document.querySelectorAll(".tab-btn").forEach(btn => {
    if (btn.dataset.tab === activeTab) {
        btn.classList.add("active");
        document.getElementById("tab-" + activeTab).classList.add("active");
    }
});