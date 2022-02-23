import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = "";
    Commands commands = new Commands();
    static HashMap<String, User> users = new HashMap<>();
    User user;
    public static boolean isTalking = false;
    public static boolean isSettingTimer = false;

    @Override
    public String getBotUsername() {
        return "SaushWeatherBot";
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isTalking) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String inputMessage = update.getMessage().getText();
                if (WeatherInfo.getCurrentWeather(inputMessage).equals("Введен неправильный поисковый запрос :("))
                    sendMessage("Введен неправильный поисковый запрос," +
                            " попробуйте запустить команду еще раз " + Emoji.no_entry);
                else {
                    users.get(user.getChatId()).setCity(inputMessage);
                    sendMessage("Город по умолчанию установлен " + Emoji.ok);
                }
            }
            Bot.isTalking = false;
        } else if (isSettingTimer) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String inputMessage = update.getMessage().getText();
                String regex = "^[0-9]{2}-[0-9]{2}$";
                String regexTime = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
                if (inputMessage.matches(regex) || inputMessage.matches(regexTime)) {
                    users.get(user.getChatId()).setTimer(inputMessage);
                    sendMessage("Периодичность отправки сообщений успешно установлена " + Emoji.ok);
                } else {
                    sendMessage("Неправильный формат, попробуйте запустить команду еще раз " + Emoji.no_entry);
                }
            }
            Bot.isSettingTimer = false;
        } else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = String.valueOf(update.getMessage().getChatId());
                String inputMessage = update.getMessage().getText();

                if (users.containsKey(chatId)) user = users.get(chatId);
                else users.put(chatId, new User(chatId));

                if (inputMessage.toCharArray()[0] == '/') commands.setMessage(inputMessage.substring(1), chatId);
                else commands.nonCommand(inputMessage, chatId);
            }
        }
    }

    private void sendMessage(String msg) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(user.getChatId());
        message.setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
