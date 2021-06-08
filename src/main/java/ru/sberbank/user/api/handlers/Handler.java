package ru.sberbank.user.api.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ru.sberbank.user.api.controllers.IndividualPartyAccountController;
import ru.sberbank.user.api.controllers.IndividualPartyCardController;
import ru.sberbank.user.api.controllers.IndividualPartyController;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Handler {

    protected final IndividualPartyCardController ipCardController = new IndividualPartyCardController();
    protected final IndividualPartyAccountController ipAccountController = new IndividualPartyAccountController();
    protected final IndividualPartyController individualPartyController = new IndividualPartyController();

    protected final ObjectMapper mapper = new ObjectMapper();

    protected void sendResponse(HttpExchange exchange, String strToResponse) throws IOException {
        exchange.sendResponseHeaders(200, strToResponse.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(strToResponse.getBytes());
        outputStream.close();
    }
}
