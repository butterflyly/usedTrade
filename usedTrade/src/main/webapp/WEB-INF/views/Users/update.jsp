<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="user-update-wrapper">

    <h2>회원 정보 수정</h2>
    <hr/>

    <form:form
            modelAttribute="userUpdateForm"
            action="/users/info"
            method="post"
            enctype="multipart/form-data">

        <div>
            <label for="nickname">닉네임</label>
            <form:input path="nickname" id="nickname"/>
            <div id="errorBox"></div>
        </div>

        <c:if test="${not empty image}">
            <div class="profile-preview">
                <img src="${image.url}" alt="Profile picture">
            </div>
        </c:if>

        <div>
            <label for="profileImage">프로필 이미지</label>
            <input type="file" id="profileImage" name="profileImage">
        </div>

        <div class="button-group">
            <button type="button" id="updateBtn" class="btn-primary">
                수정하기
            </button>
            <a href="/users/mypage" class="btn-secondary">취소</a>
        </div>

    </form:form>

</div>