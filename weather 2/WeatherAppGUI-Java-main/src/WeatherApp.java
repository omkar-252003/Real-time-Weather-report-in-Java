import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class WeatherApp {
    private static final String API_KEY = "f46981481de5a6ad41b98270d8d1ec93"; // Replace with your actual API key
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    public static JSONObject getWeatherData(String city) throws Exception {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(content.toString());

        JSONObject main = (JSONObject) jsonResponse.get("main");
        double temperature = (double) main.get("temp");
        String weatherCondition = (String) ((JSONObject) ((org.json.simple.JSONArray) jsonResponse.get("weather")).get(0)).get("description");
        long humidity = (long) main.get("humidity");
        double windspeed = (double) ((JSONObject) jsonResponse.get("wind")).get("speed");

        JSONObject weatherData = new JSONObject();
        weatherData.put("temperature", temperature);
        weatherData.put("weather_condition", weatherCondition);
        weatherData.put("humidity", humidity);
        weatherData.put("windspeed", windspeed);

        return weatherData;
    }
}
