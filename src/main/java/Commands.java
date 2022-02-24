import org.telegram.telegrambots.bots.DefaultBotOptions;

public class Commands {
    public void setMessage(String command, String chatId) {
        User user = Bot.users.get(chatId);
        MessageSender sender = new MessageSender(chatId);
        switch (command) {
            case "/get_current_weather" -> sender.sendMessage(WeatherInfo.getCurrentWeather(user.getCity()));
            case "/get_forecast" -> sender.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 7));
            case "/get_forecast_3" -> sender.sendMessage(WeatherInfo.getWeatherForWeek(user.getCity(), 3));
            case "/set_city" -> user.askForCity();
            case "/set_timer" -> user.askForTimer();
            case "/run_weather_receive_timer" -> user.startSending();
            case "/stop_weather_receive_timer" -> user.stopSending();
            case "/start" -> {
                Menu menu = new Menu(new DefaultBotOptions());
                menu.getMenu(chatId);
            }
            default -> sender.sendMessage("Команда не найдена");
        }
    }

    public void nonCommand(String command, String chatId) {
        User user = Bot.users.get(chatId);
        MessageSender sender = new MessageSender(chatId);
        if (command.contains("Погода сейчас")) {
            String str = WeatherInfo.getCurrentWeather(user.getCity());
            if ("Необходимо настроить город по умолчанию".equals(str))
                sender.sendMessage("Необходимо настроить город по умолчанию",
                    Menu.getButton("Настроить город", "/set_city"));
            else sender.sendMessage(str);
        }
        else if (command.contains("Прогноз на 3 дня")) {
            String str = WeatherInfo.getWeatherForWeek(user.getCity(), 3);
            if ("Необходимо настроить город по умолчанию".equals(str))
                sender.sendMessage("Необходимо настроить город по умолчанию",
                        Menu.getButton("Настроить город", "/set_city"));
            else sender.sendMessage(str);
        }
        else if (command.contains("Прогноз на неделю")) {
            String str = WeatherInfo.getWeatherForWeek(user.getCity(), 7);
            if ("Необходимо настроить город по умолчанию".equals(str))
                sender.sendMessage("Необходимо настроить город по умолчанию",
                        Menu.getButton("Настроить город", "/set_city"));
            else sender.sendMessage(str);
        }
        else if (command.contains("Установить город по умолчанию"))
            user.askForCity();
        else if (command.contains("Настроить таймер получения уведомлений"))
            user.askForTimer();
        else if (command.contains("Включить уведомления"))
            user.startSending();
        else if (command.contains("Выключить уведомления"))
            user.stopSending();
        else if (command.equals("Zz123456"))
            sender.sendMessage("Количество пользователей бота: " + Bot.users.size());
        else sender.sendMessage(WeatherInfo.getCurrentWeather(command));
    }
}

