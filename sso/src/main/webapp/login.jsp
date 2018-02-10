<%-- 
    Document   : login
    Created on : Feb 5, 2018, 8:40:03 PM
    Author     : sano
--%>

<%@page import="java.util.Enumeration"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html><head><title>Login Page</title></head><body onload='document.f.branchid.focus();'>
        <c:if test="${not empty param.error}">
            <p><font color='red'>Your login attempt was not successful, try again.<br/><br/>Reason: ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.mesage}</font></p>
            </c:if>
            <c:if test="${not empty param.logout}">
            <p><font color='green'>You have been logged out</font></p>
            </c:if>
        <h3>Login with Username and Password</h3><form name='f' action='<c:url value="/login.jsp"/>' method='POST'>
            <table>
                <tr><td>Branch:</td><td><input type='text' name='branchid'/></td></tr>
                <tr><td>User:</td><td><input type='text' name='userid' value=''></td></tr>
                <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
            </table>
            <sec:csrfInput/>
        </form>
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
        <table>
            <thead><tr><th>name</th><th>value</th></tr></thead>
            <tbody>
                <%
                    Enumeration params = request.getParameterNames();
                    while (params.hasMoreElements()) {
                        String key = (String) params.nextElement();
                        out.println("<tr><td>" + key + "</td><td>" + request.getParameter(key) + "</td></tr>");
                    }%>
            </tbody>
        </table>
    </body>
</html>