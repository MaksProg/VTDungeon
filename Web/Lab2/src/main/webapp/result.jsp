<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Результаты проверки</title>
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<h2>Результаты проверки</h2>

<c:if test="${not empty results}">
    <table border="1">
        <thead>
        <tr>
            <th>X</th>
            <th>Y</th>
            <th>R</th>
            <th>Попадание</th>
            <th>Время сервера (мс)</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="res" items="${results}">
            <tr>
                <td>${res.point.x}</td>
                <td>${res.point.y}</td>
                <td>${res.point.r}</td>
                <td>${res.hit ? "Да" : "Нет"}</td>
                <td>${res.execTime}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<a href="controller">Назад к форме</a>
</body>
</html>
