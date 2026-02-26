<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<ul class="tabs">
    <li class="${empty status ? 'active' : ''}">
        <a href="/admin/users">전체</a>
    </li>
    <li class="${status == 'NORMAL' ? 'active' : ''}">
        <a href="/admin/users?status=NORMAL">일반</a>
    </li>
    <li class="${status == 'BLACK' ? 'active' : ''}">
        <a href="/admin/users?status=BLACK">블랙</a>
    </li>
</ul>

<div class="grid-container">
    <c:if test="${not empty users}">
        <c:forEach var="user" items="${users}">
            <a class="grid-item" href="/admin/users/${user.id}">
                <div class="user-header">
                    <h3>${user.displayNickname}</h3>

                    <c:if test="${user.userStatus == 'BLACK'}">
                        <span class="badge black">BLACK</span>
                    </c:if>

                    <c:if test="${user.userRole == 'ADMIN'}">
                        <span class="badge admin">ADMIN</span>
                    </c:if>
                </div>

                <p><strong>ID</strong> : ${user.username}</p>
                <p><strong>Email</strong> : ${user.email}</p>
                <p><strong>상태</strong> : ${user.userStatus}</p>
            </a>
        </c:forEach>
    </c:if>

    <c:if test="${pageInfo.totalPages > 1}">
        <div class="pagination">
            <c:forEach begin="0" end="${pageInfo.totalPages - 1}" var="i">
                <a href="?page=${i}&status=${status}"
                   class="${i == pageInfo.number ? 'active' : ''}">
                    ${i + 1}
                </a>
            </c:forEach>
        </div>
    </c:if>

</div>
<style>
    .grid-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
        gap: 20px;
    }

    .grid-item {
        border: 1px solid #ddd;
        padding: 16px;
        border-radius: 8px;
        background: #fff;
        text-decoration: none;
        color: #333;
        transition: all 0.2s ease;
    }

    .grid-item:hover {
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        transform: translateY(-2px);
    }

    .user-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 10px;
    }

    .badge {
        font-size: 12px;
        padding: 3px 6px;
        border-radius: 4px;
        font-weight: bold;
    }

    .badge.black {
        background: #333;
        color: #fff;
    }

    .badge.admin {
        background: #1976d2;
        color: #fff;
    }
</style>