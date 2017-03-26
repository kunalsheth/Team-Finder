package com.teamfinder.main.server;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */
public class Index {

    public String generateIndex() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Team Finder</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/\" method=\"POST\">\n" +
                "    Name: <input type=\"text\"><br>\n" +
                "    Email: <input type=\"text\"><br>\n" +
                "    Age: <input type=\"number\"><br>\n" +
                "    Latitude: <input type=\"number\"><br>\n" +
                "    Longitude: <input type=\"number\"><br>\n" +
                "    Phone Number: <input type=\"tel\"><br>\n" +
                "    <input type=\"submit\">\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
