<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="mypage-container">

    <div class="profile-box">
        <c:if test="${image != null}">
            <img src="${image.url}" alt="Profile picture">
        </c:if>

        <div class="user-info">
            <p><strong>닉네임 :</strong> ${user.nickname}</p>

            <p>
                <strong>상태 :</strong>
                <span class="user-status ${user.userStatus}">
                ${user.userStatus}
            </span>
            </p>
        </div>

        <select id="userStatusSelect" data-user-id="${user.id}">
            <option value="NORMAL" ${user.userStatus == 'NORMAL' ? 'selected' : ''}>일반</option>
            <option value="BLACK" ${user.userStatus == 'BLACK' ? 'selected' : ''}>블랙</option>
            <option value="REST" ${user.userStatus == 'REST' ? 'selected' : ''}>정지</option>
        </select>

        <button id="changeStatusBtn">상태 변경</button>

    </div>
</div>
<style>
    .user-status {
    padding: 3px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: bold;
}

    .user-status.ACTIVE {
        background-color: #e6f4ea;
        color: #1e7f34;
    }

    .user-status.RESTING {
        background-color: #f0f0f0;
        color: #666;
    }

    .user-status.BLOCKED {
        background-color: #fdecea;
        color: #c62828;
    }
</style>
<script>
    document.getElementById("changeStatusBtn").addEventListener("click", () => {
        const select = document.getElementById("userStatusSelect");
        const userId = select.dataset.userId;
        const newStatus = select.value;

        if (!confirm("유저 상태를 변경하시겠습니까?")) {
            return;
        }

        fetch(`/admin/users/${userId}/status`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ status: newStatus })
        })
        .then(res => {
            if (!res.ok) throw new Error();
            alert("상태가 변경되었습니다.");
            location.reload();
        })
        .catch(() => {
            alert("상태 변경에 실패했습니다.");
        });
    });
</script>