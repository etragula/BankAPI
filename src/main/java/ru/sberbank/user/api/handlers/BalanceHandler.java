package ru.sberbank.user.api.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.user.api.utils.ValidationException;

import java.io.IOException;

public class BalanceHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().equalsIgnoreCase("/user/showBalance")) {
            showIndPartyBalance(exchange);
        } else if (exchange.getRequestURI().getPath().equalsIgnoreCase("/user/deposit")) {
            depositMoneyOnAccount(exchange);
        }
    }

    public void depositMoneyOnAccount(HttpExchange exchange) throws IOException {
        JsonNode jsonNode = mapper.readValue(exchange.getRequestBody(), JsonNode.class);
        String accountNumber = jsonNode.get("accountNumber").asText();
        double amount = jsonNode.get("amount").asDouble();
        try {
            ipAccountController.addIndPartyAccountBalance(accountNumber, amount);
        } catch (Exception exception) {
            sendResponse(exchange, exception.getMessage());
        }
        sendResponse(exchange, "OK");
        exchange.close();
    }

    public void showIndPartyBalance(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String[] attribute = exchange.getRequestURI().toString().split("\\?")[1].split("=");
        try {
            switch (attribute[0]) {
                case "id":
                    sendResponse(exchange, mapper.valueToTree(ipAccountController
                            .getIndPartyAccounts(Long.parseLong(attribute[1]))).toString());
                    break;
                case "phone":
                    sendResponse(exchange, mapper.valueToTree(individualPartyController
                            .getIndividualParty(attribute[1]).getAccounts()).toString());
                    break;
                case "accountNumber":
                    sendResponse(exchange, mapper.valueToTree(ipAccountController
                            .getIndPartyAccount(attribute[1])).toString());
                    break;
                default:
                    throw new ValidationException("Error: Bad request!");
            }
        } catch (ValidationException exception) {
            sendResponse(exchange, "Error: " + exception.getMessage());
        }
    }
}
