package ru.sberbank.user.api.managers;

public class PropertyConstants {

    private static final PropertyManager propertyManager = PropertyManager.getPropertyManager();

    public static String HOSTNAME = propertyManager.getProperty("hostname");

    public static int PORT = Integer.parseInt(propertyManager.getProperty("port"));

    public static String JDBC_URL = propertyManager.getProperty("jdbc_url");

    public static String JDBC_USERNAME = propertyManager.getProperty("jdbc_username");

    public static String JDBC_PASSWORD = propertyManager.getProperty("jdbc_password");
}