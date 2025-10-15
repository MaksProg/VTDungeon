<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ru">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>ЛР — попадание точки в область</title>
  <link rel="stylesheet" href="style.css" />
  <script defer src="js/script.js"></script>
</head>
<body>
  <!-- Табличная шапка -->
  <table class="header" role="presentation">
    <tr>
      <td>
        <div id="student-name">Родионов Максим Артемович</div>
        <div id="student-meta">Группа: P3231 • Вариант: 467244</div>
      </td>
    </tr>
  </table>

  <!-- Основная область -->
  <table class="layout" role="presentation">
    <tr>
      <!-- Левая колонка: форма + график -->
      <td class="left-col">
        <form id="check-form" action="controller" method="get" novalidate>
          <table class="form-table">
            <caption>Параметры</caption>

            <tr>
              <td><label for="x-select">X:</label></td>
              <td>
                <select id="x-select" name="x" required>
                  <option value="">-- выберите X --</option>
                  <c:forEach var="xVal" items="${xValues}">
                    <option value="${xVal}">${xVal}</option>
                  </c:forEach>
                </select>
              </td>
            </tr>

            <tr>
              <td><label for="y-input">Y:</label></td>
              <td>
                <input id="y-input" name="y" type="text" placeholder="-5..5" required />
                <div class="hint">Число с точкой или запятой, в диапазоне −5…5</div>
              </td>
            </tr>

            <tr>
              <td><span>R:</span></td>
              <td>
                <div class="r-checkboxes">
                  <c:forEach var="rVal" items="${rValues}">
                    <label><input type="checkbox" name="r" value="${rVal}">${rVal}</label>
                  </c:forEach>
                </div>
              </td>
            </tr>

            <tr>
              <td></td>
              <td>
                <button id="submit-btn" type="submit">Проверить</button>
              </td>
            </tr>
          </table>
        </form>

        <!-- График -->
        <div class="canvas-wrapper">
          <canvas id="graph" width="400" height="400"></canvas>
        </div>
      </td>

      <!-- Правая колонка: блок статуса сверху + история снизу -->
      <td class="right-col">
        <!-- Блок статуса -->
        <table class="meta" role="presentation">
          <caption>Статус</caption>
          <tr>
            <td>Локальное время:</td>
            <td id="local-time">—</td>
          </tr>
          <tr>
            <td>Последняя ошибка:</td>
            <td id="last-error">${errorMessage != null ? errorMessage : 'нет'}</td>
          </tr>
        </table>

        <!-- Таблица истории -->
        <table class="results" id="results-table">
          <caption>История проверок</caption>
          <thead>
            <tr>
              <th>X</th>
              <th>Y</th>
              <th>R</th>
              <th>Попадание</th>
              <th>Время сервера</th>
              <th>Время выполнения (мс)</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="res" items="${results}">
              <tr>
                <td>${res.x}</td>
                <td>${res.y}</td>
                <td>${res.r}</td>
                <td>${res.hit ? 'Да' : 'Нет'}</td>
                <td>${res.serverTime}</td>
                <td>${res.execTime}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

        <!-- Кнопка сброса истории -->
        <div class="reset-wrapper">
          <button id="reset-btn" type="button">Сбросить историю</button>
        </div>
      </td>
    </tr>
  </table>
</body>
</html>
