<%-- 
    Document   : services
    Created on : Apr 26, 2012, 7:55:43 PM
    Author     : nodrock
--%>
<%@include file="../header.jsp" %>
<h1>Services</h1>
<div class="errors">
    <c:forEach items="${errors}" var="error">
        <p><c:out value="${error}" /></p>
    </c:forEach>
</div>  
<a href="<c:url value="/index.htm"/>">Back</a>
<table> 
    <tr>               
        <th>Name</th>
        <th>URL</th>  
        <th></th> 
    </tr>         
<c:forEach items="${services}" var="service">
    <c:url var="editUrl" value="editService.htm">
        <c:param name="id" value="${service.id}" />
    </c:url>
    <c:url var="deleteUrl" value="deleteService.htm">
        <c:param name="id" value="${service.id}" />
    </c:url>
    <tr>
        <td><c:out value="${service.name}" /></td> 
        <td><c:out value="${service.url}" /></td>
        <td><a href="${editUrl}"><img src="<c:url value="/resources/images/page_white_edit.png"/>" /></a></td>
        <td><a href="${deleteUrl}"><img src="<c:url value="/resources/images/page_white_delete.png"/>" /></a></td>
    </tr>
</c:forEach>
</table>
<a href="<c:url value="editService.htm"/>"><img src="<c:url value="/resources/images/page_white_add.png"/>" /> Add service</a>
<%@include file="../footer.jsp" %>
