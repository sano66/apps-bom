<%-- 
    Document   : login
    Created on : Feb 5, 2018, 8:40:03 PM
    Author     : sano
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html><head><title>Login Page</title></head><body onload='document.f.username.focus();'>
        <h3>Login with Username and Password</h3><form name='f' action='<c:url value="/login.jsp"/>' method='POST'>
            <table>
                <tr><td>Branch:</td><td><input type='text' name='branchid'/></td></tr>
                <tr><td>User:</td><td><input type='text' name='userid' value=''></td></tr>
                <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
            </table>
            <sec:csrfInput/>
        </form></body>
</html>