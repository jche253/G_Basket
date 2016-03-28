/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.JimmyChen.myapplication.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(
  name = "myApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "endpoint.myapplication.JimmyChen.example.com",
    ownerName = "endpoint.myapplication.JimmyChen.example.com",
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

    @ApiMethod(name = "test")
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
                response.setData(e.toString());
                return response;
            }
            url =
                    "jdbc:google:mysql://testing-1261:testdb?user=root";
        } else {
            // Connecting from an external network.
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                response.setData(e.toString());
                return response;
            }
            url = "jdbc:mysql://173.194.87.130:3306?user=root";
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            response.setData(e.toString());
            return response;
        }
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM userProfile");
            StringBuilder builder = new StringBuilder();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 0; i < columnCount;) {
                    builder.append(rs.getString(i + 1));
                    if (++i < columnCount) builder.append(",");
                }
                builder.append("\r\n");
            }
            String tosresponse = builder.toString();
            response.setData(tosresponse);
            return response;
        } catch (SQLException e) {
            response.setData(e.toString());
        }
        return response;
    }

}
