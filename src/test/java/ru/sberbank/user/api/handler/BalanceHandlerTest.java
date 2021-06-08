package ru.sberbank.user.api.handler;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.sberbank.user.api.server.Server;
import ru.sberbank.user.api.steps.BaseTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BalanceHandlerTest extends BaseTest {

    @Test
    public void showIndPartyBalanceTest() throws IOException {
        Server.initServer();
        int id = 5;
        String accountNumber = "40817810216341000005";
        String phone = individualPartyController.getIndividualParty(5).getPhone();

        URL url = new URL(LOC_HOST + "/user/showBalance?id=" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        sendGetRequest(connection, url);
        String response = getResponse(connection);
        JsonNode jsonNode = mapper.valueToTree(ipAccountController.getIndPartyAccounts(5));
        Assert.assertEquals(jsonNode.toString(), response);

        url = new URL(LOC_HOST + "/user/showBalance?phone=" + phone);
        connection = (HttpURLConnection) url.openConnection();
        sendGetRequest(connection, url);
        response = getResponse(connection);
        jsonNode = mapper.valueToTree(individualPartyController.getIndividualParty(phone).getAccounts());
        Assert.assertEquals(jsonNode.toString(), response);

        url = new URL(LOC_HOST + "/user/showBalance?accountNumber=" + accountNumber);
        connection = (HttpURLConnection) url.openConnection();
        sendGetRequest(connection, url);
        response = getResponse(connection);
        jsonNode = mapper.valueToTree(ipAccountController.getIndPartyAccount(accountNumber));
        Assert.assertEquals(jsonNode.toString(), response);
        Server.stopServer();
    }

    @Test
    public void depositMoneyOnAccountTest() throws IOException {
        Server.initServer();

        URL url = new URL(LOC_HOST + "/user/deposit");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoOutput(true);

        String jsonInputString =
                "{\"accountNumber\": \"40817810216341000007\"," +
                        "\"amount\": \"6758.42\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            content.append(inputLine);

        Assert.assertEquals(content.toString(), "OK");
        Assert.assertEquals(ipAccountController.getIndPartyAccount("40817810216341000007").getBalance()
                , 6758.42, 0.0);

        Server.stopServer();
        connection.disconnect();
    }
}
