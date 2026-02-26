<%@ page contentType="text/html;charset=UTF-8" %>

<div class="admin-user-detail">

    <h2>탈퇴 유저 상세</h2>

    <table class="detail-table">
        <tr>
            <th>ID</th>
            <td>${user.username}</td>
        </tr>
        <tr>
            <th>이메일</th>
            <td>${user.email}</td>
        </tr>
        <tr>
            <th>닉네임</th>
            <td>${user.displayNickname}</td>
        </tr>
        <tr>
            <th>탈퇴일</th>
            <td>${user.deleteDate}</td>
        </tr>
    </table>

    <div class="actions">
        <button onclick="restoreUser(${user.id})">복원</button>
        <a href="javascript:history.back()">목록</a>
    </div>

</div>
<script>
    function restoreUser(userId) {

        if (!confirm("해당 사용자를 복원하시겠습니까?")) {
            return;
        }

        fetch(`/admin/users/${userId}/restore`, {
            method: "POST"
        })
        .then(res => {
            if (!res.ok) throw new Error();
            alert("유저가 복원되었습니다.");
            location.href = "/admin/users/deleted";
        })
        .catch(() => {
            alert("복원 중 오류가 발생했습니다.");
        });
    }
</script>