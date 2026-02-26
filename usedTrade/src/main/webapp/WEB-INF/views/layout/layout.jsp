<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>

    <link rel="stylesheet" href="/css/LayOut/common.css">
    <c:if test="${not empty pageCss}">
        <link rel="stylesheet" href="${pageCss}">
    </c:if>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <title><c:out value="${pageTitle}" /></title>
</head>
<body>

    <!-- Header include -->
    <jsp:include page="/WEB-INF/views/layout/header.jsp" />

    <!-- 페이지별 콘텐츠 -->
    <main>
        <jsp:include page="/WEB-INF/views/${body}.jsp" />
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <script>
        const userId = "${loginUser.id}";

                if (userId) {
                    const socket = new SockJS("/ws");
                    const stompClient = Stomp.over(socket);

                    stompClient.connect({}, () => {
                        stompClient.subscribe(
                            `/sub/notifications/${userId}`,
                            (message) => {
                                const notification = JSON.parse(message.body);
                                showNotification(notification);
                            }
                        );
                    });
                }



                let unreadCount = 0;

                li.onclick = () => {
                    fetch(`/api/notifications/${n.id}/read`, { method: "POST" });
                    window.location.href = n.link;
                };


            function showNotification(n) {
                unreadCount++;

                const badge = document.getElementById("notificationBadge");
                badge.innerText = unreadCount;
                badge.classList.remove("hidden");

                const list = document.getElementById("notificationList");
                const li = document.createElement("li");
                li.innerText = n.message;
                list.prepend(li);
            }
    </script>


    <script src="/js/layout/layout.js"></script>
    <c:if test="${not empty pageJs}">
        <script src="${pageJs}"></script>
    </c:if>

    <!-- Footer include -->
    <jsp:include page="/WEB-INF/views/layout/footer.jsp" />


</body>
</html>