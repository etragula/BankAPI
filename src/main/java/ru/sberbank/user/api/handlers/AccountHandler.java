package ru.sberbank.user.api.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AccountHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JsonNode jsonNode = mapper.readValue(exchange.getRequestBody(), JsonNode.class);
        try {
            if (jsonNode.has("id")) {
                ipAccountController.createIndPartyAccount(jsonNode.get("id").asLong());
            } else if (jsonNode.has("phone")) {
                ipAccountController.createIndPartyAccount(jsonNode.get("phone").asText());
            }
        } catch (Exception exception) {
            sendResponse(exchange, exception.getMessage());
        }
        sendResponse(exchange, "OK");
        exchange.close();
    }
}
