import lombok.Setter;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherNewsletter extends DefaultAbsSender implements Runnable {
    @Setter
    String chatId;
    String city;
    String timer;
    private final Thread startSending = new Thread(this);;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public WeatherNewsletter(DefaultBotOptions options, String chatId, String city, String timer) {
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
        if (timer.contains("-")) {
            while (running.get()) {
                int minutes = Integer.parseInt(timer.split("-")[0]) * 60 + Integer.parseInt(timer.split("-")[1]);
                SendMessage message = new SendMessage();
                message.enableHtml(true);
                message.setChatId(this.chatId);
                message.setText(WeatherInfo.getCurrentWeather(city));
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                TimeUnit.MINUTES.sleep(minutes);
            }
        } else {
            if (timer.toCharArray()[0] == '0') timer = timer.substring(1);
            while (running.get()) {
                LocalTime time = LocalTime.now();
                if (time.getHour() == Integer.parseInt(timer.split(":")[0]) &&
                        time.getMinute() == Integer.parseInt(timer.split(":")[1])) {
                    SendMessage message = new SendMessage();
                    message.enableHtml(true);
                    message.setChatId(this.chatId);
                    message.setText(WeatherInfo.getCurrentWeather(city));
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    TimeUnit.HOURS.sleep(23);
                    TimeUnit.MINUTES.sleep(58);
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return Bot.BOT_TOKEN;
    }
}
