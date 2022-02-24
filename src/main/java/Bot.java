import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {

    public static final String BOT_TOKEN = "";
    Commands commands = new Commands();
    static HashMap<String, User> users;
    User user;
    public static boolean isTalking = false;
    public static boolean isSettingTimer = false;

    public Bot() {
        users = SaveRead.readFile();
    }

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
                    SaveRead.saveFile(users);
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
                    SaveRead.saveFile(users);
                    sendMessage("Периодичность отправки сообщений успешно установлена " + Emoji.ok,
                            Menu.getButton("Старт", "/run_weather_receive_timer"));
                } else {
                    sendMessage("Неправильный формат, попробуйте запустить команду еще раз " + Emoji.no_entry);
                }
            }
            Bot.isSettingTimer = false;
        } else {
            String inputMessage;
            if (update.hasMessage() && update.getMessage().hasText()) {
                String chatId = String.valueOf(update.getMessage().getChatId());

                if (users.containsKey(chatId)) user = users.get(chatId);
                else {
                    user = new User(chatId);
                    users.put(chatId, user);
                    SaveRead.saveFile(users);
                }

                inputMessage = update.getMessage().getText();

                if (inputMessage.toCharArray()[0] == '/') commands.setMessage(inputMessage, chatId);
                else commands.nonCommand(inputMessage, chatId);
            } else if (update.hasCallbackQuery()) {
                inputMessage = update.getCallbackQuery().getData();
                commands.setMessage(inputMessage, user.getChatId());
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

    private void sendMessage(String msg, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(user.getChatId());
        message.setText(msg);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
