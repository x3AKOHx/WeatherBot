import lombok.SneakyThrows;

import java.io.*;
import java.util.HashMap;

public class SaveRead implements Serializable {
    private static final String PATH = "users.db";

    @SneakyThrows
    public static void saveFile(HashMap<String, User> users) {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
            os.writeObject(users);
        }
    }

    @SneakyThrows
    public static HashMap<String, User> readFile() {
        File file = new File(PATH);
        HashMap<String, User> map = new HashMap<>();
        if (file.length() > 0) {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            map = (HashMap<String, User>) ois.readObject();
            ois.close();
        }
        return map;
    }
}
