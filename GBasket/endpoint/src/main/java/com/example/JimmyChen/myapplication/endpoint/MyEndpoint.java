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

    @ApiMethod(name = "loginquery")
    public MyBean test(@Named("email") String email, @Named("pw") String pw) {
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
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement stmt = conn.createStatement();
            try {
                rs = stmt.executeQuery
                        ("SELECT fname, lname FROM product WHERE email = '"+email+ "' AND pw = '" + pw +"'");
                if (rs.next()) {
                    String fn = rs.getString(1);
                    String ln = rs.getString(2);
                    response.setFname(fn);
                    response.setLname(ln);
                }
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

    @ApiMethod(name = "product_query")
    public MyBean pquery(@Named("format") String format, @Named("content") String content) {
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
        MyBean response = new MyBean();
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
                rs = stmt.executeQuery
                        ("SELECT pname, price FROM product WHERE format = '" +format+ "' AND pname = '" + content + "'");
                if (rs.next()) {
                    String pn = rs.getString(1);
                    double pr = rs.getDouble(2);
                    response.setPname(pn);
                    response.setPrice(pr);

                }
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