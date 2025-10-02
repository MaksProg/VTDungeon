const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");
const size = 500;
canvas.width = size;
canvas.height = size;
const center = size / 2;
const scale = 40;

const MAX_HISTORY = 10;

let R = 2;

// ----------------- Рисовка -----------------
function drawScene() {
  ctx.clearRect(0, 0, size, size);

  // --- Области ---
  ctx.fillStyle = "rgba(0, 128, 255, 0.5)";
  ctx.beginPath();
  ctx.moveTo(center, center);
  ctx.arc(center, center, R * scale, Math.PI, 3 * Math.PI / 2, false);
  ctx.moveTo(center, center);
  ctx.rect(center, center, R * scale, R * scale);
  ctx.moveTo(center, center);
  ctx.lineTo(center + R * scale, center);
  ctx.lineTo(center, center - (R * scale) / 2);
  ctx.closePath();
  ctx.fill();

  // --- Оси ---
  ctx.strokeStyle = "black";
  ctx.lineWidth = 2;
  ctx.beginPath();

  // OX
  ctx.moveTo(0, center);
  ctx.lineTo(size, center);
  ctx.moveTo(size - 10, center - 5);
  ctx.lineTo(size, center);
  ctx.lineTo(size - 10, center + 5);

  // OY
  ctx.moveTo(center, size);
  ctx.lineTo(center, 0);
  ctx.moveTo(center - 5, 10);
  ctx.lineTo(center, 0);
  ctx.lineTo(center + 5, 10);

  ctx.stroke();

  // --- Деления ---
  ctx.fillStyle = "black";
  ctx.font = "12px Arial";
  ctx.textAlign = "center";
  ctx.textBaseline = "middle";

  for (let i = -5; i <= 5; i++) {
    if (i === 0) continue;

    // X деления
    ctx.beginPath();
    ctx.moveTo(center + i * scale, center - 5);
    ctx.lineTo(center + i * scale, center + 5);
    ctx.stroke();
    ctx.fillText(i, center + i * scale, center + 15);

    // Y деления
    ctx.beginPath();
    ctx.moveTo(center - 5, center - i * scale);
    ctx.lineTo(center + 5, center - i * scale);
    ctx.stroke();
    ctx.fillText(i, center - 15, center - i * scale);
  }
}

// ----------------- Рисуем точки -----------------
function drawPoint(x, y, color = "red") {
  ctx.beginPath();
  ctx.arc(center + x * scale, center - y * scale, 4, 0, Math.PI * 2);
  ctx.fillStyle = color;
  ctx.fill();
}

// ----------------- Валидация -----------------
function showError(msg) {
  document.getElementById("last-error").textContent = msg || "нет";
}

function validate(x, y, checked) {
  if (isNaN(x)) {
    showError("Выберите X!");
    return false;
  }
  if (isNaN(y) || y < -5 || y > 5) {
    showError("Y должен быть числом от -5 до 5.");
    return false;
  }
  if (checked.length === 0) {
    showError("Выберите хотя бы одно значение R!");
    return false;
  }
  showError(null);
  return true;
}

// ----------------- Работа с cookies -----------------
function saveTableToCookies() {
  const rows = [];
  document.querySelectorAll("#results-table tbody tr").forEach(tr => {
    const cells = tr.querySelectorAll("td");
    rows.push({
      x: cells[0].textContent,
      y: cells[1].textContent,
      r: cells[2].textContent,
      hit: cells[3].textContent,
      time: cells[4].textContent,
      ms_exec: cells[5].textContent
    });
  });
  document.cookie = `table=${encodeURIComponent(JSON.stringify(rows))}; path=/; max-age=86400`;
}

function loadTableFromCookies() {
  const cookie = document.cookie.split("; ").find(row => row.startsWith("table="));
  if (!cookie) return;
  const data = JSON.parse(decodeURIComponent(cookie.split("=")[1]));
  data.forEach(row => addRowToTable(row, false));
}

function clearCookies() {
  document.cookie = "table=; path=/; max-age=0";
}

// ----------------- Добавление новой строки в таблицу -----------------
function addRowToTable(row, save = true) {
  const tbody = document.querySelector("#results-table tbody");
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td>${row.x}</td>
    <td>${row.y}</td>
    <td>${row.r}</td>
    <td>${row.hit ? "✅" : "❌"}</td>
    <td>${row.time}</td>
    <td>${row.ms_exec}</td>`;

  tbody.prepend(tr);

  while (tbody.rows.length > MAX_HISTORY) {
    tbody.deleteRow(tbody.rows.length - 1);
  }

  if (save) saveTableToCookies();
}

// ----------------- Сабмит формы -----------------
document.getElementById("check-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const x = parseFloat(document.getElementById("x-select").value);
  const y = parseFloat(document.getElementById("y-input").value.replace(",", "."));
  const checked = document.querySelectorAll("input[name='r']:checked");

  if (!validate(x, y, checked)) return;

  for (const box of checked) {
    const r = parseFloat(box.value);
    R = r;

    drawScene();
    drawPoint(x, y, "blue");

    try {
      const start = performance.now();
      const resp = await fetch(`/api?x=${x}&y=${y}&r=${r}`);
      const end = performance.now();
      const msExec = Math.round(end - start);

      if (!resp.ok) throw new Error(`Ошибка ${resp.status}`);
      const data = await resp.json();

      const last = data.result[data.result.length - 1];
      last.ms_exec = msExec;

      addRowToTable(last);
      document.getElementById("local-time").textContent = new Date().toLocaleTimeString();
    } catch (err) {
      showError(err.message);
    }
  }
});

// ----------------- Кнопка сброса -----------------
document.getElementById("reset-btn").addEventListener("click", () => {
  const tbody = document.querySelector("#results-table tbody");
  tbody.innerHTML = "";
  clearCookies();
});

// ----------------- Первый вызов -----------------
drawScene();
loadTableFromCookies();
