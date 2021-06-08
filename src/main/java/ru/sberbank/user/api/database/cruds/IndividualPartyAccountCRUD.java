package ru.sberbank.user.api.database.cruds;

import ru.sberbank.user.api.database.H2JDBC;
import ru.sberbank.user.api.models.IndividualPartyAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.user.api.database.queries.IndividualPartyAccountSQLQueries.*;

public class IndividualPartyAccountCRUD {

    private final IndividualPartyCardCRUD ipCardCrud = new IndividualPartyCardCRUD();
    private final List<IndividualPartyAccount> backUpIPAccList = new ArrayList<>();

    public void createIPAccountTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(AC_CREATE_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public void dropIPAccountTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(AC_DROP_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public IndividualPartyAccount insertIPAccount(long individualPartyID) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AC_INSERT_ACCOUNT_SQL)) {
            String accountNumber = getNextAccountNumber();
            preparedStatement.setLong(1, individualPartyID);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.setDouble(3, 0.0D);
            preparedStatement.executeUpdate();
            return selectIPAccByNumber(accountNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<IndividualPartyAccount> selectIPAccounts(long individualPartyID) {
        List<IndividualPartyAccount> accountList = new ArrayList<>();
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AC_SELECT_BY_ID_SQL + individualPartyID)) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            int i = 1;
            while (rs.next()) {
                IndividualPartyAccount account = IndividualPartyAccount.builder()
                        .indPartyId(rs.getLong(getFirstField()))
                        .number(rs.getString(getSecondField()))
                        .balance(rs.getDouble(getThirdField()))
                        .id(i++).build();
                try {
                    account.setCards(ipCardCrud.selectIPCardByAcc(account.getNumber()));
                } catch (Exception exception) {
                }
                accountList.add(account);
            }
            return accountList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualPartyAccount selectIPAccByNumber(String accountNumber) {
        IndividualPartyAccount account = null;
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AC_SELECT_BY_NUMBER_SQL + accountNumber + ("'"))) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                account = IndividualPartyAccount.builder()
                        .indPartyId(rs.getLong(getFirstField()))
                        .number(rs.getString(getSecondField()))
                        .balance(rs.getDouble(getThirdField()))
                        .build();
                try {
                    account.setCards(ipCardCrud.selectIPCardByAcc(account.getNumber()));
                } catch (Exception exception) {
                }
            }
            return account;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualPartyAccount setIPAccBalance(String accountNumber, double newBalance) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     UPDATE_AC_BALANCE_SQL.toString() + accountNumber + ("'"))) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.executeUpdate();
            return selectIPAccByNumber(accountNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void deleteAccount(String accountNumber) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     AC_DELETE_ROW_BY_NUM + accountNumber + ("'"))) {
            if (selectIPAccByNumber(accountNumber) == null) return;
            else backUpIPAccList.add(selectIPAccByNumber(accountNumber));
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public IndividualPartyAccount undoDeleteAccount() {
        if (backUpIPAccList.size() != 0) {
            IndividualPartyAccount individualPartyAccount = backUpIPAccList.get(backUpIPAccList.size() - 1);
            backUpIPAccList.remove(backUpIPAccList.size() - 1);
            try (Connection connection = H2JDBC.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(AC_INSERT_ACCOUNT_SQL)) {
                preparedStatement.setLong(1, individualPartyAccount.getIndPartyId());
                preparedStatement.setString(2, individualPartyAccount.getNumber());
                preparedStatement.setDouble(3, individualPartyAccount.getBalance());
                preparedStatement.executeUpdate();
                return selectIPAccByNumber(individualPartyAccount.getNumber());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    private String getNextAccountNumber() throws SQLException {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AC_COUNT_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return "4081781021634" + (1000001 + rs.getInt("COUNT"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}