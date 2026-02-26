const socket = new SockJS('/ws/chat');
const stomp = Stomp.over(socket);

stomp.connect({}, () => {
    console.log("✅ WebSocket 연결됨");

    // ✅ 구독 경로 수정
    stomp.subscribe(`/sub/chat/${roomId}`, (message) => {
        const data = JSON.parse(message.body);
        renderMessage(data);
    });
});

async function loadMessages() {
    const res = await fetch(`/api/chat/rooms/${roomId}/messages`);
    if (!res.ok) return;

    const messages = await res.json();
    messages.forEach(renderMessage);
}

loadMessages();

// 메시지 전송
document.getElementById("sendBtn").addEventListener("click", sendMessage);
document.getElementById("messageInput").addEventListener("keydown", e => {
    if (e.key === "Enter") sendMessage();
});

function sendMessage() {
    const input = document.getElementById("messageInput");
    const content = input.value.trim();
    if (!content) return;

    stomp.send("/pub/chat/send", {}, JSON.stringify({
        roomId: roomId,
        content: content
    }));

    input.value = "";
}

function renderMessage(msg) {
    const area = document.getElementById("message-area");
    const div = document.createElement("div");

    const isMine = msg.senderUsername === myUsername;

    div.className = isMine
        ? "chat-message mine"
        : "chat-message other";

    const nickname = document.createElement("strong");
    nickname.textContent = msg.senderNickname;

    const content = document.createElement("span");
    content.textContent = msg.content;

    const time = document.createElement("small");
    time.textContent = formatTime(msg.createdAt);

    div.appendChild(nickname);
    div.appendChild(content);
    div.appendChild(time);

    area.appendChild(div);
    area.scrollTop = area.scrollHeight;
}

function formatTime(iso) {
    const date = new Date(iso);
    return date.toLocaleTimeString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit"
    });
}

document.getElementById("leaveBtn").addEventListener("click", () => {
    if (!confirm("채팅방을 나가시겠습니까?")) return;

    fetch(`/api/chat/rooms/${roomId}/leave`, {
        method: "POST"
    })
    .then(() => {
        disconnect(); // websocket 종료
        location.href = "/chat/rooms";
    });
});

function disconnect() {
    if (stompClient) {
        stompClient.disconnect();
    }
}
