<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="mypage-container">

    <!-- ===================== -->
    <!-- 프로필 -->
    <!-- ===================== -->
    <div class="profile-box">
        <c:if test="${image != null}">
            <img src="${image.url}" alt="Profile picture">
        </c:if>

        <div class="user-info">
            <p><strong>아이디 :</strong> ${user.username}</p>
            <p><strong>닉네임 :</strong> ${user.nickname}</p>
            <p><strong>이메일 :</strong> ${user.email}</p>
        </div>
    </div>

    <!-- ===================== -->
    <!-- 탭 버튼 -->
    <!-- ===================== -->
    <!-- 탭 전용 영역 -->
    <div class="mypage-tabs">

        <div class="tab-buttons">
            <button class="tab-btn" data-tab="liked">찜한 게시글</button>
            <button class="tab-btn" data-tab="selling">판매 중 상품</button>
            <button class="tab-btn" data-tab="buying">구매 내역</button>
            <button class="tab-btn" data-tab="reviews">받은 후기</button>
        </div>

        <div class="tab-contents">

            <!-- ===================== -->
            <!-- 찜한 게시글 -->
            <!-- ===================== -->
            <div class="tab-content" id="tab-liked">
                <h2>내가 찜한 게시글</h2>


                <div class="grid-container mypage">

                    <c:if test="${empty likedPosts.content}">
                        <p>찜한 게시글이 없습니다.</p>
                    </c:if>

                    <c:forEach var="post" items="${likedPosts.content}">
                        <a class="grid-item mypage" href="/item-posts/detail/${post.itemId}">
                            <c:if test="${not empty post.itemImageUrl}">
                                <img src="${post.itemImageUrl[0]}" alt="썸네일">
                            </c:if>
                            <h3>${post.itemTitle}</h3>
                            <p>가격: ${post.itemPrice}원</p>
                            <p class="mypage-tag">❤️ 찜한 상품</p>
                        </a>
                    </c:forEach>
                </div>

                <c:if test="${likedPosts.totalPages > 0}">
                    <div class="pagination">

                        <c:if test="${likedPosts.hasPrevious()}">
                            <a href="?tab=liked&likedPage=${likedPosts.number - 1}">이전</a>
                        </c:if>

                        <c:forEach begin="0" end="${likedPosts.totalPages - 1}" var="i">
                            <a href="?tab=liked&likedPage=${i}"
                               class="${likedPosts.number == i ? 'active' : ''}">
                                ${i + 1}
                            </a>
                        </c:forEach>

                        <c:if test="${likedPosts.hasNext()}">
                            <a href="?tab=liked&likedPage=${likedPosts.number + 1}">다음</a>
                        </c:if>

                    </div>
                </c:if>
            </div>


            <!-- ===================== -->
            <!-- 판매 중 상품 -->
            <!-- ===================== -->
            <div class="tab-content" id="tab-selling">
                <h2>판매 중 상품</h2>


                <div class="grid-container mypage">

                    <c:if test="${empty sellingPosts.content}">
                        <p>현재 판매 중인 상품이 없습니다.</p>
                    </c:if>

                    <c:forEach var="post" items="${sellingPosts.content}">
                        <a class="grid-item mypage" href="/item-posts/detail/${post.id}">
                            <c:if test="${not empty post.imageUrls}">
                                <img src="${post.imageUrls[0]}" alt="썸네일">
                            </c:if>
                            <h3>${post.title}</h3>
                            <p>가격: ${post.price}원</p>
                            <span class="status selling">판매중</span>
                        </a>
                    </c:forEach>
                </div>

                <c:if test="${sellingPosts.totalPages > 0}">
                    <div class="pagination">

                        <c:if test="${sellingPosts.hasPrevious()}">
                            <a href="?tab=selling&sellingPage=${sellingPosts.number - 1}">이전</a>
                        </c:if>
                        <c:forEach begin="0" end="${sellingPosts.totalPages - 1}" var="i">
                            <a href="?tab=selling&sellingPage=${i}"
                               class="${sellingPosts.number == i ? 'active' : ''}">
                                ${i + 1}
                            </a>
                        </c:forEach>
                        <c:if test="${sellingPosts.hasNext()}">
                            <a href="?tab=selling&sellingPage=${sellingPosts.number + 1}">다음</a>
                        </c:if>
                    </div>
                </c:if>
            </div>

            <!-- ===================== -->
            <!-- 구매 상품 -->
            <!-- ===================== -->
            <div class="tab-content" id="tab-buying">
                <h2>구매 내역</h2>

                <div class="grid-container mypage">
                    <c:if test="${empty buyingDeals.content}">
                        <p>구매 내역이 없습니다.</p>
                    </c:if>

                    <c:forEach var="deal" items="${buyingDeals.content}">
                        <a class="grid-item mypage" href="/item-posts/detail/${deal.itemPostId}">
                            <img src="${deal.thumbnailUrl}">
                            <h3>${deal.title}</h3>
                            <p>${deal.price}원</p>
                            <p class="mypage-tag">판매자: ${deal.sellerNickname}</p>
                        </a>
                    </c:forEach>
                </div>

                <c:if test="${buyingDeals.totalPages > 0}">
                    <div class="pagination">

                        <c:if test="${buyingDeals.hasPrevious()}">
                            <a href="?tab=buying&buyingPage=${buyingDeals.number - 1}">
                                이전
                            </a>
                        </c:if>

                        <c:forEach begin="0" end="${buyingDeals.totalPages - 1}" var="i">
                            <a href="?tab=buying&buyingPage=${i}"
                               class="${buyingDeals.number == i ? 'active' : ''}">
                                ${i + 1}
                            </a>
                        </c:forEach>

                        <c:if test="${buyingDeals.hasNext()}">
                            <a href="?tab=buying&buyingPage=${buyingDeals.number + 1}">
                                다음
                            </a>
                        </c:if>

                    </div>
                </c:if>
            </div>

            <!-- ===================== -->
            <!-- 리뷰 상품 -->
            <!-- ===================== -->
            <div class="tab-content" id="tab-reviews">
                <h2>받은 후기</h2>

                <div class="review-list">
                    <c:if test="${empty receivedReviews.content}">
                        <p>아직 받은 후기가 없습니다.</p>
                    </c:if>

                    <c:forEach var="review" items="${receivedReviews.content}">
                        <div class="review-card">
                            <a href="/item-posts/detail/${review.itemPostId}">
                                <img src="${review.thumbnailUrl}">
                            </a>

                            <div class="review-info">
                                <h4>${review.title}</h4>
                                <p class="buyer">구매자: ${review.buyerNickname}</p>
                                <p class="content">"${review.reviewContent}"</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${receivedReviews.totalPages > 0}">
                    <div class="pagination">

                        <c:if test="${receivedReviews.hasPrevious()}">
                            <a href="?tab=reviews&reviewPage=${receivedReviews.number - 1}">
                                이전
                            </a>
                        </c:if>

                        <c:forEach begin="0" end="${receivedReviews.totalPages - 1}" var="i">
                            <a href="?tab=reviews&reviewPage=${i}"
                               class="${receivedReviews.number == i ? 'active' : ''}">
                                ${i + 1}
                            </a>
                        </c:forEach>

                        <c:if test="${receivedReviews.hasNext()}">
                            <a href="?tab=reviews&reviewPage=${receivedReviews.number + 1}">
                                다음
                            </a>
                        </c:if>

                    </div>
                </c:if>
            </div>

        </div>

    </div>

    <!-- ===================== -->
    <!-- 하단 버튼 -->
    <!-- ===================== -->
    <sec:authorize access="isAuthenticated()">
        <div class="action-buttons">
            <a href="/users/mypage/chats">
                💬 채팅방 목록
                <span id="chat-badge" class="badge"></span>
            </a>
            <a href="/users/info">회원 수정</a>
            <a href="/users/pwchange">비밀번호 변경</a>
            <form id="deleteForm">
                <button type="submit">회원탈퇴</button>
            </form>
        </div>
    </sec:authorize>
</div>
