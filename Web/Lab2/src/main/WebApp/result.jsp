<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Результат</title></head>
<body>
  <h2>Результат проверки</h2>
  <p>X = ${result.point.x}</p>
  <p>Y = ${result.point.y}</p>
  <p>R = ${result.point.r}</p>
  <p>Попадание: ${result.hit ? "Да" : "Нет"}</p>
  <a href="controller">Назад</a>
</body>
</html>