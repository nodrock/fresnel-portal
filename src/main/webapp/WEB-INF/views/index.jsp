<%@include file="header.jsp" %>
<h1><fmt:message key="fresnel_portal"/></h1>

<%@include file="uploadFile.jsp" %>   

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
<%@include file="footer.jsp" %>
