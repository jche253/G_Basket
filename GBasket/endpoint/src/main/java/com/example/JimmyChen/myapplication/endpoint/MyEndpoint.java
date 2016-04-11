/*
   For step-by-step instructions on connecting your Android application to this\
 backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/maste\
r/HelloEndpoints
*/

package com.example.JimmyChen.myapplication.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.utils.SystemProperty;
import com.google.cloud.sql.jdbc.Connection;
import com.google.cloud.sql.jdbc.ResultSet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.JimmyChen.example.com",
                ownerName = "backend.myapplication.JimmyChen.example.com",
                packagePath=""
        )
)
public class MyEndpoint {

    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {
        MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "query")
    public MyBean test(@Named("name") String name) {
        MyBean response = new MyBean();

        String url = null;
        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            // Connecting from App Engine.
            // Load the class that provides the "jdbc:google:mysql://"
            // prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            url =
                    "jdbc:google:mysql://testing-1261:testdb/testdb?user=root";
        } else {
            // Connecting from an external network.
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            url = "jdbc:mysql://173.194.87.130:3306?user=root";
        }


        java.sql.Connection conn = null;
        java.sql.ResultSet rs;
        ArrayList<String> arr = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement stmt = conn.createStatement();
            try {
                rs = stmt.executeQuery("SELECT * FROM product");
                while (rs.next()) {
                    String em = rs.getString(1);
                    arr.add(em);
                }
                response.setData(arr.get(0));
                return response;
            } catch (SQLException e) {
                e.printStackTrace();
                response.setData(e + " ");
                return response;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setData(e + " ");
            return response;
        }

    }
}