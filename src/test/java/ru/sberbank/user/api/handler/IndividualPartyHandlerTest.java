package ru.sberbank.user.api.handler;

import com.fasterxml.jackson.databind.JsonNode;
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

public class IndividualPartyHandlerTest extends BaseTest {

    @Test
    public void showIndPartyInfoTest() throws IOException {
        Server.initServer();

        IndividualParty individualParty = individualPartyController.createIndividualParty(
                "James Johnson",
                "+79884933692"
        );

        URL url = new URL(LOC_HOST + "/office/showIndParty?id=" + individualParty.getIndividualPartyID());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        sendGetRequest(connection, url);
        String response = getResponse(connection);
        JsonNode jsonNode = mapper.valueToTree(individualParty);
        Assert.assertEquals(jsonNode.toString(), response);

        url = new URL(LOC_HOST + "/office/showIndParty?phone=" + individualParty.getPhone());
        connection = (HttpURLConnection) url.openConnection();
        sendGetRequest(connection, url);
        response = getResponse(connection);
        jsonNode = mapper.valueToTree(individualParty);
        Assert.assertEquals(jsonNode.toString(), response);

        Server.stopServer();
    }

    @Test
    public void createNewIndPartyTest() throws IOException {
        Server.initServer();

        URL url = new URL(LOC_HOST + "/office/addNewIndParty");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonString =
                "{\"name\": \"Mikhail Petrosyan\"," +
                        " \"phone\": \"+79774943692\"}";

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
        Assert.assertNotNull(individualPartyController.getIndividualParty("+79774943692"));

        connection.disconnect();
        Server.stopServer();
    }
}
