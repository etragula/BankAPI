package ru.sberbank.user.api.handler;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.server.Server;
import ru.sberbank.user.api.steps.BaseTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AccountHandlerTest extends BaseTest {

    @Test
    public void addNewAccountTest() throws IOException {
        Server.initServer();

        IndividualParty individualParty = individualPartyController.createIndividualParty("Mike", phoneGenerator());

        URL url = new URL(LOC_HOST + "/office/addNewAccount");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonString = "{\"id\": \"" + individualParty.getIndividualPartyID() + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        Assert.assertEquals(content.toString(), "OK");
        Assert.assertEquals(content.toString(), "OK");
        Assert.assertEquals(ipAccountController.getIndPartyAccounts(individualParty.getIndividualPartyID()).size(), 1);

        Server.stopServer();
        connection.disconnect();
    }
}
