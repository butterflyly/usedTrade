<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="user-password-wrapper">

    <h2>비밀번호 변경</h2>

    <form id="pwChangeForm">

        <div>
            <label>현재 비밀번호</label>
            <input type="password" name="prePassword">
            <div class="error" id="prePwError"></div>
        </div>

        <div>
            <label>새 비밀번호</label>
            <input type="password" id="newPassword1" name="newPassword1">
            <div class="error" id="newPwError"></div>
        </div>

        <div>
            <label>새 비밀번호 확인</label>
            <input type="password" id="newPassword2" name="newPassword2">
            <div class="error" id="newPwMatchError"></div>
        </div>

        <button type="submit">비밀번호 변경</button>
    </form>

</div>
