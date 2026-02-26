<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="admin-report-container">

    <h2>신고 목록</h2>

    <!-- 🔍 신고 사유 탭 -->
    <div class="report-reason-tabs">
        <a href="/admin/report/SCAM">사기</a>
        <a href="/admin/report/ABUSE">욕설</a>
        <a href="/admin/report/SPAM">스팸</a>
        <a href="/admin/report/ILLEGAL">불법</a>
        <a href="/admin/report/ETC">기타</a>
    </div>

    <!-- 📋 신고 목록 테이블 -->
    <table class="report-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>신고자</th>
            <th>신고 대상 유저</th>
            <th>게시글</th>
            <th>사유</th>
            <th>상태</th>
            <th>신고일</th>
            <th>관리</th>
        </tr>
        </thead>

        <tbody>
        <c:if test="${empty reports.content}">
            <tr>
                <td colspan="8" style="text-align:center;">신고 내역이 없습니다.</td>
            </tr>
        </c:if>

        <c:forEach var="report" items="${reports.content}">
            <tr>
                <td>${report.id}</td>

                <td>
                    <c:out value="${report.reporter.displayNickname}" />
                </td>

                <td>
                    <c:out value="${report.targetUser.displayNickname}" />
                </td>

                <td>
                    <c:if test="${not empty report.itemPost}">
                        <a href="/item-posts/detail/${report.itemPost.id}" target="_blank">
                            <c:out value="${report.itemPost.title}" />
                        </a>
                    </c:if>
                </td>

                <td>${report.reason}</td>

                <td>
                    <span class="status ${report.status}">
                        ${report.status}
                    </span>
                </td>

                <td>
                    ${report.createdAt}
                </td>

                <td>
                    <a href="/admin/report/detail/${report.id}">상세</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- 📌 페이징 -->
    <div class="pagination">
        <c:if test="${reports.hasPrevious()}">
            <a href="?page=${reports.number - 1}">이전</a>
        </c:if>

        <span>
            ${reports.number + 1} / ${reports.totalPages}
        </span>

        <c:if test="${reports.hasNext()}">
            <a href="?page=${reports.number + 1}">다음</a>
        </c:if>
    </div>
    <div>
        <a href="/admin/users">유저 데이터로</a>
    </div>

</div>

<!-- 💡 최소 CSS (임시) -->
<style>
    .admin-report-container {
        padding: 20px;
    }

    .report-reason-tabs {
        margin-bottom: 15px;
    }

    .report-reason-tabs a {
        margin-right: 10px;
        text-decoration: none;
        font-weight: bold;
    }

    .report-table {
        width: 100%;
        border-collapse: collapse;
    }

    .report-table th, .report-table td {
        border: 1px solid #ddd;
        padding: 8px;
    }

    .report-table th {
        background-color: #f5f5f5;
    }

    .status.RECEIVED {
        color: red;
        font-weight: bold;
    }

    .status.IN_PROGRESS {
        color: orange;
    }

    .status.RESOLVED {
        color: green;
    }

    .pagination {
        margin-top: 15px;
        text-align: center;
    }

    .pagination a {
        margin: 0 10px;
    }
</style>