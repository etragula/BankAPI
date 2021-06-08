package ru.sberbank.user.api.database.queries;

public class IndividualPartyCardSQLQueries {

    private static final String TABLE_NAME = "CARDS";
    private static final String FIRST_FIELD = "IND_PARTY_ID";
    private static final String SECOND_FIELD = "Account_Number";
    private static final String THIRD_FIELD = "Card_Number";
    private static final String FORTH_FIELD = "Card_Status";

    public static final String CARD_CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (\r\n" +
            FIRST_FIELD + " int NOT NULL,\r\n" +
            SECOND_FIELD + " varchar(30) NOT NULL,\r\n" +
            THIRD_FIELD + " varchar(30) NOT NULL,\r\n" +
            FORTH_FIELD + " varchar(30) NOT NULL,\r\n" +
            "UNIQUE (" + THIRD_FIELD + "));";

    public static final String CARD_INSERT_CLIENT_SQL = "INSERT INTO " + TABLE_NAME + "(" +
            FIRST_FIELD + ", " +
            SECOND_FIELD + ", " +
            THIRD_FIELD + ", " +
            FORTH_FIELD + ")" +
            " VALUES (?, ?, ?, 'IN_REVIEW');";

    public static final String CARD_DROP_TABLE_SQL = "DROP TABLE " + TABLE_NAME;

    public static final String CARD_SELECT_BY_ID_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + FIRST_FIELD + " = ";

    public static final String CARD_SELECT_BY_CARD_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + THIRD_FIELD + " = '";

    public static final String CARD_SELECT_BY_ACC_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + SECOND_FIELD + " = '";

    public static final String CARD_UPDATE_STATUS_SQL = "UPDATE " + TABLE_NAME + " SET " +
            FORTH_FIELD + " = ? " +
            " WHERE " + THIRD_FIELD + " = '";

    public static final String CARD_COUNT_SQL = "SELECT COUNT(*) as COUNT" +
            " FROM " + TABLE_NAME;

    public static final String CARD_DELETE_ROW = "DELETE FROM " + TABLE_NAME +
            " WHERE " + THIRD_FIELD + " = '";

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

    public static String getForthField() {
        return FORTH_FIELD;
    }
}
