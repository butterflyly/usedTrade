<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="container">
    <h2>회원가입 통계</h2>
    <canvas id="myChart"></canvas>
</div>

<script>
    const ctx = document.getElementById('myChart').getContext('2d');

    // JSP에서 배열 형태로 JS에 전달
    const labels = [
        <c:forEach var="label" items="${chartData.labels}" varStatus="status">
            "${label}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const data = [
        <c:forEach var="value" items="${chartData.values}" varStatus="status">
            ${value}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    new Chart(ctx, {
        type: 'line', // bar 로 바꿔도 됨
        data: {
            labels: labels,
            datasets: [{
                label: '회원가입 수',
                data: data,
                borderWidth: 2,
                tension: 0.3 // 부드러운 곡선
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        maxTicksLimit: 5
                    }
                }
            }
        }
    });
</script>

