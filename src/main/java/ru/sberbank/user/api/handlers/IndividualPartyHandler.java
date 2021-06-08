package ru.sberbank.user.api.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.user.api.models.IndividualParty;

import java.io.IOException;

public class IndividualPartyHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            showIndPartyInfo(exchange);
        } else if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
            createNewIndParty(exchange);
        }
    }

    public void createNewIndParty(HttpExchange exchange) throws IOException {
        JsonNode jsonNode = mapper.readValue(exchange.getRequestBody(), JsonNode.class);
        String name = jsonNode.get("name").asText();
        String phone = jsonNode.get("phone").asText();
        try {
            individualPartyController.createIndividualParty(name, phone);
        } catch (Exception exception) {
            sendResponse(exchange, exception.getMessage());
        }
        sendResponse(exchange, "OK");
        exchange.close();
    }

    public void showIndPartyInfo(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IndividualParty individualParty = null;
        String[] attribute = exchange.getRequestURI().toString().split("\\?")[1].split("=");
        try {
            switch (attribute[0]) {
                case "id":
                    individualParty = individualPartyController.getIndividualParty(Long.parseLong(attribute[1]));
                    break;
                case "phone":
                    individualParty = individualPartyController.getIndividualParty(attribute[1]);
                    break;
                default:
                    sendResponse(exchange, "Error: Bad request!");
                    break;
            }
        } catch (Exception exception) {
            sendResponse(exchange, "Error: " + exception.getMessage());
            return;
        }
        sendResponse(exchange, mapper.valueToTree(individualParty).toString());
    }
}
