<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="chat-list-container">

    <h2>💬 내 채팅방 목록</h2>

    <c:if test="${empty chatRooms.content}">
        <p class="empty">채팅방이 없습니다.</p>
    </c:if>

    <c:forEach var="room" items="${chatRooms.content}">
        <a href="/chat/room/${room.roomId}" class="chat-room">

            <div class="left">
                <div class="nickname">
                    ${room.opponent.displayNickname}
                </div>

                <div class="role">
                    <c:choose>
                        <c:when test="${room.seller}">
                            판매중
                        </c:when>
                        <c:otherwise>
                            구매중
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="right">
                <c:if test="${room.unreadCount > 0}">
                    <span class="unread-badge">${room.unreadCount}</span>
                </c:if>
            </div>

        </a>
    </c:forEach>

    <!-- 페이징 -->
    <c:if test="${chatRooms.totalPages > 1}">
        <div class="pagination">

            <c:if test="${chatRooms.hasPrevious()}">
                <a href="?page=${chatRooms.number - 1}">이전</a>
            </c:if>

            <c:forEach begin="0" end="${chatRooms.totalPages - 1}" var="i">
                <a href="?page=${i}"
                   class="${chatRooms.number == i ? 'active' : ''}">
                    ${i + 1}
                </a>
            </c:forEach>

            <c:if test="${chatRooms.hasNext()}">
                <a href="?page=${chatRooms.number + 1}">다음</a>
            </c:if>

        </div>
    </c:if>

</div>