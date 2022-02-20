public class Commands {
    public void setMessage(String command, String chatId) {
        User user = Bot.users.get(chatId);
        switch (command) {
            case "get_current_weather" -> user.sendMessage(WeatherInfo.getCurrentWeather(user.getCity()));
            case "set_city" -> user.askForCity();
            case "set_timer" -> user.askForTimer();
            case "run_weather_receive_timer" -> user.startSending();
            case "stop_weather_receive_timer" -> user.stopSending();
            case "start" -> user.sendMessage("Просто напишите название города, " +
                    "погоду в котором хотите узнать, или используйте командное меню для настройки бота.");
            default -> user.sendMessage("Команда не найдена");
        }
    }
}

