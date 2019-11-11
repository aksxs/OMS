import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonInstance {
    private static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("dd.MM.yyyy hh:mm").create();
        }

        return gson;
    }
}
