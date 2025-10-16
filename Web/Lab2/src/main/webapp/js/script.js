const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");
const size = 500;
canvas.width = size;
canvas.height = size;
const center = size / 2;
const scale = 40;

// ----------------- Получаем текущие выбранные R -----------------
function getSelectedRs() {
    return Array.from(document.querySelectorAll("input[name='r']:checked"))
        .map(rb => parseFloat(rb.value));
}

// ----------------- Рисуем фигуры -----------------
function drawScene() {
    ctx.clearRect(0, 0, size, size);

    const rValues = getSelectedRs().sort((a, b) => a - b);
    rValues.forEach(R => {
        ctx.fillStyle = "rgba(0, 128, 255, 0.3)";

        // Круг (первая четверть, x>=0, y>=0)
        ctx.beginPath();
        ctx.moveTo(center, center);
        ctx.arc(center, center, R * scale / 2, 1.5 * Math.PI, 2 * Math.PI, false);
        ctx.lineTo(center, center);
        ctx.fill();

        // Квадрат (вторая четверть, x<=0, y>=0)
        ctx.beginPath();
        ctx.rect(center - R * scale, center - R * scale, R * scale, R * scale);
        ctx.fill();

        // Треугольник (четвёртая четверть, x>=0, y<=0)
        ctx.beginPath();
        ctx.moveTo(center, center);
        ctx.lineTo(center + R * scale, center);
        ctx.lineTo(center, center + R * scale);
        ctx.closePath();
        ctx.fill();
    });

    drawAxes();
}

// ----------------- Оси -----------------
function drawAxes() {
    ctx.strokeStyle = "black";
    ctx.lineWidth = 2;

    // OX
    ctx.beginPath();
    ctx.moveTo(0, center);
    ctx.lineTo(size, center);
    ctx.stroke();

    // OY
    ctx.beginPath();
    ctx.moveTo(center, 0);
    ctx.lineTo(center, size);
    ctx.stroke();

    ctx.fillStyle = "black";
    ctx.font = "12px Arial";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";
    for (let i = -5; i <= 5; i++) {
        if (i === 0) continue;

        // OX деления
        ctx.beginPath();
        ctx.moveTo(center + i * scale, center - 5);
        ctx.lineTo(center + i * scale, center + 5);
        ctx.stroke();
        ctx.fillText(i, center + i * scale, center + 15);

        // OY деления
        ctx.beginPath();
        ctx.moveTo(center - 5, center - i * scale);
        ctx.lineTo(center + 5, center - i * scale);
        ctx.stroke();
        ctx.fillText(i, center - 15, center - i * scale);
    }
}

// ----------------- Клик по графику -----------------
canvas.addEventListener("click", e => {
    const rect = canvas.getBoundingClientRect();
    const xClick = ((e.clientX - rect.left) - center) / scale;
    const yClick = (center - (e.clientY - rect.top)) / scale;
    const rValues = getSelectedRs();

    if (!rValues.length) {
        alert("Выберите хотя бы один R!");
        return;
    }

    const params = new URLSearchParams();
    params.append("x", xClick.toFixed(2));
    params.append("y", yClick.toFixed(2));
    rValues.forEach(r => params.append("r", r));

    window.location.href = `controller?${params.toString()}`;
});

// ----------------- Сабмит формы -----------------
document.getElementById("check-form").addEventListener("submit", e => {
    e.preventDefault();
    const xRadio = document.querySelector("input[name='x']:checked");
    const x = xRadio ? parseFloat(xRadio.value) : NaN;
    const y = parseFloat(document.getElementById("y-input").value.replace(",", "."));
    const rValues = getSelectedRs();

    if (isNaN(x) || isNaN(y) || !rValues.length || x>3 || x <-5||y<-5||y>5) {
        alert("Введите корректные значения X, Y и выберите хотя бы один R!");
        return;
    }

    const params = new URLSearchParams();
    params.append("x", x);
    params.append("y", y);
    rValues.forEach(r => params.append("r", r));

    window.location.href = `controller?${params.toString()}`;
});

// ----------------- Перерисовка графика при выборе R -----------------
document.querySelectorAll("input[name='r']").forEach(rb => rb.addEventListener("change", drawScene));

// Инициализация
drawScene();
