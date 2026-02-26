<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>회원가입</h2>

<!-- 🔴 에러 영역 -->
<div id="error-box"
     style="display:none; color:red; border:1px solid red; padding:10px; margin-bottom:15px;">
</div>

<form id="registerForm" enctype="multipart/form-data">

    <div class="form-group">
        <label for="username">아이디</label>
        <div class="input-row">
            <input type="text" id="username" name="username"
                   placeholder="영문, 숫자 포함 6~20자">
            <button type="button" id="checkUsernameBtn"
                    onclick="checkDuplicateUsername()">중복확인</button>
            <button type="button" id="changeUsernameBtn"
                    onclick="resetField('#username','#checkUsernameBtn','#changeUsernameBtn','username')"
                    class="change-btn">변경</button>
        </div>
    </div>

    <div class="form-group">
        <label for="nickname">닉네임</label>
        <div class="input-row">
            <input type="text" id="nickname" name="nickname"
                   placeholder="한글, 영문, 숫자 4~12자">
            <button type="button" id="checkNicknameBtn"
                    onclick="checkDuplicateNickname()">중복확인</button>
            <button type="button" id="changeNicknameBtn"
                    onclick="resetField('#nickname','#checkNicknameBtn','#changeNicknameBtn','nickname')"
                    class="change-btn">변경</button>
        </div>
    </div>

    <div class="form-group">
        <label for="email">이메일</label>
        <div class="input-row">
            <input type="text" id="email" name="email"
                   placeholder="example@email.com">
            <button type="button" id="checkEmailBtn"
                    onclick="checkDuplicateEmail()">중복확인</button>
            <button type="button" id="changeEmailBtn"
                    onclick="resetField('#email','#checkEmailBtn','#changeEmailBtn','email')"
                    class="change-btn">변경</button>
        </div>
    </div>

    <div class="form-group">
        <label for="password">비밀번호</label>
        <div class="input-row">
            <input type="password" id="password" name="password"
                   placeholder="영문+숫자 포함 12자 이상">
        </div>
        <div id="passwordError" class="error-text"></div>
    </div>

    <div class="form-group">
        <label for="password2">비밀번호 확인</label>
        <div class="input-row">
            <input type="password" id="password2" name="password2"
                   placeholder="비밀번호 재입력">

        <div id="passwordMatchError" class="error-text"></div>

    </div>

    <div class="form-group">
        <label for="files">프로필 이미지</label>
        <div class="input-row">
            <input type="file" id="files" name="files" accept="image/*">
        </div>
    </div>

    <button type="submit" class="submit-btn">회원가입</button>

</form>