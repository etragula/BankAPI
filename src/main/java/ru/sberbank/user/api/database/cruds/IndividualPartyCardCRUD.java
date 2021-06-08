package ru.sberbank.user.api.database.cruds;

import ru.sberbank.user.api.database.H2JDBC;
import ru.sberbank.user.api.models.IndividualPartyCard;
import ru.sberbank.user.api.utils.CardStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.user.api.database.queries.IndividualPartyCardSQLQueries.*;

public class IndividualPartyCardCRUD {

    private final List<IndividualPartyCard> backUpCardList = new ArrayList<>();

    public void creatCardTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CARD_CREATE_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public void dropCardTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CARD_DROP_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public IndividualPartyCard insertIPCard(long individualPartyId, String ipAccountNumber) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CARD_INSERT_CLIENT_SQL)) {
            String cardNumber = getNextCardNumber();
            preparedStatement.setLong(1, individualPartyId);
            preparedStatement.setString(2, ipAccountNumber);
            preparedStatement.setString(3, cardNumber);
            preparedStatement.executeUpdate();
            return selectIPCard(cardNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualPartyCard selectIPCard(String cardNumber) {
        long id = 0;
        String accountNumber = null;
        CardStatus cardStatus = null;
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     CARD_SELECT_BY_CARD_SQL + cardNumber + "'")) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                id = rs.getLong(getFirstField());
                accountNumber = rs.getString(getSecondField());
                cardNumber = rs.getString(getThirdField());
                cardStatus = CardStatus.valueOf(rs.getString(getForthField()));
            }
            return IndividualPartyCard.builder().indPartyId(id).cardNumber(cardNumber)
                    .accountNumber(accountNumber).status(cardStatus).build();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<IndividualPartyCard> selectIPCard(long individualPartyId) {
        List<IndividualPartyCard> individualPartyCards = new ArrayList<>();
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     CARD_SELECT_BY_ID_SQL + individualPartyId)) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            int i = 1;
            while (rs.next()) {
                individualPartyCards.add(IndividualPartyCard.builder()
                        .id(i++)
                        .indPartyId(rs.getLong(getFirstField()))
                        .accountNumber(rs.getString(getSecondField()))
                        .cardNumber(rs.getString(getThirdField()))
                        .status(CardStatus.valueOf(rs.getString(getForthField())))
                        .build());
            }
            return individualPartyCards;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<IndividualPartyCard> selectIPCardByAcc(String accountNumber) {
        List<IndividualPartyCard> individualPartyCards = new ArrayList<>();
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     CARD_SELECT_BY_ACC_SQL + accountNumber + "'")) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            int i = 1;
            while (rs.next()) {
                individualPartyCards.add(IndividualPartyCard.builder()
                        .id(i++)
                        .indPartyId(rs.getLong(getFirstField()))
                        .accountNumber(rs.getString(getSecondField()))
                        .cardNumber(rs.getString(getThirdField()))
                        .status(CardStatus.valueOf(rs.getString(getForthField())))
                        .build());
            }
            return individualPartyCards;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void deleteIPCard(String cardNumber) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CARD_DELETE_ROW + cardNumber + "'")) {
            if (selectIPCard(cardNumber) == null) return;
            else backUpCardList.add(selectIPCard(cardNumber));
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public IndividualPartyCard undoDeleteIPCard() {
        if (backUpCardList.size() != 0) {
            IndividualPartyCard individualPartyCard = backUpCardList.get(backUpCardList.size() - 1);
            backUpCardList.remove(backUpCardList.size() - 1);
            try (Connection connection = H2JDBC.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(CARD_INSERT_CLIENT_SQL)) {
                preparedStatement.setLong(1, individualPartyCard.getIndPartyID());
                preparedStatement.setString(2, individualPartyCard.getAccountNumber());
                preparedStatement.setString(3, individualPartyCard.getCardNumber());
                preparedStatement.executeUpdate();
                return selectIPCard(individualPartyCard.getCardNumber());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public IndividualPartyCard setIPCardStatus(String ipCardNumber, CardStatus cardStatus) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     CARD_UPDATE_STATUS_SQL + ipCardNumber + ("'"))) {
            preparedStatement.setString(1, cardStatus.toString());
            preparedStatement.executeUpdate();
            return selectIPCard(ipCardNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private String getNextCardNumber() {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CARD_COUNT_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            return "42763412" + (10000001 + rs.getInt("COUNT"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}