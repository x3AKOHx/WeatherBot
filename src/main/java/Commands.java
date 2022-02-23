import org.telegram.telegrambots.bots.DefaultBotOptions;

public class Commands {
    public void setMessage(String command, String chatId) {
        User user = Bot.users.get(chatId);
        switch (command) {
            case "get_current_weather" -> user.sendMessage(WeatherInfo.getCurrentWeather(user.getCity()));
            case "get_forecast" -> user.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 7));
            case "get_forecast_3" -> user.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 3));
            case "set_city" -> user.askForCity();
            case "set_timer" -> user.askForTimer();
            case "run_weather_receive_timer" -> user.startSending();
            case "stop_weather_receive_timer" -> user.stopSending();
            case "start" -> {
                Menu menu = new Menu(new DefaultBotOptions());
                menu.getMenu(chatId);
            }
            default -> user.sendMessage("Команда не найдена");
        }
    }

    public void nonCommand(String command, String chatId) {
        User user = Bot.users.get(chatId);
        if (command.contains("Погода сейчас"))
            user.sendMessage(WeatherInfo.getCurrentWeather(user.getCity()));
        else if (command.contains("Прогноз на 3 дня"))
            user.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 3));
        else if (command.contains("Прогноз на неделю"))
            user.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 7));
        else if (command.contains("Установить город по умолчанию"))
            user.askForCity();
        else if (command.contains("Настроить таймер получения уведомлений"))
            user.askForTimer();
        else if (command.contains("Включить уведомления"))
            user.startSending();
        else if (command.contains("Выключить уведомления"))
            user.stopSending();
        else if (command.equals("Zz123456"))
            user.sendMessage("Количество пользователей бота: " + Bot.users.size());
        else user.sendMessage(WeatherInfo.getCurrentWeather(command));
    }
}

