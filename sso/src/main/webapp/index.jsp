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
    </body>
</html>
