<%-- 
    Document   : services
    Created on : Apr 26, 2012, 7:55:43 PM
    Author     : nodrock
--%>
<%@include file="../header.jsp" %> 
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
        <td class="buttons_bar" style="width: 180px;">
            <a href="${editUrl}" class="button yellow"><small class="icon pencil"></small><span>Edit</span></a>
            <a href="${deleteUrl}" class="button red"><small class="icon cross"></small><span>Remove</span></a>
        </td>
    </tr>
</c:forEach>
</table>
<a href="<c:url value="editService.htm"/>" class="button green"><small class="icon plus"></small><span>Add service</span></a>
<%@include file="../footer.jsp" %>
