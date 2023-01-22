package com.example.application.services;

import com.vaadin.flow.component.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Shared {

    private static Connection conn;

    public static Connection getDb() throws SQLException {
        if(conn == null) {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost/cinema?user=postgres&password=1234&ssl=false");
        }

        return conn;
    }

    /**
     * Alert helper.
     *
     * @param text
     */
    public static void alert(String text) {
        UI.getCurrent().getPage().executeJs("alert('"+text+"')");
    }
}
