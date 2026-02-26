<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<h1>게시글 등록
</h1>
<!-- 에러 영역 -->
<div id="error-box"></div>

<!-- 폼 -->
<form id="postForm" enctype="multipart/form-data">

    <div class="form-group">
        <label for="title">제목:</label>
        <input type="text" id="title" name="title" value="${itemPostWriteForm.title}">

    </div>

    <div class="form-group">
        <label for="price">가격:</label>
        <input type="number" id="price" name="price" value="${itemPostWriteForm.price}">
    </div>


    <div class="form-group">
        <label for="content">내용:</label>
        <textarea id="content" name="content">${itemPostWriteForm.content}</textarea>
    </div>

    <div class="form-group">
        <label for="categoryId">카테고리:</label><br>
        <select id="categoryId" name="categoryId">
            <option value="">-- 카테고리 선택 --</option>
            <c:forEach var="c" items="${categories}">
                <option value="${c.id}"
                        ${c.id == itemPostWriteForm.categoryId ? 'selected' : ''}>
                        >${c.name}
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="form-group">
        <label>이미지 업로드:</label><br>
        <input type="file" name="files" multiple>
        <div id="preview" class="image-grid"></div>
    </div>

    <div class="form-actions">
        <button type="submit">등록</button>
    </div>
</form>
