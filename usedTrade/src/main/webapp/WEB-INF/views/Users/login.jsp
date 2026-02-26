<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="login-wrapper">
    <form onsubmit="login(event)" class="login-form">
        <label for="username">아이디</label>
        <input type="text" id="username" name="username" placeholder="아이디를 입력해주세요">

        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요">

        <button type="submit">로그인</button>

        <div class="find-links">
            <label>
                <input type="checkbox" name="rememberMe">
                자동 로그인
            </label>
            <a href="#">아이디 찾기</a> · <a href="#">비밀번호 찾기</a>
        </div>

        <div class="social-login">
            <a href="/oauth2/authorization/google" class="btn-google">구글 로그인</a>
            <a href="/oauth2/authorization/naver" class="btn-naver">네이버 로그인</a>
        </div>

        <div class="register">
            <a href="/users/register">회원가입</a>
        </div>
    </form>
</div>

