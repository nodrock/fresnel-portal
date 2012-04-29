<%-- 
    Document   : transformations
    Created on : Apr 27, 2012, 7:55:43 PM
    Author     : nodrock
--%>
<%@include file="../header.jsp" %>
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
        <td class="buttons_bar" style="width: 180px;">
            <a href="${editUrl}" class="button yellow"><small class="icon pencil"></small><span>Edit</span></a>
            <a href="${deleteUrl}" class="button red"><small class="icon cross"></small><span>Remove</span></a>
        </td>
    </tr>
</c:forEach>
</table>
<a href="<c:url value="editTransformation.htm"/>" class="button green"><small class="icon plus"></small><span>Add transformation</span></a>
<%@include file="../footer.jsp" %>
