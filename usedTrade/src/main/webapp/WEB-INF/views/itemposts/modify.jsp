<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h1>게시글 수정
</h1>

<!-- 에러 영역 -->
<div id="error-box"></div>

<div class="update-container">

    <!-- 왼쪽: 이미지 영역 (40%) -->
    <div class="image-section">
        <h3>기존 이미지</h3>

        <c:if test="${empty imageList}">
            <p>등록된 이미지가 없습니다.</p>
        </c:if>

        <div class="image-grid" id="existingImages">
            <c:forEach var="img" items="${imageList}">
                <div class="image-item"
                     data-image-id="${img.imageId}"
                     data-image-url="${img.url}">

                    <img src="${img.url}" />

                    <button type="button"
                            class="delete-btn"
                            onclick="markImageDeleted(this)">
                        ✕ 삭제
                    </button>
                </div>
            </c:forEach>
        </div>
        <!-- 🔥 삭제 예정 이미지 URL 담는 영역 -->
        <div id="deletedImageInputs"></div>
    </div>

    <!-- 오른쪽: 수정 폼 영역 (60%) -->
    <div class="form-section">
        <!-- 폼 -->
        <form id="postForm" enctype="multipart/form-data">

            <input type="hidden" id="postId" name="postId" value="${id}">
            <label for="title">제목:</label><br>
            <input type="text" id="title" name="title" value="${itemPostWriteForm.title}">
            <br><br>

            <label for="price">가격:</label><br>
            <input type="number" id="price" name="price" value="${itemPostWriteForm.price}">
            <br><br>

            <label for="content">내용:</label><br>
            <textarea id="content" name="content">${itemPostWriteForm.content}</textarea>
            <br><br>


            <label for="categoryId">카테고리:</label><br>
            <select id="categoryId" name="categoryId">
                <option value="">-- 카테고리 선택 --</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.id}"
                    <c:if test="${itemPostWriteForm.categoryId eq c.id}">
                        selected="selected"
                    </c:if>>
                    ${c.name}
                    </option>
                </c:forEach>
            </select>


            <label>새 이미지 추가 : </label><br>
            <input type="file" id="files" multiple onchange="console.log(this.files)">
            <br><br>

            <button type="submit">수정</button>
        </form>
    </div>
</div>
<!-- ✅ 검증 통과 시 JS fetch 실행 -->

