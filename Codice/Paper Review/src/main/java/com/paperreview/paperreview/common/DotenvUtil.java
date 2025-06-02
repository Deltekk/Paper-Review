package com.paperreview.paperreview.common;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvUtil{
    private static Dotenv dotenv; // legge .env dalla root

    public static void init() {
        if (dotenv == null) {
            dotenv = Dotenv.load();
        }
    }

    public static String getEmailSender() {
        return dotenv.get("EMAIL_SENDER");
    }

    public static String getEmailPassword() {
        return dotenv.get("EMAIL_PASSWORD");
    }

    public static String getDBBaseUrl() {
        return dotenv.get("DB_BASE_URL");
    }

    public static String getDbUser() {
        return dotenv.get("DB_USER");
    }

    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }

}
