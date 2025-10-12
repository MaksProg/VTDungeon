const canvas = document.getElementById("graph");
const ctx = canvas.getContext("2d");
const size = 500;
canvas.width = size;
canvas.height = size;
const center = size / 2;
const scale = 40;

const MAX_HISTORY = 10;
let R = 2;
let history = [];



// ----------------- Рисовка -----------------
function drawScene(currentPoints = []) {
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

    ctx.beginPath();
    ctx.moveTo(center + i * scale, center - 5);
    ctx.lineTo(center + i * scale, center + 5);
    ctx.stroke();
    ctx.fillText(i, center + i * scale, center + 15);

    ctx.beginPath();
    ctx.moveTo(center - 5, center - i * scale);
    ctx.lineTo(center + 5, center - i * scale);
    ctx.stroke();
    ctx.fillText(i, center - 15, center - i * scale);
  }
  currentPoints.forEach(p => drawPoint(p.x, p.y, "blue"));
}

// ----------------- Рисуем точку -----------------
function drawPoint(x, y, color = "blue") {
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
  if (isNaN(x)) return showError("Выберите X!");
  if (isNaN(y) || y < -5 || y > 5) return showError("Y должен быть числом от -5 до 5.");
  if (checked.length === 0) return showError("Выберите хотя бы одно значение R!");
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
    <td>${row.hit ? "✅" : "❌"}</td>
    <td>${row.time}</td>
    <td>${row.ms_exec}</td>`;
  tbody.prepend(tr);

  while (tbody.rows.length > MAX_HISTORY) {
    tbody.deleteRow(tbody.rows.length - 1);
  }
}

// ----------------- Получение истории с сервера -----------------
async function fetchHistory() {
  try {
    const resp = await fetch("/history");
    if (!resp.ok) throw new Error(`Ошибка ${resp.status}`);
    const data = await resp.json();
    history = Array.isArray(data.result) ? data.result.slice(-MAX_HISTORY) : [];

    const tbody = document.querySelector("#results-table tbody");
    tbody.innerHTML = "";
    history.forEach(addRowToTable);
  } catch (err) {
    showError(err.message);
  }
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

    try {
      const start = performance.now();
      const resp = await fetch(`/api?x=${x}&y=${y}&r=${r}`);
      const end = performance.now();
      if (!resp.ok) throw new Error(`Ошибка ${resp.status}`);
      const data = await resp.json();
      const last = Array.isArray(data.result) ? data.result[data.result.length - 1] : null;
      if (!last) continue;

      last.ms_exec = Math.round(end - start);

      history.push(last);
      if (history.length > MAX_HISTORY) history = history.slice(-MAX_HISTORY);

      const tbody = document.querySelector("#results-table tbody");
      tbody.innerHTML = "";
      history.forEach(addRowToTable);

      drawScene([last]); // рисуем только последнюю точку
      document.getElementById("local-time").textContent = new Date().toLocaleTimeString();
      console.log("Клиентское время:", msExec, "мс, сервер:", data.result.at(-1).ms_exec, "мс");
    } catch (err) {
      showError(err.message);
    }
  }
});

// ----------------- Кнопка сброса -----------------
document.getElementById("reset-btn").addEventListener("click", async () => {
  const tbody = document.querySelector("#results-table tbody");
  tbody.innerHTML = "";
  history = [];
  drawScene();
});

// ----------------- Инициализация -----------------
fetchHistory();
drawScene();

let gamepadIndex = null;
let rIndex = 0;             
let lastL1 = false;          
let lastR1 = false;         
let lastA = false;           
let lastX = false;

window.addEventListener("load", () => {
  const gamepads = navigator.getGamepads();
  for (const gp of gamepads) {
    if (gp) {
      gamepadIndex = gp.index;
      console.log(`Геймпад уже подключен: ${gp.id}`);
    }
  }
});


window.addEventListener("gamepadconnected", (e) =>{
  gamepadIndex = e.gamepad.index;
  console.log(`Геймпад подключен ${e.gamepad.id}`)
})


window.addEventListener("gamepaddisconnected", (e) => {
  console.log(`Геймпад отключен: ${e.gamepad.id}`);
  gamepadIndex = null;
});

let selectedR = []; // массив состояния чекбоксов
let lastXTime = 0;
let lastStickTime = 0;
const stickDelay = 200; // мс между шагами по X
const xStep = 1;        // шаг изменения X
const ySmooth = 0.02;   // плавность изменения Y

const rBoxesInit = document.querySelectorAll("input[name='r']");
selectedR = Array.from(rBoxesInit).map(r => r.checked);

function pollGamepad() {
  if (gamepadIndex === null) {
    requestAnimationFrame(pollGamepad);
    return;
  }

  const gp = navigator.getGamepads()[gamepadIndex];
  if (!gp) {
    requestAnimationFrame(pollGamepad);
    return;
  }

  const now = performance.now();


  const leftX = gp.axes[0];
  const xSelect = document.getElementById("x-select");
  let xValue = Number(xSelect.value || 0);
  if (now - lastStickTime > stickDelay) {
    if (leftX > 0.5) xValue = Math.min(xValue + xStep, 5);
    if (leftX < -0.5) xValue = Math.max(xValue - xStep, -3);
    xSelect.value = xValue;
    lastStickTime = now;
  }

  const rightY = gp.axes[3]; 
  const yInput = document.getElementById("y-input");
  let yValue = Number(yInput.value || 0);
  yValue -= rightY * ySmooth;
  yValue = Math.max(-5, Math.min(5, yValue));
  yInput.value = yValue.toFixed(2);


  const rBoxes = document.querySelectorAll("input[name='r']");
  const l1 = gp.buttons[4].pressed;
  const r1 = gp.buttons[5].pressed;

  if (l1 && !lastL1) rIndex = Math.max(0, rIndex - 1);
  if (r1 && !lastR1) rIndex = Math.min(rBoxes.length - 1, rIndex + 1);


  rBoxes.forEach((r, i) => r.checked = selectedR[i]);

  lastL1 = l1;
  lastR1 = r1;

  const xBtn = gp.buttons[2].pressed;
  if (xBtn && now - lastXTime > 150) { 
    selectedR[rIndex] = !selectedR[rIndex];
    lastXTime = now;
  }

  const aBtn = gp.buttons[0].pressed;
  if (aBtn && !lastA) document.getElementById("submit-btn").click();
  lastA = aBtn;

    const bPressed = gp.buttons[1].pressed;
    if (bPressed && !lastB) document.getElementById("reset-btn").click(); 
    lastB = bPressed;

  requestAnimationFrame(pollGamepad);
}

requestAnimationFrame(pollGamepad);


