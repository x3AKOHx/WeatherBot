import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.net.URLConnection;

public class WeatherInfo {
    private static final String KEY = "";
    static String temp_info;
    static String temp_feels;
    static String weather;
    static String humidity;
    static String pressure;
    static String wind_speed;
    static String city_info;
    static int ID;


    public static String getCurrentWeather(String city) {
        city = city.toLowerCase();
        if (!Objects.equals(city, "")) {
            final String getURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + KEY + "&lang=ru";

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
                ID = obj.getJSONArray("weather").getJSONObject(0).getInt("id");
                weather = "На улице: " + obj.getJSONArray("weather").getJSONObject(0).getString("description");
                humidity = "Влажность: " + (int) obj.getJSONObject("main").getDouble("humidity") + " %";
                pressure = "Давление: " + (int) (obj.getJSONObject("main").getDouble("pressure") * 0.75) + " мм рт. ст.";
                wind_speed = "Скорость ветра: " + (int) obj.getJSONObject("wind").getDouble("speed") + " м/с";
                city_info = obj.getString("name");
                char[] temp = city_info.toCharArray();
                if (temp[temp.length - 1] == 'а') {
                    temp[temp.length - 1] = 'е';
                    city_info = "Сейчас в " + new String(temp) + ": ";
                } else if (temp[temp.length - 1] == 'ь') {
                    temp[temp.length - 1] = 'и';
                    city_info = "Сейчас в " + new String(temp) + ": ";
                } else if (temp[temp.length - 1] == 'я') {
                    temp[temp.length - 1] = 'и';
                    city_info = "Сейчас в " + new String(temp) + ": ";
                } else if (temp[temp.length - 1] == 'и' || temp[temp.length - 1] == 'е') {
                    city_info = "Сейчас в " + new String(temp) + ": ";
                } else {
                    city_info = "Сейчас в " + new String(temp) + "е: ";
                }
                city_info = "<b>" + city_info + "</b>" + Emoji.getWeatherEmoji(ID) + "\n";
            } catch (IOException e) {
                return "Введен неправильный поисковый запрос :(";
            }
        return city_info + "\n" + temp_info + "\n" + temp_feels + "\n" + weather
                + "\n" + humidity + "\n" + pressure + "\n" + wind_speed;
        } else {
            return "Необходимо задать город";
        }
    }

    public static String getWeatherForWeek(String city, int days) {
        city = city.toLowerCase();
        if (!Objects.equals(city, "")) {
            double lat, lon;
            final String placeURL = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&appid=" + KEY;
            try {
                URL url = new URL(placeURL);
                URLConnection conn = url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    builder.append(inputLine);
                br.close();
                JSONObject obj = new JSONArray(builder.toString()).getJSONObject(0);

                lat = obj.getDouble("lat");
                lon = obj.getDouble("lon");
            } catch (IOException e) {
                return "Введен неправильный поисковый запрос :(";
            }
            final String getURL = "http://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon +
                    "&units=metric&exclude=current,minutely,hourly&appid=" + KEY + "&lang=ru";
            try {
                URL url = new URL(getURL);
                URLConnection conn = url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null)
                    builder.append(inputLine);
                br.close();
                JSONArray arr = new JSONObject(builder.toString()).getJSONArray("daily");
                StringBuilder week = new StringBuilder();
                week.append(getDailyHead(getCityName(lat, lon), days));
                for (int i = 1; i <= days; i++) {
                    JSONObject object = arr.getJSONObject(i);
                    int ID = object.getJSONArray("weather").getJSONObject(0).getInt("id");
                    week.append("\n").append("\n").append(getDate(object.getInt("dt"))).append(Emoji.getWeatherEmoji(ID))
                            .append("\nТемпература днем: ")
                            .append((int) (object.getJSONObject("temp").getDouble("day"))).append("°")
                            .append(" (Ощ-ся как ").append((int) (object.getJSONObject("feels_like").getDouble("day")))
                            .append("°)")
                            .append("\nТемпература ночью: ")
                            .append((int) (object.getJSONObject("temp").getDouble("night"))).append("°")
                            .append(" (Ощ-ся как ").append((int) (object.getJSONObject("feels_like").getDouble("night")))
                            .append("°)")
                            .append("\nНа улице: ")
                            .append(object.getJSONArray("weather").getJSONObject(0).getString("description"))
                            .append("\nВлажность: ")
                            .append((int) object.getDouble("humidity")).append(" %")
                            .append("\nДавление: ")
                            .append((int) (object.getDouble("pressure") * 0.75)).append(" мм рт. ст.")
                            .append("\nСкорость ветра: ")
                            .append((int) object.getDouble("wind_speed")).append(" м/с");
                }
            return week.toString();
            } catch (IOException e) {
                return "Введен неправильный поисковый запрос :(";
            }
        } else {
            return "Необходимо задать город";
        }
    }

    private static String getDate(long unixSeconds) {
        Instant instant = Instant.ofEpochMilli(unixSeconds*1000L);
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(new Locale("ru", "RU"));
        String[] arr = date.format(df).split(" ");
        String result = arr[0] + " " + arr[1] + " " + arr[2];

        return "<b>" + result + ": " + "</b>";
    }

    private static String getDailyHead(String city, int days) {
        String result;
        String str = days == 7 ? "неделю:" : "3 дня:";
        char[] temp = city.toCharArray();
        if (temp[temp.length - 1] == 'а') {
            temp[temp.length - 1] = 'е';
            result = "Погода в " + new String(temp) + " на " + str;
        } else if (temp[temp.length - 1] == 'ь') {
            temp[temp.length - 1] = 'и';
            result = "Погода в " + new String(temp) + " на " + str;
        } else if (temp[temp.length - 1] == 'я') {
            temp[temp.length - 1] = 'и';
            result = "Погода в " + new String(temp) + " на " + str;
        } else if (temp[temp.length - 1] == 'и' || temp[temp.length - 1] == 'е') {
            result = "Погода в " + new String(temp) + " на " + str;
        } else {
            result = "Погода в " + new String(temp) + "е на " + str;
        }
        return result;
    }

    private static String getCityName(double lat, double lon) {
        final String getURL = "http://api.openweathermap.org/geo/1.0/reverse?lat=" + lat + "&lon=" + lon +
                "&appid=" + KEY;
        try {
            URL url = new URL(getURL);
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null)
                builder.append(inputLine);
            br.close();
            JSONObject obj = new JSONArray(builder.toString()).getJSONObject(0);
            return obj.getJSONObject("local_names").getString("ru");

        } catch (IOException e) {
            return "Введен неправильный поисковый запрос :(";
        }
    }
}
