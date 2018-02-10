<%@page import="java.util.Enumeration"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>

<html>
    <head>
        <title>Security Debug Information</title>
    </head>
    <body>

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
                    Enumeration<String> keys = session.getAttributeNames();
                    while (keys.hasMoreElements()) {
                        String key = keys.nextElement();
                        out.println("<tr><td>" + key + "</td><td>" + session.getAttribute(key) + "</td></tr>");
                    }%>
            </tbody>
        </table>

    </body>
</html>
