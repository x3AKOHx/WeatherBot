import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import org.json.JSONObject;
import java.net.URL;
import java.net.URLConnection;

public class WeatherInfo {
    private static final String KEY = "4e31d834f817a57f1cb6980cf70ce319";

    public static String getCurrentWeather(String city) {
        city = city.toLowerCase();
        if (!Objects.equals(city, "")) {
            final String getURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + KEY + "&lang=ru";
            String temp_info;
            String temp_feels;
            String weather;
            String humidity;
            String pressure;
            String wind_speed;
            String city_info;

            try {
                URL url = new URL(getURL);
                URLConnection conn = url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    builder.append(inputLine);
                br.close();

                JSONObject obj = new JSONObject(builder.toString());

                temp_info = "Температура: " + (int) (obj.getJSONObject("main").getDouble("temp") - 273.15) + "°";
                temp_feels = "Ощущается как: " + (int) (obj.getJSONObject("main").getDouble("feels_like") - 273.15) + "°";
                weather = "На улице: " + obj.getJSONArray("weather").getJSONObject(0).getString("description");
                humidity = "Влажность: " + (int) obj.getJSONObject("main").getDouble("humidity") + " %";
                pressure = "Давление: " + (int) (obj.getJSONObject("main").getDouble("pressure") * 0.75) + " мм рт. ст.";
                wind_speed = "Скорость ветра: " + (int) obj.getJSONObject("wind").getDouble("speed") + " м/с";
                city_info = obj.getString("name");
                char[] temp = city_info.toCharArray();
                if (temp[temp.length - 1] == 'а') {
                    temp[temp.length - 1] = 'е';
                    city_info = "Сейчас в " + new String(temp) + ":\n";
                } else if (temp[temp.length - 1] == 'ь'){
                    temp[temp.length - 1] = 'и';
                    city_info = "Сейчас в " + new String(temp) + ":\n";
                } else {
                    city_info = "Сейчас в " + new String(temp) + "е" + ":\n";
                }

            } catch (IOException e) {
                return "Введен неправильный поисковый запрос :(";
            }
        return city_info + "\n" + temp_info + "\n" + temp_feels + "\n" + weather + "\n" + humidity + "\n" + pressure + "\n" + wind_speed;
        } else {
            return "Необходимо задать город";
        }
    }
}
