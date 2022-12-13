package com.example.pbl6app.Utils;

import com.example.pbl6app.Models.User;

public class Constant {
    public static String BASE_URL = "https://6cb0-13-251-197-11.ap.ngrok.io";

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}";

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\$@\\$!#%*?&.,])[A-Za-z\\d\\$@\\$!#%*?&.,]{8,}$";

    public static final int PROVINCE_DATA = 0;
    public static final int DISTRICT_DATA = 21;
    public static final int WARD_DATA = 332;
    public static final int GENDER_DATA = 111;
    public static final int TYPE_OF_JOB_DATA = 222;
    public static final int NUMBER_HOUR_DATA = 333;
    public static final int JOB_INFO_DATA = 444;



    public static String username = "";
    public static String email = "";
    public static String pass = "";

    public static User USER = new User();
}
