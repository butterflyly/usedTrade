<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="item-detail-container">

    <!-- 왼쪽: 이미지 -->
    <div class="image-section">
        <div class="main-image">
            <img id="mainImage" src="${post.imageUrls[0]}" alt="상품 이미지">

            <div id="imageData"
                 data-images='[
                <c:forEach var="img" items="${post.imageUrls}" varStatus="s">
                    "${img}"<c:if test="${!s.last}">,</c:if>
                </c:forEach>
             ]'>
            </div>

            <button class="arrow left" onclick="prevImage()">‹</button>
            <button class="arrow right" onclick="nextImage()">›</button>
        </div>
    </div>

    <!-- 오른쪽: 정보 -->
    <div class="info-section">

        <h2 class="title">${post.title}</h2>
        <div class="price">${post.price}원</div>

        <div class="meta">
            <span>${post.createdAtFormatted}</span>
        </div>

        ${post.sellerNickname}

        <!-- 상태 영역 -->
        <div class="status">

            <c:choose>
                <c:when test="${post.itemStatus == 'ON_SALE'}">판매중</c:when>
                <c:when test="${post.itemStatus == 'RESERVED'}">예약중</c:when>
                <c:when test="${post.itemStatus == 'SOLD_OUT'}">판매완료</c:when>
            </c:choose>

            <!-- 판매자 상태 변경 -->
            <c:if test="${loginUserId == post.seller.id}">
                <div class="mt-3">
                    <label>판매 상태 변경:</label>
                    <select id="statusSelect" onchange="changeStatus(${post.id})">
                        <option value="ON_SALE" ${post.itemStatus == 'ON_SALE' ? 'selected' : ''}>판매중</option>
                        <option value="RESERVED" ${post.itemStatus == 'RESERVED' ? 'selected' : ''}>예약중</option>
                        <option value="SOLD_OUT" ${post.itemStatus == 'SOLD_OUT' ? 'selected' : ''}>판매완료</option>
                        <option value="CANCELED" ${post.itemStatus == 'CANCELED' ? 'selected' : ''}>거래취소</option>
                    </select>
                </div>
            </c:if>

            <!-- 판매자 확정 -->
            <c:if test="${loginUserId == post.seller.id && post.itemStatus == 'RESERVED' && post.hasDeal}">
                <button onclick="confirmDeal(${post.dealId})">판매확정</button>
                <button onclick="cancelDeal(${post.dealId})">판매취소</button>
            </c:if>

            <!-- 구매자 -->
            <c:if test="${loginUserId != null && loginUserId != post.seller.id && post.itemStatus == 'ON_SALE'}">
                <button onclick="requestDeal(${post.id})">구매확정</button>
            </c:if>

            <c:if test="${loginUserId != null && loginUserId != post.seller.id && post.itemStatus == 'RESERVED' && post.hasDeal}">
                <button onclick="cancelDeal(${post.dealId})">구매취소</button>
            </c:if>
        </div>

        <!-- 구매후기 작성 버튼 -->
        <c:if test="
            ${post.itemStatus == 'SOLD_OUT'
              && dealId != null
              && loginUserId == dealBuyerId
              && review == null}">
            <div class="review-action">
                <button class="review-btn" onclick="openReviewModal(${dealId})">
                    구매후기 작성
                </button>
            </div>
        </c:if>



        <c:if test="${review != null && loginUserId == review.buyerId}">
            <div class="review-actions">
                <button onclick="openEditReview('${review.content}')">수정</button>
                <button onclick="deleteReview(${dealId})">삭제</button>
            </div>
        </c:if>

        <!-- 본문 -->
        <div class="content-section">
            <p>${post.content}</p>
        </div>

        <!-- 구매후기 표시 -->
        <c:if test="${review != null}">
            <div class="review-section">
                <h3>구매후기</h3>

                <div class="review-box">
                    <div class="review-header">
                        <span class="review-writer">${review.buyerNickname}</span>
                        <span class="review-date">${review.createdAtFormatted}</span>
                    </div>

                    <div class="review-content">
                        ${review.content}
                    </div>
                </div>
            </div>
        </c:if>

        <!-- 액션 -->
        <div class="actions">
            <span id="heart"
                  class="heart-btn ${isLikedByCurrentUser ? 'liked' : 'unliked'}"
                  onclick="toggleLike(${post.id})">♥</span>

             <button onclick="openChat(${post.id})">채팅하기</button>

            <!-- 신고 버튼 (로그인 유저만) -->
            <c:if test="${loginUserId != null && loginUserId != post.seller.id}">
                <button class="report-btn"
                        onclick="openReportModal('POST', ${post.id})">
                    신고
                </button>
            </c:if>
        </div>
    </div>

</div>

<!-- 상품 영역 끝난 뒤 -->
<div class="bottom-actions">
    <c:if test="${loginUserId == post.seller.id}">
        <a href="/item-posts/modify/${post.id}" class="btn edit-btn">
            수정
        </a>

        <button class="btn delete-btn"
                onclick="deletePost(${post.id})">
            삭제
        </button>
    </c:if>
</div>

<!-- 신고 모달 -->
<div id="reportModal" class="report-modal hidden">
    <div class="modal-content">
        <h3>신고하기</h3>

        <label>신고 사유</label>
        <select id="reportReason">
            <option value="">선택하세요</option>
            <option value="SCAM">사기 의심</option>
            <option value="ABUSE">욕설 / 비방</option>
            <option value="SPAM">광고 / 도배</option>
            <option value="ETC">기타</option>
        </select>

        <label>상세 내용</label>
        <textarea id="reportContent"
                  placeholder="신고 내용을 입력해주세요 (선택)"></textarea>

        <div class="modal-actions">
            <button onclick="submitReport()">신고</button>
            <button onclick="closeReportModal()">취소</button>
        </div>
    </div>
</div>