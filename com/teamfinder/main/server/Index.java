package com.teamfinder.main.server;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */
public class Index {

    public static String generateIndex() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Team Finder</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/\" method=\"POST\">\n" +
                "    Name: <input name=\"name\" type=\"text\"><br>\n" +
                "    Email: <input name=\"email\" type=\"text\"><br>\n" +
                "    Age: <input name=\"age\" type=\"number\"><br>\n" +
                "    Latitude: <input name=\"latitude\" type=\"number\"><br>\n" +
                "    Longitude: <input name=\"longitude\" type=\"number\"><br>\n" +
                "    Phone Number: <input name=\"phone\" type=\"tel\"><br>\n" +
                "    <input type=\"submit\">\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
