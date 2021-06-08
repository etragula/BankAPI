package ru.sberbank.user.api.server;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import ru.sberbank.user.api.managers.Base;
import ru.sberbank.user.api.managers.PropertyConstants;
import ru.sberbank.user.api.handlers.AccountHandler;
import ru.sberbank.user.api.handlers.BalanceHandler;
import ru.sberbank.user.api.handlers.IndividualPartyHandler;
import ru.sberbank.user.api.handlers.CardHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server extends Base {

    private static HttpServer server;

    private Server() {
    }

    public static void initServer() throws IOException {
        doBefore();
        server = HttpServer.create(new InetSocketAddress(PropertyConstants.HOSTNAME, PropertyConstants.PORT), 5);

        CardHandler cardHandler = new CardHandler();
        BalanceHandler balanceHandler = new BalanceHandler();
        AccountHandler accountHandler = new AccountHandler();
        IndividualPartyHandler individualPartyHandler = new IndividualPartyHandler();

        server.createContext("/user/showCards", cardHandler).setAuthenticator(new AuthChecker());
        server.createContext("/user/createCard", cardHandler).setAuthenticator(new AuthChecker());
        server.createContext("/user/deposit", balanceHandler).setAuthenticator(new AuthChecker());
        server.createContext("/user/showBalance", balanceHandler).setAuthenticator(new AuthChecker());
        server.createContext("/office/addNewIndParty", individualPartyHandler).setAuthenticator(new AuthChecker());
        server.createContext("/office/showIndParty", individualPartyHandler).setAuthenticator(new AuthChecker());
        server.createContext("/office/addNewAccount", accountHandler).setAuthenticator(new AuthChecker());

        server.start();
    }

    public static void stopServer() {
        server.stop(0);
    }

    static class AuthChecker extends Authenticator {
        @Override
        public Result authenticate(HttpExchange exchange) {
            if (exchange.getHttpContext().getPath().equals(exchange.getRequestURI().getPath()))
                return new Success(new HttpPrincipal("admin", "pass"));
            else
                return new Failure(403);
        }
    }
}
