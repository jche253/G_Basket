<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.google.appengine.api.utils.SystemProperty" %>

<html>
  <body>

<%
String url = null;
if (SystemProperty.environment.value() ==
    SystemProperty.Environment.Value.Production) {
  // Load the class that provides the new "jdbc:google:mysql://" prefix.
  Class.forName("com.mysql.jdbc.GoogleDriver");
  url = "jdbc:google:mysql://testing-1261:testdb/testdb?user=root";
} else {
  // Local MySQL instance to use during development.
  Class.forName("com.mysql.jdbc.Driver");
  url = "jdbc:mysql://173.194.87.130:3306/testdb?user=root";
}

Connection conn = DriverManager.getConnection(url);
ResultSet rs = conn.createStatement().executeQuery(
    "SELECT fname, lname, email FROM users");
%>

<table style="border: 1px solid black">
<tbody>
<tr>
<th width="35%" style="background-color: #CCFFCC; margin: 5px">Name</th>
<th style="background-color: #CCFFCC; margin: 5px">Message</th>
<th style="background-color: #CCFFCC; margin: 5px">ID</th>
</tr>

<%
while (rs.next()) {
    String fname = rs.getString("fname");
    String lname = rs.getString("lname");
    String email = rs.getString("email");
 %>
<tr>
<td><%= fname %></td>
<td><%= lname %></td>
<td><%= email %></td>
</tr>
<%
}
conn.close();
%>

</tbody>
</table>
<br />
No more messages!

  </body>
</html>