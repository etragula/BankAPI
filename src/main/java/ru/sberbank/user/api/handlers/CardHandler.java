package ru.sberbank.user.api.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberbank.user.api.models.IndividualPartyCard;
import ru.sberbank.user.api.utils.ValidationException;

import java.io.IOException;
import java.util.List;

public class CardHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestURI().getPath().equalsIgnoreCase("/user/showCards")) {
            getIndPartyCards(exchange);
        } else if (exchange.getRequestURI().getPath().equalsIgnoreCase("/user/createCard")) {
            createNewCard(exchange);
        }
    }

    public void createNewCard(HttpExchange exchange) throws IOException {
        JsonNode jsonNode = mapper.readValue(exchange.getRequestBody(), JsonNode.class);
        String accountNumber = jsonNode.get("accountNumber").asText();
        try {
            ipCardController.createIndPartyCard(accountNumber);
        } catch (Exception exception) {
            sendResponse(exchange, exception.getMessage());
        }
        sendResponse(exchange, "OK");
        exchange.close();
    }

    public void getIndPartyCards(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<IndividualPartyCard> individualPartyCards = null;
        String[] attribute = exchange.getRequestURI().toString().split("\\?")[1].split("=");
        try {
            switch (attribute[0]) {
                case "id":
                    individualPartyCards = ipCardController.getIndividualPartyCards(Long.parseLong(attribute[1]));
                    break;
                case "phone":
                    individualPartyCards = ipCardController.getIndividualPartyCards(attribute[1]);
                    break;
                case "accountNumber":
                    individualPartyCards = ipAccountController.getIndPartyAccount(attribute[1]).getCards();
                    break;
                default:
                    throw new ValidationException("Error: Bad request!");
            }
        } catch (Exception exception) {
            sendResponse(exchange, "Error: " + exception.getMessage());
            return;
        }
        sendResponse(exchange, mapper.valueToTree(individualPartyCards).toString());
    }
}
