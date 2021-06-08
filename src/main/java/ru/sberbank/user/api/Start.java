package ru.sberbank.user.api;

import ru.sberbank.user.api.server.Server;

import java.io.IOException;

public class Start {

    public static void main(String[] args) throws IOException {
        Server.initServer();
    }
}
