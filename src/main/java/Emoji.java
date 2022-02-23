import com.vdurmont.emoji.EmojiParser;

public class Emoji {
    public static String ok = EmojiParser.parseToUnicode(":ok_hand:");
    public static String no_entry = EmojiParser.parseToUnicode(":no_entry_sign:");
    public static String WTF = EmojiParser.parseToUnicode("&#129335;");
    public static String moon_face = EmojiParser.parseToUnicode("&#127770;");
    public static String sun_face = EmojiParser.parseToUnicode("&#127773;");
    public static String city = EmojiParser.parseToUnicode("&#127750;");
    public static String timer = EmojiParser.parseToUnicode("&#9201;");
    public static String yes = EmojiParser.parseToUnicode("&#9989;");
    public static String no = EmojiParser.parseToUnicode("&#9940;");

    public static String getWeatherEmoji(int ID) {
        String emoji;
        if (ID != 800) {
            ID = Math.floorDiv(ID, 100);
        }

        switch (ID) {
            case 2 -> emoji = EmojiParser.parseToUnicode("&#9928;");
            case 3 -> emoji = EmojiParser.parseToUnicode("&#127783;");
            case 5 -> emoji = EmojiParser.parseToUnicode("&#9748;");
            case 6 -> emoji = EmojiParser.parseToUnicode("&#127784;");
            case 7 -> emoji = EmojiParser.parseToUnicode("&#127787;");
            case 8 -> emoji = EmojiParser.parseToUnicode("&#9729;");
            case 800 -> emoji = EmojiParser.parseToUnicode("&#9728;");
            default -> emoji = EmojiParser.parseToUnicode("&#127781;");
        }
        return emoji + emoji + emoji;
    }
}
