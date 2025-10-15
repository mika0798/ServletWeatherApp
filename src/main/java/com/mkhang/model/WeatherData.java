package com.mkhang.model;

public class WeatherData {
    private String city;
    private String country;
    private String flagUrl;
    private double temp;
    private String description;
    private String main;
    private int clouds;
    private int humidity;
    private int pressure;
    private String localTime;

    public WeatherData() {}

    public WeatherData(String city, String country, String flagUrl, double temp, String description,
                       String main, int clouds, int humidity, int pressure, String localTime) {
        this.city = city;
        this.country = country;
        this.flagUrl = flagUrl;
        this.temp = temp;
        this.description = description;
        this.main = main;
        this.clouds = clouds;
        this.humidity = humidity;
        this.pressure = pressure;
        this.localTime = localTime;
    }

    // --- Getter & Setter ---
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getFlagUrl() { return flagUrl; }
    public void setFlagUrl(String flagUrl) { this.flagUrl = flagUrl; }

    public double getTemp() { return temp; }
    public void setTemp(double temp) { this.temp = temp; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMain() { return main; }
    public void setMain(String main) { this.main = main; }

    public int getClouds() { return clouds; }
    public void setClouds(int clouds) { this.clouds = clouds; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public int getPressure() { return pressure; }
    public void setPressure(int pressure) { this.pressure = pressure; }

    public String getLocalTime() { return localTime; }
    public void setLocalTime(String localTime) { this.localTime = localTime; }
}
