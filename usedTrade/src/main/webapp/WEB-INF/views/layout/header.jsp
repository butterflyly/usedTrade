<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<header class="site-header">
    <h1 class="site-title">중고거래 플랫폼</h1>
    <nav class="main-nav">
        <div id="nav-notification" class="nav-notification hidden">
            <button id="notificationBtn">
                🔔
                <span id="notificationBadge" class="badge hidden">0</span>
            </button>

            <div id="notificationDropdown" class="dropdown hidden">
                <ul id="notificationList"></ul>
            </div>
        </div>
        <a href="/">홈</a>
        <a href="/item-posts/1">게시글</a>
        <div id="auth-buttons"></div>


        <div class="dropdown">
            <button class="dropbtn">카테고리</button>
            <div class="dropdown-content">
                <c:forEach var="cat" items="${categories}">
                    <a href="/item-posts/${cat.id}">
                        <c:out value="${cat.name}"/>
                    </a>
                </c:forEach>
            </div>
        </div>
    </nav>
</header>
