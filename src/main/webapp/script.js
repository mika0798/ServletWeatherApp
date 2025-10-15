document.getElementById('search-btn').addEventListener('click', getWeather);
document.getElementById('city-input').addEventListener('keypress', e => {
    if (e.key === 'Enter') getWeather();
});

async function getWeather() {
    const city = document.getElementById('city-input').value.trim();
    if (!city) return alert('Please enter a city name.');

    const response = await fetch(`weather?city=${encodeURIComponent(city)}`);
    if (!response.ok) {
        alert('City not found!');
        return;
    }

    const data = await response.json();
    displayWeather(data);
}

function displayWeather(data) {
    document.getElementById('weather-card').style.display = 'block';
    document.getElementById('city-name').textContent = data.city;
    document.getElementById('flag').src = data.flagUrl;
    document.getElementById('temperature').textContent = Math.round(data.temp);
    document.getElementById('description').textContent = data.description;
    document.getElementById('clouds').textContent = `${data.clouds}%`;
    document.getElementById('humidity').textContent = `${data.humidity}%`;
    document.getElementById('pressure').textContent = `${data.pressure}hPa`;
    document.getElementById('weather-icon').src = getWeatherIcon(data.main);
}

function getWeatherIcon(main) {
    const map = {
        Clouds: 'https://cdn-icons-png.flaticon.com/512/1163/1163624.png',
        Clear: 'https://cdn-icons-png.flaticon.com/512/6974/6974833.png',
        Rain: 'https://cdn-icons-png.flaticon.com/512/1163/1163657.png',
        Snow: 'https://cdn-icons-png.flaticon.com/512/2315/2315309.png',
        Drizzle: 'https://cdn-icons-png.flaticon.com/512/3076/3076129.png',
        Thunderstorm: 'https://cdn-icons-png.flaticon.com/512/1146/1146860.png',
        Mist: 'https://cdn-icons-png.flaticon.com/512/4005/4005901.png'
    };
    return map[main] || map['Clouds'];
}