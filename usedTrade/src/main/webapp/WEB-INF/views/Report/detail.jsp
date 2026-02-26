<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-report-detail">

    <h2>신고 상세</h2>

    <!-- 기본 정보 -->
    <table class="detail-table">
        <tr>
            <th>신고 ID</th>
            <td>${report.id}</td>
        </tr>

        <tr>
            <th>신고자</th>
            <td>
                <c:out value="${report.reporter.displayNickname}" />
                (<c:out value="${report.reporter.username}" />)
            </td>
        </tr>

        <tr>
            <th>신고 대상 유저</th>
            <td>
                <c:out value="${report.targetUser.displayNickname}" />
                (<c:out value="${report.targetUser.username}" />)
            </td>
        </tr>

        <tr>
            <th>신고 게시글</th>
            <td>
                <c:if test="${not empty report.itemPost}">
                    <a href="/item-posts/detail/${report.itemPost.id}" target="_blank">
                        <c:out value="${report.itemPost.title}" />
                    </a>
                </c:if>
            </td>
        </tr>

        <tr>
            <th>신고 사유</th>
            <td>${report.reason}</td>
        </tr>

        <tr>
            <th>신고 상태</th>
            <td>
                <span class="status ${report.status}">
                    ${report.status}
                </span>
            </td>
        </tr>

        <tr>
            <th>신고 일시</th>
            <td>${report.createdAt}</td>
        </tr>

        <tr>
            <th>상세 내용</th>
            <td class="content">
                <c:out value="${report.content}" />
            </td>
        </tr>
    </table>

    <!-- 처리 버튼 영역 (아직 기능 없음) -->
    <div class="action-buttons">
        <form action="/admin/reports/${report.id}/process" method="post" style="display:inline">
            <button type="submit">경고 처리</button>
        </form>

        <form action="/admin/reports/${report.id}/reject" method="post" style="display:inline">
            <button type="submit">반려</button>
        </form>

        <a href="javascript:history.back()">목록으로</a>
    </div>

</div>

<!-- 최소 CSS (임시) -->
<style>
    .admin-report-detail {
        padding: 20px;
    }

    .detail-table {
        width: 100%;
        border-collapse: collapse;
        margin-bottom: 20px;
    }

    .detail-table th {
        width: 160px;
        background-color: #f5f5f5;
        text-align: left;
    }

    .detail-table th,
    .detail-table td {
        border: 1px solid #ddd;
        padding: 10px;
        vertical-align: top;
    }

    .detail-table td.content {
        white-space: pre-wrap;
    }

    .status.RECEIVED {
        color: red;
        font-weight: bold;
    }

    .status.IN_PROGRESS {
        color: orange;
        font-weight: bold;
    }

    .status.RESOLVED {
        color: green;
        font-weight: bold;
    }

    .action-buttons {
        margin-top: 20px;
    }

    .action-buttons button,
    .action-buttons a {
        margin-right: 10px;
        padding: 6px 12px;
    }
</style>