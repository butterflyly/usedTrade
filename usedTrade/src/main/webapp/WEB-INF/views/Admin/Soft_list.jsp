<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<div class="grid-container">
    <c:forEach var="user" items="${users}">
        <a class="grid-item" href="/admin/users/${user.id}">
            <div class="user-header">
                <h3>${user.displayNickname}</h3>
            </div>

            <p><strong>ID</strong> : ${user.username}</p>
            <p><strong>Email</strong> : ${user.email}</p>
        </a>
    </c:forEach>
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