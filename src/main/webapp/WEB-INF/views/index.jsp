<%@include file="header.jsp" %>
<table> 
    <tr>
        <th>URI</th>
        <th>Name</th>
        <th>Description</th>
        <th>Filename</th>
        <th></th>
<!--        <th></th>
        <th></th>-->
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
            <td><a href="<c:out value="${prod.uri}"/>"><c:out value="${prod.uri}"/></a></td>
            <td><c:out value="${prod.title}"/></td>
            <td><c:out value="${prod.description}"/></td>
            <td><c:out value="${prod.filename}"/></td>
<!--            <td><a href="${downloadUrl}"><img src="<c:url value="/resources/images/page_white_download.png"/>" /></a></td>
            <td><a href="${deleteUrl}"><img src="<c:url value="/resources/images/page_white_delete.png"/>" /></a></td>
            <td><a href="${fresnelDocumentUrl}"><img src="<c:url value="/resources/images/page_white_go.png"/>" /></a></td>-->
            <td class="buttons_bar" style="width: 330px;">
                <a href="${downloadUrl}" class="button yellow"><small class="icon save"></small><span>Download</span></a>
                <a href="${deleteUrl}" class="button red"><small class="icon cross"></small><span>Remove</span></a>
                <a href="${fresnelDocumentUrl}" class="button green"><small class="icon play"></small><span>Render</span></a>
            </td>
        </tr>
    </c:forEach>
</table>  
<a href="<c:url value="uploadProject.htm"/>" class="button green"><small class="icon plus"></small><span>Add project</span></a>
<%@include file="footer.jsp" %>
