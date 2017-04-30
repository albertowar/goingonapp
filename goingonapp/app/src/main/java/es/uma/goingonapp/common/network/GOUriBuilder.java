package es.uma.goingonapp.common.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public class GOUriBuilder {
    public static final String Endpoint = "http://goingontest.azurewebsites.net";

    public static String getHotNewsUri(String city) {
        return String.format("%s/api/hot/%s", GOUriBuilder.Endpoint, city);
    }

    public static String getUserUri(String nickname) {
        return String.format("%s/api/user/%s", GOUriBuilder.Endpoint, nickname);
    }

    public static String getCreateUserUri() {
        return String.format("%s/api/user", GOUriBuilder.Endpoint);
    }

    public static String getCreateNewsUri(String city, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return String.format("%s/api/city/%s/date/%s", GOUriBuilder.Endpoint, city, dateFormat.format(date));
    }

    public static String getCreateImageUri(String city, Date date, UUID uuid) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return String.format("%s/api/city/%s/date/%s/news/%s/image", GOUriBuilder.Endpoint, city, dateFormat.format(date), uuid.toString());
    }
}
