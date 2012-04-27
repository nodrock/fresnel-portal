<%-- 
    Document   : transformations
    Created on : Apr 27, 2012, 7:55:43 PM
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
        <th>Filename</th>
        <th>ContentType</th>  
        <th></th> 
    </tr>         
<c:forEach items="${transformations}" var="transformation">
    <c:url var="editUrl" value="editTransformation.htm">
        <c:param name="id" value="${transformation.id}" />
    </c:url>
    <c:url var="deleteUrl" value="deleteTransformation.htm">
        <c:param name="id" value="${transformation.id}" />
    </c:url>
    <tr>
        <td><c:out value="${transformation.name}" /></td> 
        <td><c:out value="${transformation.filename}" /></td>
        <td><c:out value="${transformation.contentType}" /></td>
        <td><a href="${editUrl}"><img src="<c:url value="/resources/images/page_white_edit.png"/>" /></a></td>
        <td><a href="${deleteUrl}"><img src="<c:url value="/resources/images/page_white_delete.png"/>" /></a></td>
    </tr>
</c:forEach>
</table>
<a href="<c:url value="editTransformation.htm"/>"><img src="<c:url value="/resources/images/page_white_add.png"/>" /> Add transformation</a>
<%@include file="../footer.jsp" %>
