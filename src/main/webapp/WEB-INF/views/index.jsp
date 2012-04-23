<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
    </head>

    <body>
        <h1><fmt:message key="fresnel_portal"/></h1>
        
        <%@include file="uploadFile.jsp" %>
          
        <div class="errors">
        <c:forEach items="${errors}" var="error">
            <p><c:out value="${error}" /></p>
        </c:forEach>
        </div>    
        
        <table> 
            <tr>
                <th>URI</th>
                <th>Name</th>
                <th>Description</th>
                <th>Filename</th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach items="${projects}" var="prod">
                <c:url var="downloadUrl" value="download.htm">
                    <c:param name="id" value="${prod.id}" />
                </c:url>
                <c:url var="deleteUrl" value="delete.htm">
                    <c:param name="id" value="${prod.id}" />
                </c:url>
                <c:url var="fresnelDocumentUrl" value="fresnelDocument.htm">
                    <c:param name="id" value="${prod.id}" />
                </c:url>
            <tr>
                <td><c:out value="${prod.uri}"/></td>
                <td><c:out value="${prod.name}"/></td>
                <td><c:out value="${prod.description}"/></td>
                <td><c:out value="${prod.filename}"/></td>
                <td><a href="${downloadUrl}"><img src="<c:url value="/resources/images/page_white_download.png"/>" /></a></td>
                <td><a href="${deleteUrl}"><img src="<c:url value="/resources/images/page_white_delete.png"/>" /></a></td>
                <td><a href="${fresnelDocumentUrl}"><img src="<c:url value="/resources/images/page_white_go.png"/>" /></a></td>
            </tr>
            </c:forEach>
        </table>  
    </body>
</html>
