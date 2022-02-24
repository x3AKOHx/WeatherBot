import lombok.Getter;
import lombok.Setter;

import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.io.Serializable;

public class User implements Serializable {
    @Getter
    private String chatId;
    private WeatherNewsletter sendByTimer = null;
    @Setter
    @Getter
    private String city = "";
    private String timer = "";

    public User (String chatId) {
        this.chatId = chatId;
    }

    public User() {
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public void askForCity() {
        MessageSender sender = new MessageSender(chatId);
        Bot.isTalking = true;
        sender.sendMessage("Введите название города");
    }

    public void askForTimer() {
        MessageSender sender = new MessageSender(chatId);
        Bot.isSettingTimer = true;
        sender.sendMessage("Введите периодичность получения текущей погоды в формате \"часы-минуты\", " +
                "например: 00-15 (раз в 15 минут) или 24-00 (раз в сутки). Сообщения начнут приходить с момента " +
                "запуска таймера.\nТак же вы можете ввести время в формате \"часы:минуты\", \"например: 7:50. " +
                "В таком случе сообщения будут приходить раз в сутки, в заданное время (формат 24 часа).");
    }

    public void startSending() {
        MessageSender sender = new MessageSender(chatId);
        if (city.equals("")) {
            sender.sendMessage("Необходимо настроить город по умолчанию",
                    Menu.getButton("Настроить город", "/set_city"));
        }
        else if (timer.equals("")) {
            sender.sendMessage("Необходимо установить таймер отправки",
                    Menu.getButton("Настроить таймер", "/set_timer"));
        }
        else {
            if (sendByTimer != null) sendByTimer.stop();
            sendByTimer = new WeatherNewsletter(new DefaultBotOptions(), chatId, city, timer);
            sendByTimer.start();
            sender.sendMessage("Автоматическое получение сведений о погоде успешно запущенно");
        }
    }

    public void stopSending() {
        MessageSender sender = new MessageSender(chatId);
        sendByTimer.stop();
        sender.sendMessage("Автоматическое получение сведений о погоде успешно остановленно");
    }
}
