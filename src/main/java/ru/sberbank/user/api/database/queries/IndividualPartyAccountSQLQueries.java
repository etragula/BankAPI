package ru.sberbank.user.api.database.queries;

public class IndividualPartyAccountSQLQueries {

    private static final String TABLE_NAME = "ACCOUNTS";
    private static final String FIRST_FIELD = "ID";
    private static final String SECOND_FIELD = "Account_Number";
    private static final String THIRD_FIELD = "Balance";

    public static final String AC_CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (\r\n" +
            FIRST_FIELD + " int NOT NULL,\r\n" +
            SECOND_FIELD + " varchar(30) NOT NULL,\r\n" +
            THIRD_FIELD + " double,\r\n" +
            "UNIQUE (" + SECOND_FIELD + "));";

    public static final String AC_INSERT_ACCOUNT_SQL = "INSERT INTO " + TABLE_NAME + "(" +
            FIRST_FIELD + ", " +
            SECOND_FIELD + ", " +
            THIRD_FIELD + ")" +
            " VALUES (?, ?, ?);";

    public static final String AC_DROP_TABLE_SQL = "DROP TABLE " + TABLE_NAME;

    public static final String AC_SELECT_BY_ID_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + FIRST_FIELD + " = ";

    public static final String AC_SELECT_BY_NUMBER_SQL = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + SECOND_FIELD + " = '";

    public static final String AC_COUNT_SQL = "SELECT COUNT(*) as COUNT" +
            " FROM " + TABLE_NAME;

    public static final String UPDATE_AC_BALANCE_SQL = "UPDATE " + TABLE_NAME + " SET " +
            THIRD_FIELD + " = ? " +
            " WHERE " + SECOND_FIELD + " = '";

    public static final String AC_DELETE_ROW_BY_NUM = "DELETE FROM " + TABLE_NAME +
            " WHERE " + SECOND_FIELD + " = '";

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
