package ru.sberbank.user.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import ru.sberbank.user.api.controllers.IndividualPartyAccountController;
import ru.sberbank.user.api.controllers.IndividualPartyCardController;
import ru.sberbank.user.api.controllers.IndividualPartyController;
import ru.sberbank.user.api.database.cruds.IndividualPartyAccountCRUD;
import ru.sberbank.user.api.database.cruds.IndividualPartyCRUD;
import ru.sberbank.user.api.database.cruds.IndividualPartyCardCRUD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

public class BaseTest {

    protected static final IndividualPartyCRUD ipCrud = new IndividualPartyCRUD();
    protected static final IndividualPartyCardCRUD ipCardCrud = new IndividualPartyCardCRUD();
    protected static final IndividualPartyAccountCRUD ipAccountCrud = new IndividualPartyAccountCRUD();

    protected static final IndividualPartyCardController ipCardController = new IndividualPartyCardController();
    protected static final IndividualPartyController individualPartyController = new IndividualPartyController();
    protected static final IndividualPartyAccountController ipAccountController = new IndividualPartyAccountController();

    protected static final ObjectMapper mapper = new ObjectMapper();

    protected static final String LOC_HOST = "http://localhost:8080";

    @Before
    public void doBeforeTest() {
        try {
            ipCrud.creatIndPartyTable();
            ipCardCrud.creatCardTable();
            ipAccountCrud.createIPAccountTable();
        } catch (SQLException throwables) {
            doAfterTest();
            doBeforeTest();
        }
    }

    protected static void doAfterTest() {
        try {
            ipCrud.dropIndPartyTable();
            ipCardCrud.dropCardTable();
            ipAccountCrud.dropIPAccountTable();
        } catch (SQLException throwables) {
        }
    }

    protected static void sendGetRequest(HttpURLConnection connection, URL url) {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static String getResponse(HttpURLConnection connection) {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected static String phoneGenerator() {
        int num1, num2;
        int set2, set3;

        Random generator = new Random();

        num1 = generator.nextInt(9);
        num2 = generator.nextInt(9);

        set2 = generator.nextInt(643) + 100;
        set3 = generator.nextInt(8999) + 1000;

        return ("+79" + num1 + num2 + set2 + set3);
    }
}
