const REFRESH_INTERVAL_MS = 60000;
const API_URL = '/api/measurements/today';
const TIME_ZONE = 'Europe/Moscow';

const timeFormatter = new Intl.DateTimeFormat('ru-RU', {
    hour: '2-digit',
    minute: '2-digit',
    timeZone: TIME_ZONE
});

const dateTimeFormatter = new Intl.DateTimeFormat('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
    timeZone: TIME_ZONE
});

const numberFormatter = new Intl.NumberFormat('ru-RU', {
    minimumFractionDigits: 1,
    maximumFractionDigits: 1
});

const baseOptions = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: { mode: 'index', intersect: false },
    plugins: {
        legend: { labels: { color: '#ffffff' } },
        tooltip: {
            callbacks: {
                label: (context) => `${context.dataset.label}: ${numberFormatter.format(context.parsed.y)}`
            }
        }
    }
};

const xScale = {
    ticks: { color: '#ffffff', autoSkip: true, maxTicksLimit: 12, maxRotation: 0 },
    grid: { color: 'rgba(255, 255, 255, 0.15)' }
};

function createChart(canvasId, label, color, background, yScale) {
    return new Chart(document.getElementById(canvasId), {
        type: 'line',
        data: {
            labels: [],
            datasets: [
                {
                    label: label,
                    data: [],
                    borderColor: color,
                    backgroundColor: background,
                    fill: true,
                    tension: 0.2,
                    pointRadius: 2
                }
            ]
        },
        options: {
            ...baseOptions,
            scales: {
                x: xScale,
                y: yScale
            }
        }
    });
}

const temperatureChart = createChart(
    'chart-temperature',
    'Температура, °C',
    '#ffd166',
    'rgba(255, 209, 102, 0.2)',
    {
        title: { display: true, text: 'Температура, °C', color: '#ffffff' },
        ticks: { color: '#ffffff' },
        grid: { color: 'rgba(255, 255, 255, 0.15)' }
    }
);

const humidityChart = createChart(
    'chart-humidity',
    'Влажность, %',
    '#8fd3ff',
    'rgba(143, 211, 255, 0.2)',
    {
        min: 0,
        max: 100,
        title: { display: true, text: 'Влажность, %', color: '#ffffff' },
        ticks: { color: '#ffffff' },
        grid: { color: 'rgba(255, 255, 255, 0.15)' }
    }
);

function formatDateTime(value) {
    const parts = dateTimeFormatter.formatToParts(new Date(value));
    const part = (type) => parts.find((item) => item.type === type).value;
    return `${part('day')}.${part('month')}.${part('year')} ${part('hour')}:${part('minute')}:${part('second')}`;
}

function renderCurrent(rows) {
    const block = document.getElementById('current-block');
    const empty = document.getElementById('no-data');
    const last = rows.length > 0 ? rows[rows.length - 1] : null;

    block.hidden = last === null;
    empty.hidden = last !== null;

    if (last === null) {
        return;
    }

    document.getElementById('current-temperature').textContent = `${numberFormatter.format(last.temperature)} °C`;
    document.getElementById('current-humidity').textContent = `${numberFormatter.format(last.humidity)} %`;
    document.getElementById('current-device').textContent = last.deviceId;
    document.getElementById('current-time').textContent = formatDateTime(last.receivedAt);
}

function createCell(text) {
    const cell = document.createElement('td');
    cell.textContent = text;
    return cell;
}

function renderTable(rows) {
    const table = document.getElementById('measurements-table');
    const body = document.getElementById('measurements-body');
    const empty = document.getElementById('measurements-empty');

    body.replaceChildren();

    if (rows.length === 0) {
        table.hidden = true;
        empty.hidden = false;
        return;
    }

    table.hidden = false;
    empty.hidden = true;

    rows.slice(-50).reverse().forEach((row) => {
        const tr = document.createElement('tr');
        tr.append(
            createCell(formatDateTime(row.receivedAt)),
            createCell(row.deviceId),
            createCell(numberFormatter.format(row.temperature)),
            createCell(numberFormatter.format(row.humidity))
        );
        body.append(tr);
    });
}

function renderCharts(rows) {
    const labels = rows.map((row) => timeFormatter.format(new Date(row.receivedAt)));

    temperatureChart.data.labels = labels;
    temperatureChart.data.datasets[0].data = rows.map((row) => row.temperature);
    temperatureChart.update();

    humidityChart.data.labels = labels;
    humidityChart.data.datasets[0].data = rows.map((row) => row.humidity);
    humidityChart.update();

    document.getElementById('chart-empty').hidden = rows.length > 0;
}

async function refresh() {
    try {
        const response = await fetch(API_URL, { headers: { Accept: 'application/json' } });
        if (!response.ok) {
            return;
        }
        const rows = await response.json();
        renderCurrent(rows);
        renderTable(rows);
        renderCharts(rows);
    } catch (error) {
        console.error('Не удалось обновить данные', error);
    }
}

refresh();
setInterval(refresh, REFRESH_INTERVAL_MS);
