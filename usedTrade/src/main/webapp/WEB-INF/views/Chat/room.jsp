<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="chat-room">

    <div class="chat-header">
        <div>
            <h3>${room.post.title}</h3>
            <span>상대방: ${opponent.nickname}</span>
        </div>

        <button id="leaveBtn" class="leave-btn">나가기</button>
    </div>

    <div id="message-area" class="chat-messages"></div>

    <div class="chat-input">
        <input type="text"
               id="messageInput"
               placeholder="메시지를 입력하세요"
               autocomplete="off" />
        <button id="sendBtn">전송</button>
    </div>

</div>
<script>
    const roomId = ${room.id};
    const myUsername = "${myUsername}";
</script>