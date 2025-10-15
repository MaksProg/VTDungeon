const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");
const size = 500;
canvas.width = size;
canvas.height = size;
const center = size / 2;
const scale = 40;

const MAX_HISTORY = 10;
let R = 2;  // текущий радиус для графика
let history = [];

// ----------------- Рисуем фигуры -----------------
function drawScene(currentPoints = []) {
    ctx.clearRect(0, 0, size, size);

    ctx.fillStyle = "rgba(0, 128, 255, 0.5)";

    // Первая четверть: круг R/2
    ctx.beginPath();
    ctx.moveTo(center, center);
    ctx.arc(center, center, R * scale / 2, 1.5 * Math.PI, 2 * Math.PI, false);
    ctx.lineTo(center, center);
    ctx.fill();

    // Вторая четверть: квадрат R
    ctx.beginPath();
    ctx.rect(center - R * scale, center - R * scale, R * scale, R * scale);
    ctx.fill();

    // Четвёртая четверть: треугольник с катетами R
    ctx.beginPath();
    ctx.moveTo(center, center);
    ctx.lineTo(center + R * scale, center);
    ctx.lineTo(center, center + R * scale);
    ctx.closePath();
    ctx.fill();

    // Оси
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(0, center); ctx.lineTo(size, center); // OX
    ctx.moveTo(center, size); ctx.lineTo(center, 0); // OY
    ctx.stroke();

    // Деления
    ctx.fillStyle = "black";
    ctx.font = "12px Arial";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";
    for (let i = -5; i <= 5; i++) {
        if (i === 0) continue;
        // OX
        ctx.beginPath();
        ctx.moveTo(center + i * scale, center - 5);
        ctx.lineTo(center + i * scale, center + 5);
        ctx.stroke();
        ctx.fillText(i, center + i * scale, center + 15);
        // OY
        ctx.beginPath();
        ctx.moveTo(center - 5, center - i * scale);
        ctx.lineTo(center + 5, center - i * scale);
        ctx.stroke();
        ctx.fillText(i, center - 15, center - i * scale);
    }

    // Рисуем текущие точки
    currentPoints.forEach(p => drawPoint(p.x, p.y, "blue"));
}

// ----------------- Рисуем точку -----------------
function drawPoint(x, y, color = "blue") {
    ctx.beginPath();
    ctx.arc(center + x * scale, center - y * scale, 4, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
}

// ----------------- Валидация -----------------
function showError(msg) {
    document.getElementById("last-error").textContent = msg || "нет";
}

function validate(x, y, rValues) {
    if (isNaN(x)) return showError("Выберите X!");
    if (isNaN(y) || y < -5 || y > 5) return showError("Y должен быть числом от -5 до 5.");
    if (!rValues.length) return showError("Выберите хотя бы один R!");
    showError(null);
    return true;
}

// ----------------- Добавление строки в таблицу -----------------
function addRowToTable(row) {
    const tbody = document.querySelector("#results-table tbody");
    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td>${row.x}</td>
        <td>${row.y}</td>
        <td>${row.r}</td>
        <td>${row.hit ? "Да" : "Нет"}</td>
        <td>${row.time}</td>
        <td>${row.ms_exec}</td>
    `;
    tbody.prepend(tr);

    while (tbody.rows.length > MAX_HISTORY) {
        tbody.deleteRow(tbody.rows.length - 1);
    }
}

// ----------------- Сабмит формы -----------------
document.getElementById("check-form").addEventListener("submit", async e => {
    e.preventDefault();

    const xRadio = document.querySelector("input[name='x']:checked");
    if (!xRadio) return showError("Выберите X!");
    const x = parseFloat(xRadio.value);

    const yInput = document.getElementById("y-input");
    const y = parseFloat(yInput.value.replace(",", "."));

    const rBoxes = document.querySelectorAll("input[name='r']:checked");
    const rValues = Array.from(rBoxes).map(b => parseFloat(b.value));
    if (!validate(x, y, rValues)) return;

    try {
        for (const r of rValues) {
            R = r;
            const start = performance.now();
            const params = new URLSearchParams({ x, y, r });
            const resp = await fetch(`controller?${params.toString()}`, {
                headers: { 'X-Requested-With': 'XMLHttpRequest' }
            });
            const end = performance.now();
            if (!resp.ok) throw new Error(`Ошибка ${resp.status}`);
            const data = await resp.json();
            const last = data.result[0];
            last.ms_exec = Math.round(end - start);
            last.time = new Date().toLocaleTimeString();
            history.push(last);
            addRowToTable(last);
            drawScene([last]);
            document.getElementById("local-time").textContent = last.time;
        }
    } catch (err) {
        showError(err.message);
    }
});

// ----------------- Клик по графику -----------------
canvas.addEventListener("click", e => {
    const rect = canvas.getBoundingClientRect();
    const xClick = (e.clientX - rect.left - center) / scale;
    const yClick = (center - (e.clientY - rect.top)) / scale;

    const rBoxes = document.querySelectorAll("input[name='r']:checked");
    if (!rBoxes.length) return showError("Выберите хотя бы один R перед кликом на графике!");
    const r = parseFloat(rBoxes[0].value); // используем первый выбранный R
    R = r;

    submitPoint(xClick.toFixed(2), yClick.toFixed(2), r);
});

// ----------------- Кнопка сброса -----------------
document.getElementById("reset-btn").addEventListener("click", () => {
    const tbody = document.querySelector("#results-table tbody");
    tbody.innerHTML = "";
    history = [];
    drawScene();
});

// ----------------- Инициализация -----------------
drawScene();
