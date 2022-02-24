import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSender extends DefaultAbsSender {

    private final String chatId;
    protected MessageSender(String chatId) {
        super(new DefaultBotOptions());
        this.chatId = chatId;
    }

    public void sendMessage(String msg) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(chatId);
        message.setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(chatId);
        message.setText(msg);
        message.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return Bot.BOT_TOKEN;
    }
}
