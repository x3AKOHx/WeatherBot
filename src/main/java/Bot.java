import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

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
        return "5267180843:AAGAQfSJhoujYjqoC-kUFan-qkKuuBCNJ08";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String ok = EmojiParser.parseToUnicode(":ok_hand:");
        String no_entry = EmojiParser.parseToUnicode(":no_entry_sign:");
        if (isTalking) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String inputMessage = update.getMessage().getText();
                if (WeatherInfo.getCurrentWeather(inputMessage).equals("Введен неправильный поисковый запрос :("))
                    sendMessage("Введен неправильный поисковый запрос," +
                            " попробуйте запустить команду еще раз " + no_entry);
                else {
                    users.get(user.getChatId()).setCity(inputMessage);
                    sendMessage("Город по умолчанию установлен " + ok);
                }
            }
            Bot.isTalking = false;
        } else if (isSettingTimer){
            if (update.hasMessage() && update.getMessage().hasText()) {
                String inputMessage = update.getMessage().getText();
                String regex = "^[0-9]{2}-[0-9]{2}$";
                if (inputMessage.matches(regex)) {
                    users.get(user.getChatId()).setTimer(inputMessage);
                    sendMessage("Периодичность отправки сообщений успешно установлена " + ok);
                }
                else {
                    sendMessage("Неправильный формат, попробуйте запустить команду еще раз " + no_entry);
                }
            }
            Bot.isSettingTimer = false;
        } else {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = String.valueOf(update.getMessage().getChatId());
                String inputMessage = update.getMessage().getText();
                String messageText;

                if (users.containsKey(chatId)) user = users.get(chatId);
                else {
                    user = new User(chatId);
                    users.put(chatId, user);
                }

                if (inputMessage.toCharArray()[0] == '/') {
                    commands.setMessage(inputMessage.substring(1), chatId);
                } else {
                    messageText = WeatherInfo.getCurrentWeather(inputMessage);
                    sendMessage(messageText);
                }
            }
        }
    }

    private void sendMessage(String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(user.getChatId());
        message.setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
