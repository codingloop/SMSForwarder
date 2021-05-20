package in.codingloop.sms;

public class Constants {
    public static final String DEFAULT_EXTENSION = "+91";
    public static final int BU_R1_EQUALS = 1;
    public static final int BU_R2_CONTAINS = 2;

    /* DB Details */
    public static final String DATABASE_NAME = "sms_fwd_db";  // DB Name

    // Table Names
    public static final String T_CONTACTS = "contacts";
    public static final String T_BLOCKED_USERS = "blocked_users";
    public static final String T_ACTION_HISTORY = "action_history";

    // Table columns
    // Contacts columns
    public static final String CT_ID = "id";
    public static final String CT_EXTENSION = "extension";
    public static final String CT_NUMBER = "cnt_number";
    public static final String CT_MAX_LIMIT = "max_limit";

    // Blocked User columns
    public static final String BU_ID = "id";
    public static final String BU_NAME = "bu_name";
    public static final String BU_RELATION = "bu_relation";

    // History columns
    public static final String AH_ID = "id";
    public static final String AH_ACTION = "action_name";
    public static final String AH_ENTITY = "ah_entity";
    public static final String AH_INFORMATION = "information";
    public static final String AH_TIMESTAMP = "added_on";

    /* End DB Details */
}
