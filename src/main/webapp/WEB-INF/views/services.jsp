<%-- 
    Document   : services
    Created on : Apr 26, 2012, 7:55:43 PM
    Author     : nodrock
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <c:forEach items="${services}" var="service">
            <c:out value="${service.name}" /> <c:out value="${service.url}" /> <br/>
        </c:forEach>
    </body>
</html>
