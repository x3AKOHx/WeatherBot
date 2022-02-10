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
            final String getURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + KEY;
            String temp_info;
            String temp_feels;
            String temp_max;
            String temp_min;
            String pressure;
            String wind_speed;

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

                temp_info = ("Температура: " + (int)(obj.getJSONObject("main").getDouble("temp") - 273.15) + "°");
                temp_feels = ("Ощущается как: " + (int)(obj.getJSONObject("main").getDouble("feels_like") - 273.15) + "°");
                temp_max = ("Максимум: " + (int)(obj.getJSONObject("main").getDouble("temp_max") - 273.15) + "°");
                temp_min = ("Минимум: " + (int)(obj.getJSONObject("main").getDouble("temp_min") - 273.15) + "°");
                pressure = ("Давление: " + (int)(obj.getJSONObject("main").getDouble("pressure") - 273.15) + "°");
                wind_speed = ("Скорость ветра: " + (int) obj.getJSONObject("wind").getDouble("speed") + " м/с");

            } catch (IOException e) {
                return "Введен неправильный поисковый запрос :(";
            }
        return temp_info + "\n" + temp_feels + "\n" + temp_max + "\n" + temp_min + "\n" + pressure + "\n" + wind_speed;
        } else {
            return "Введен пустой запрос";
        }
    }
}
