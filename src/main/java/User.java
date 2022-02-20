import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class User extends DefaultAbsSender {
    @Getter
    private final String chatId;
    private WeatherNewsletter sendByTimer = null;
    @Setter
    @Getter
    private String city = "";
    private int timer = 0;

    public User (String chatId) {
        super(new DefaultBotOptions());
        this.chatId = chatId;
    }

    public void setTimer(String str) {
        int hours = Integer.parseInt(str.split("-")[0]);
        int minutes = Integer.parseInt(str.split("-")[1]);
        this.timer = hours * 60 + minutes;
    }

    public void askForCity() {
        Bot.isTalking = true;
        sendMessage("Введите название города");
    }

    public void askForTimer() {
        Bot.isSettingTimer = true;
        sendMessage("Введите периодичность отправки сообщений в формате \"часы-минуты\", " +
                "например: 00-15 (раз в 15 минут) или 24-00 (раз в сутки)");
    }

    public void startSending() {
        if (city.equals("")) sendMessage("Необходимо задать гороод по умолчанию");
        else if (timer == 0) sendMessage("Необходимо установить таймер отправки");
        else {
            if (sendByTimer != null) sendByTimer.stop();
            sendByTimer = new WeatherNewsletter(new DefaultBotOptions(), chatId, city, timer);
            sendByTimer.start();
            sendMessage("Автоматическое получение сведений о погоде успешно запущенно");
        }
    }

    public void stopSending() {
        sendByTimer.stop();
        sendMessage("Автоматическое получение сведений о погоде успешно остановленно");
    }

    public void sendMessage(String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "5267180843:AAGAQfSJhoujYjqoC-kUFan-qkKuuBCNJ08";
    }
}
