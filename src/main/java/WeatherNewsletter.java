import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherNewsletter extends DefaultAbsSender implements Runnable {
    @Setter
    String chatId;
    String city;
    int timer;
    private final Thread startSending = new Thread(this);;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public WeatherNewsletter(DefaultBotOptions options, String chatId, String city, int timer) {
        super(options);
        this.chatId = chatId;
        this.city = city;
        this.timer = timer;
    }

    public void start() {
        startSending.start();
    }

    public void stop() {
        running.set(false);
        startSending.stop();
    }


    @SneakyThrows
    public void run() {
        running.set(true);
        while (running.get()) {
            SendMessage message = new SendMessage();
            message.setChatId(this.chatId);
            message.setText(WeatherInfo.getCurrentWeather(city));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            TimeUnit.MINUTES.sleep(timer);
        }
    }

    @Override
    public String getBotToken() {
        return "5267180843:AAGAQfSJhoujYjqoC-kUFan-qkKuuBCNJ08";
    }
}
