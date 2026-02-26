
 // 게시글 삭제 함수
 function deletePost(id) {
    if (confirm('정말 삭제하시겠습니까?')) {
        fetch('/api/item-posts/delete/' + id, {
            method: 'DELETE',
            credentials: 'include' // 쿠키(JWT) 포함
        })
        .then(response => {
            if (response.ok) {
                alert('삭제 완료');
                window.location.href = '/item-posts';
            } else {
                alert('삭제 실패');
            }
        })
        .catch(() => alert('서버 오류가 발생했습니다.'));
    }
}


function openChatWindow(roomId) {
    if (document.getElementById(`chat-room-${roomId}`)) return;

    const div = document.createElement('div');
    div.id = `chat-room-${roomId}`;
    div.classList.add("chat-popup");
    div.innerHTML = `
        <div class="chat-header">채팅방 ${roomId}</div>
        <div class="chat-content" id="chat-content-${roomId}"></div>
        <input type="text" onkeydown="sendMessage(event, ${roomId})">
    `;
    document.body.appendChild(div);
 //   subscribe(roomId);
}

// 채팅방 상태 함수
function changeStatus(postId) {
    const newStatus = document.getElementById("statusSelect").value;

    fetch(`/api/item-posts/status/ ` + postId, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ status: newStatus })
    })
    .then(res => {
        if (res.ok) {
            alert("상태가 변경되었습니다.");
            location.reload();
        } else {
            alert("상태 변경 실패");
        }
    })
    .catch(() => alert("서버 오류"));
}

// 추천 함수
function toggleLike(postId) {
    fetch(`/api/item-posts/vote/` + postId, {
    method: "POST",
    headers: {
        "X-Requested-With": "XMLHttpRequest"
    }
    }).then(response => {
    // ❗ 실패 처리 (자기 게시글 찜 금지 등)
    if (!response.ok) {
        return response.text().then(msg => {
            alert(msg);   // 백엔드에서 준 메시지 alert
        });
    }

    if (response.ok) {
        const heart = document.getElementById("heart");

        // liked → unliked 토글
        if (heart.classList.contains("liked")) {
            heart.classList.remove("liked");
            heart.classList.add("unliked");
        } else {
            heart.classList.remove("unliked");
            heart.classList.add("liked");
        }
    } else {
        alert("로그인이 필요합니다.");
    }
    });
}

  // 채팅방 오픈 함수
async function openChat(postId) {
    const res = await fetch(`/api/chat/room?itemPostId=${postId}`, {
        method: "POST"
    });

    if (!res.ok) {
        const msg = await res.text();
        alert(msg);
        return;
    }

    console.log("채팅방 ㄱㄱ");

    const room = await res.json();
    location.href = `/chat/room/${room.roomId}`;
}


function openChatModal(roomId) {
    document.getElementById("chatModal").style.display = "block";
   // connectWebSocket(roomId);
}

// 채팅방 닫기
 function closeChat() {
    const modal = document.getElementById("chatModal");
    if (modal) {
        modal.style.display = "none";
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const imageData = document.getElementById("imageData");
    if (!imageData) return;

    window.images = JSON.parse(imageData.dataset.images);
    window.currentIndex = 0;

    showImage(currentIndex);
});

function showImage(index) {
    const img = document.getElementById("mainImage");
    if (!img || !images.length) return;


    img.src = images[index];
}

function prevImage() {
    currentIndex = (currentIndex - 1 + images.length) % images.length;
    showImage(currentIndex);
}

function nextImage() {
    currentIndex = (currentIndex + 1) % images.length;
    showImage(currentIndex);
}

// 구매 판매확정
function requestDeal(postId) {
    fetch(`/deals/request/${postId}`, { method: "POST" })
        .then(res => {
            if (!res.ok) throw new Error();
            location.reload();
        });
}

function confirmDeal(dealId) {
    fetch(`/deals/confirm/${dealId}`, { method: "POST" })
        .then(res => {
            if (!res.ok) throw new Error();
            location.reload();
        });
}

function cancelDeal(dealId) {
    fetch(`/deals/cancel/${dealId}`, { method: "POST" })
        .then(res => {
            if (!res.ok) throw new Error();
            location.reload();
        });
}


function updateReview(dealId) {
    const content = document.getElementById("reviewContent").value;

    fetch(`/api/reviews/${dealId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ content })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("후기가 수정되었습니다.");
        location.reload();
    });
}

function deleteReview(dealId) {
    if (!confirm("후기를 삭제하시겠습니까?")) return;

    fetch(`/api/reviews/${dealId}`, {
        method: "DELETE"
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("후기가 삭제되었습니다.");
        location.reload();
    });
}

let reportTargetId = null;

function openReportModal(type, targetId) {
    reportTargetId = targetId;
    document.getElementById("reportModal").classList.remove("hidden");
}

function closeReportModal() {
    document.getElementById("reportModal").classList.add("hidden");
}

function submitReport() {
    const reason = document.getElementById("reportReason").value;
    const content = document.getElementById("reportContent").value;

    if (!reason) {
        alert("신고 사유를 선택해주세요.");
        return;
    }

    fetch("/api/reports", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            targetId: reportTargetId,
            reason: reason,
            content: content
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        alert("신고가 접수되었습니다.");
        closeReportModal();
    })
    .catch(() => {
        alert("신고 처리 중 오류가 발생했습니다.");
    });
}