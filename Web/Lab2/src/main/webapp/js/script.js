const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");
const size = 500;
canvas.width = size;
canvas.height = size;
const center = size / 2;
const scale = 40;

// История результатов (не динамическая, просто для таблицы)
const MAX_HISTORY = 10;

// ----------------- Получаем текущие выбранные R -----------------
function getSelectedRs() {
    const rBoxes = document.querySelectorAll("input[name='r']:checked");
    return Array.from(rBoxes).map(rb => parseFloat(rb.value));
}

// ----------------- Рисуем фигуры -----------------
function drawScene(currentPoints = []) {
    ctx.clearRect(0, 0, size, size);

    // --- Рисуем фигуры для каждого выбранного R ---
    const rValues = getSelectedRs().sort((a, b) => a - b); // от меньшего к большему
    rValues.forEach(R => {
        ctx.fillStyle = "rgba(0, 128, 255, 0.3)";

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
    });

    // --- Оси ---
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(0, center); ctx.lineTo(size, center); // OX
    ctx.moveTo(center, size); ctx.lineTo(center, 0); // OY
    ctx.stroke();

    // --- Деления ---
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

    // --- Рисуем текущие точки ---
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
    if (!rValues || rValues.length === 0) return showError("Выберите хотя бы один R!");
    showError(null);
    return true;
}

// ----------------- Сабмит формы -----------------
document.getElementById("check-form").addEventListener("submit", e => {
    e.preventDefault();
    const xRadio = document.querySelector("input[name='x']:checked");
    const x = xRadio ? parseFloat(xRadio.value) : NaN;
    const y = parseFloat(document.getElementById("y-input").value.replace(",", "."));
    const rValues = getSelectedRs();

    if (!validate(x, y, rValues)) return;

    rValues.forEach(r => {
        const url = `controller?x=${x}&y=${y}&r=${r}`;
        window.open(url, "_blank");
    });
});

// ----------------- Клик по графику -----------------
canvas.addEventListener("click", e => {
    const rect = canvas.getBoundingClientRect();
    const xClick = ((e.clientX - rect.left) - center) / scale;
    const yClick = (center - (e.clientY - rect.top)) / scale;
    const rValues = getSelectedRs();

    if (rValues.length === 0) return showError("Выберите хотя бы один R перед кликом на графике!");

    if (!validate(xClick, yClick, rValues)) return;

    rValues.forEach(r => {
        const url = `controller?x=${xClick.toFixed(2)}&y=${yClick.toFixed(2)}&r=${r}`;
        window.open(url, "_blank");
    });
});

// ----------------- Смена R -----------------
document.querySelectorAll("input[name='r']").forEach(rb => {
    rb.addEventListener("change", () => drawScene());
});

// ----------------- Инициализация -----------------
drawScene();
