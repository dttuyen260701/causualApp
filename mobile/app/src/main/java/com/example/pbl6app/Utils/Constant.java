package com.example.pbl6app.Utils;

import com.example.pbl6app.Models.User;

public class Constant {
    public static String BASE_URL = "https://9bfd-14-245-64-89.ap.ngrok.io";

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}";

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\$@\\$!#%*?&.,])[A-Za-z\\d\\$@\\$!#%*?&.,]{8,}$";

    public static String username = "";
    public static String email = "";
    public static String pass = "";

    public static User USER = new User();
}
