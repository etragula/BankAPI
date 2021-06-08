package ru.sberbank.user.api.database.queries;

public class IndividualPartySQLQueries {

    private static final String TABLE_NAME = "INDIVIDUAL_PARTIES";
    private static final String FIRST_FIELD = "ID";
    private static final String SECOND_FIELD = "Name";
    private static final String THIRD_FIELD = "Phone";

    public static final String IP_CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (\r\n" +
            FIRST_FIELD + " int NOT NULL,\r\n" +
            SECOND_FIELD + " varchar(30) NOT NULL,\r\n" +
            THIRD_FIELD + " varchar(30) NOT NULL,\r\n" +
            "UNIQUE (" + FIRST_FIELD + ", " + THIRD_FIELD + " ));";

    public static final String IP_INSERT_CLIENT_SQL = "INSERT INTO " + TABLE_NAME + "(" +
            FIRST_FIELD + ", " +
            SECOND_FIELD + ", " +
            THIRD_FIELD + ")" +
            " VALUES (?, ?, ?);";

    public static final String IP_DROP_TABLE_SQL = "DROP TABLE " + TABLE_NAME;

    public static final String IP_SELECT_BY_ID_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + FIRST_FIELD + " = ";

    public static final String IP_SELECT_BY_NAME_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + SECOND_FIELD + " = '";

    public static final String IP_SELECT_BY_PHONE_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + THIRD_FIELD + " = '";

    public static final String IP_COUNT_SQL = "SELECT COUNT(*) as COUNT" +
            " FROM " + TABLE_NAME;

    public static final String UPDATE_IP_NAME_SQL = "UPDATE " + TABLE_NAME + " SET " +
            SECOND_FIELD + " = ? " +
            " WHERE " + FIRST_FIELD + " = ";

    public static final String UPDATE_IP_PHONE_SQL = "UPDATE " + TABLE_NAME + " SET " +
            THIRD_FIELD + " = ? " +
            " WHERE " + FIRST_FIELD + " = ";

    public static final String IP_DELETE_ROW = "DELETE FROM " + TABLE_NAME +
            " WHERE " + FIRST_FIELD + " = ";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getFirstField() {
        return FIRST_FIELD;
    }

    public static String getSecondField() {
        return SECOND_FIELD;
    }

    public static String getThirdField() {
        return THIRD_FIELD;
    }
}
