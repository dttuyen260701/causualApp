package com.example.pbl6app.Utils;

import com.example.pbl6app.Models.User;

public class Constant {
    public static final int BUSY_STATUS = 1;
    public static final int ROLE_WORKER = 1;
    public static final int ROLE_CUSTOMER = 0;
    public static String BASE_URL = "https://f427-14-250-209-246.ap.ngrok.io";

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}";

    public static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\$@\\$!#%*?&.,])[A-Za-z\\d\\$@\\$!#%*?&.,]{8,}$";

    public final static int PROCESS_STATUS = 0;
    public final static int CANCEL_STATUS = 1;
    public final static int COMPLETED_STATUS = 2;
    public final static int WAITING_STATUS = 3;
    public final static int REJECT_STATUS = 4;
    public final static int ACCEPT_STATUS = 5;
    public final static int TRACKING_STATUS = 6;

    public static final int PROVINCE_DATA = 0;
    public static final int DISTRICT_DATA = 21;
    public static final int WARD_DATA = 332;
    public static final int GENDER_DATA = 111;
    public static final int JOB_INFO_DATA_DETAIL = 666;
    public static final int TYPE_OF_JOB_DATA = 222;
    public static final int NUMBER_HOUR_DATA = 333;
    public static final int JOB_INFO_DATA = 444;

    public static final String CHANNEL_ID = "CHANNEL";
    public static final String DATA_LOGIN = "DATA_LOGIN";

    public static String username = "";
    public static String email = "";
    public static String pass = "";

    public static User USER = new User();

    public static int DEFAULT_PAGE_SIZE = 10;
}
