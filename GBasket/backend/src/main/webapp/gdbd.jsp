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
  url = "jdbc:google:mysql://pioneering-flag-125204:gbdb/gbdb?user=root";
} else {
  // Local MySQL instance to use during development.
  Class.forName("com.mysql.jdbc.Driver");
  url = "jdbc:mysql://173.194.227.45:3306/jbdb?user=root";
}

Connection conn = DriverManager.getConnection(url);
ResultSet rs = conn.createStatement().executeQuery(
    "SELECT fname, lname FROM userProfile");
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
 %>
<tr>
<td><%= fname %></td>
<td><%= lname %></td>
</tr>
<%
}
conn.close();
%>

</tbody>
</table>
<br />
No more messages!
<p><strong>Hello its me!</strong></p>
<form action="/sign" method="post">
    <div>First Name: <input type="text" name="fname"></input></div>
    <div>Message:
    <br /><textarea name="content" rows="3" cols="60"></textarea>
    </div>
    <div><input type="submit" value="Post Greeting" /></div>
    <input type="hidden" name="fname" />
  </form>
  </body>
</html>