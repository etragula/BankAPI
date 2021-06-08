package ru.sberbank.user.api.database.cruds;

import ru.sberbank.user.api.database.H2JDBC;
import ru.sberbank.user.api.models.IndividualParty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sberbank.user.api.database.queries.IndividualPartySQLQueries.*;

public class IndividualPartyCRUD {

    private final List<IndividualParty> backUpIndPartiesList = new ArrayList<>();

    public void creatIndPartyTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(IP_CREATE_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public void dropIndPartyTable() throws SQLException {
        Connection connection = H2JDBC.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(IP_DROP_TABLE_SQL);
        preparedStatement.execute();
        connection.close();
        preparedStatement.close();
    }

    public IndividualParty insertIndParty(String individualPartyName, String individualPartyPhone) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(IP_INSERT_CLIENT_SQL)) {
            Long id = setId();
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, individualPartyName);
            preparedStatement.setString(3, individualPartyPhone);
            preparedStatement.executeUpdate();
            return selectIndPartyById(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualParty selectIndPartyById(long individualPartyID) {
        String name = null;
        String phone = null;
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     IP_SELECT_BY_ID_SQL + individualPartyID)) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                name = rs.getString(getSecondField());
                phone = rs.getString(getThirdField());
            }
            return IndividualParty.builder().id(individualPartyID).name(name).phone(phone).build();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualParty selectIndPartyByName(String individualPartyName) {
        long id = 0;
        String phone = null;
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     IP_SELECT_BY_NAME_SQL + individualPartyName + '\'')) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                id = rs.getLong(getFirstField());
                phone = rs.getString(getThirdField());
            }
            return IndividualParty.builder().id(id).name(individualPartyName).phone(phone).build();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualParty selectIndPartyByPhone(String individualPartyPhone) {
        long id = 0;
        String name = null;
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     IP_SELECT_BY_PHONE_SQL + individualPartyPhone + '\'')) {
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.isBeforeFirst()) return null;
            while (rs.next()) {
                id = rs.getLong(getFirstField());
                name = rs.getString(getSecondField());
            }
            return IndividualParty.builder().id(id).name(name).phone(individualPartyPhone).build();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualParty setIndPartyName(long individualPartyID, String newIndividualPartyName) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     UPDATE_IP_NAME_SQL + individualPartyID)) {
            preparedStatement.setString(1, newIndividualPartyName);
            preparedStatement.executeUpdate();
            return selectIndPartyById(individualPartyID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public IndividualParty setIndPartyPhone(long individualPartyID, String newIndividualPartyPhone) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     UPDATE_IP_PHONE_SQL + individualPartyID)) {
            preparedStatement.setString(1, newIndividualPartyPhone);
            preparedStatement.executeUpdate();
            return selectIndPartyById(individualPartyID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void deleteIndParty(long individualPartyID) {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(IP_DELETE_ROW + individualPartyID)) {
            if (selectIndPartyById(individualPartyID) == null) return;
            else backUpIndPartiesList.add(selectIndPartyById(individualPartyID));
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public IndividualParty undoDeleteIndParty() {
        if (backUpIndPartiesList.size() != 0) {
            IndividualParty individualPartyCard = backUpIndPartiesList.get(backUpIndPartiesList.size() - 1);
            backUpIndPartiesList.remove(backUpIndPartiesList.size() - 1);
            try (Connection connection = H2JDBC.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(IP_INSERT_CLIENT_SQL)) {
                preparedStatement.setLong(1, individualPartyCard.getIndividualPartyID());
                preparedStatement.setString(2, individualPartyCard.getName());
                preparedStatement.setString(3, individualPartyCard.getPhone());
                preparedStatement.executeUpdate();
                return selectIndPartyById(individualPartyCard.getIndividualPartyID());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    private Long setId() {
        try (Connection connection = H2JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(IP_COUNT_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            long size = rs.getInt("COUNT");
            return size == 0 ? 1L : (size + 1L);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}