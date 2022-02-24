import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Menu extends DefaultAbsSender {

    protected Menu(DefaultBotOptions options) {
        super(options);
    }

    public void getMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(chatId);
        message.setText("Просто напишите название города, погоду в котором хотите узнать, " +
                "или используйте командное меню для настройки бота.");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardButton button = new KeyboardButton(Emoji.WTF + " Погода сейчас");
        keyboardRow.add(button);
        keyboard.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        button = new KeyboardButton(Emoji.moon_face + " Прогноз на 3 дня");
        keyboardRow.add(button);
        button = new KeyboardButton(Emoji.sun_face + " Прогноз на неделю");
        keyboardRow.add(button);
        keyboard.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        button = new KeyboardButton(Emoji.city + " Установить город по умолчанию");
        keyboardRow.add(button);
        button = new KeyboardButton(Emoji.timer + " Настроить таймер получения уведомлений");
        keyboardRow.add(button);
        keyboard.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        button = new KeyboardButton(Emoji.yes + " Включить уведомления");
        keyboardRow.add(button);
        button = new KeyboardButton(Emoji.no + " Выключить уведомления");
        keyboardRow.add(button);
        keyboard.add(keyboardRow);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static InlineKeyboardMarkup getButton(String buttonText, String sendText) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(buttonText);
        inlineKeyboardButton.setCallbackData(sendText);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    @Override
    public String getBotToken() {
        return Bot.BOT_TOKEN;
    }
}
