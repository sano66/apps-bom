<%@page import="java.util.Enumeration"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Spring Security Index</title>
    </head>
    <body>
        <h2>Hello World!</h2>
        <hr>
        <form action="<c:url value="/logout"/>" method="post">
            <sec:csrfInput/>
            <button>logout</button>
        </form>
        <hr/>
        <h3>Security Debug Information</h3>

        <%
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {%>
        <p>
            Authentication object is of type: <em><%= auth.getClass().getName()%></em>
        </p>
        <p>
            Authentication object as a String: <br/><br/><%= auth.toString()%>
        </p>

        Authentication object holds the following granted authorities:<br /><br />
        <%
    for (GrantedAuthority authority : auth.getAuthorities()) {%>
        <%= authority%> (<em>getAuthority()</em>: <%= authority.getAuthority()%>)<br />
        <%			}
        %>

        <p><b>Success! Your web filters appear to be properly configured!</b></p>
        <%
        } else {
        %>
        Authentication object is null.<br />
        This is an error and your Spring Security application will not operate properly until corrected.<br /><br />
        <%		}
        %>
        <hr>
        <table>
            <thead><tr><th>name</th><th>value</th></tr></thead>
            <tbody>
                <%
                    Enumeration keys = session.getAttributeNames();
                    while (keys.hasMoreElements()) {
                        String key = (String) keys.nextElement();
                        out.println("<tr><td>" + key + "</td><td>" + session.getAttribute(key) + "</td></tr>");
                    }%>
            </tbody>
        </table>

    </body>
</html>
