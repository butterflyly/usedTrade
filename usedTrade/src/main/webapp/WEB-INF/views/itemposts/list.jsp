<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="page-header">
    <h1>게시글 목록 (${categoryName})</h1>

    <sec:authorize access="isAuthenticated()">
        <a href="/item-posts/new?category=${categoryId}" class="btn-new-post">새 글 작성</a>
    </sec:authorize>
</div>

<!-- =======================
     판매 상태 탭
     ======================= -->
<div class="status-tab mb-3">
    <ul class="nav d-flex gap-2">
        <li class="nav-item">
            <a class="nav-link btn
               ${param.status == 'ON_SALE' || empty param.status ? 'btn-success text-white' : 'btn-outline-success'}"
               href="?status=ON_SALE&sort=${param.sort}&type=${type}&keyword=${keyword}">
                판매중
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link btn
               ${param.status == 'SOLD_OUT' ? 'btn-secondary text-white' : 'btn-outline-secondary'}"
               href="?status=SOLD_OUT&sort=${param.sort}&type=${type}&keyword=${keyword}">
                판매완료
            </a>
        </li>
    </ul>
</div>

<!-- =======================
     정렬 탭
     ======================= -->
<div class="sort-bar mb-3">
    <ul class="nav d-flex align-items-baseline gap-2">
        <span>정렬 기준</span>

        <li class="nav-item">
            <a class="nav-link btn btn-primary text-white
               ${param.sort == 'createdAt' || empty param.sort ? 'active' : ''}"
               href="?sort=createdAt&page=0&status=${param.status}&type=${type}&keyword=${keyword}">
                최신순
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link btn btn-primary text-white
               ${param.sort == 'views' ? 'active' : ''}"
               href="?sort=views&page=0&status=${param.status}&type=${type}&keyword=${keyword}">
                조회수순
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link btn btn-primary text-white
               ${param.sort == 'price' ? 'active' : ''}"
               href="?sort=price&page=0&status=${param.status}&type=${type}&keyword=${keyword}">
                가격순
            </a>
        </li>
    </ul>
</div>

<!-- =======================
     게시글 리스트
     ======================= -->
<div class="grid-container">
    <c:forEach var="post" items="${posts.content}">
        <a class="grid-item" href="/item-posts/detail/${post.id}">
            <c:if test="${not empty post.imageUrls}">
                <img src="${post.imageUrls[0]}" alt="썸네일"
                     style="width:120px; height:120px; object-fit:cover;">
            </c:if>

            <h3>${post.title}</h3>
            <p>가격: ${post.price}</p>
            <p>작성일: ${post.createdAtFormatted}</p>
            <p>작성자: <c:out value="${post.sellerDisplayName}"/></p>
            <p>조회수: ${post.views}</p>

            <c:if test="${post.itemStatus == 'SOLD_OUT'}">
                <span class="badge bg-secondary">판매완료</span>
            </c:if>
        </a>
    </c:forEach>
</div>

<!-- =======================
     검색 폼
     ======================= -->
<form class="search-form mt-4" method="get">
    <input type="hidden" name="status" value="${param.status}">
    <input type="hidden" name="sort" value="${param.sort}">

    <label for="searchType">검색 기준:</label>
    <select name="type" id="searchType">
        <option value="title" ${type == 'title' ? 'selected' : ''}>제목</option>
        <option value="title_Content" ${type == 'title_Content' ? 'selected' : ''}>제목/내용</option>
        <option value="nickname" ${type == 'nickname' ? 'selected' : ''}>닉네임</option>
    </select>

    <input type="text" name="keyword" value="${keyword}" placeholder="검색어 입력">
    <button type="submit">검색</button>
</form>

<!-- =======================
     페이징
     ======================= -->
<div class="pagination mt-4">
    <a href="?page=${posts.number - 1}&status=${param.status}&sort=${param.sort}&type=${type}&keyword=${keyword}"
       class="btn ${posts.first ? 'disabled' : ''}">
        &laquo; 이전
    </a>

    <c:forEach begin="0" end="${posts.totalPages - 1}" var="i">
        <c:choose>
            <c:when test="${i == posts.number}">
                <span class="current">${i + 1}</span>
            </c:when>
            <c:otherwise>
                <a href="?page=${i}&status=${param.status}&sort=${param.sort}&type=${type}&keyword=${keyword}">
                    ${i + 1}
                </a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <a href="?page=${posts.number + 1}&status=${param.status}&sort=${param.sort}&type=${type}&keyword=${keyword}"
       class="btn ${posts.last ? 'disabled' : ''}">
        다음 &raquo;
    </a>
</div>
