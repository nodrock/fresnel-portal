<%-- 
    Document   : editService
    Created on : Apr 27, 2012, 1:57:47 PM
    Author     : nodrock
--%>

<%@include file="../header.jsp" %>
<h1>Services <c:if test="${mode == 'create'}">create</c:if><c:if test="${mode == 'edit'}">edit</c:if></h1>
<form:form modelAttribute="service" action="saveTransformation.htm" method="post" enctype="multipart/form-data">
    <form:hidden path="id" name="id" />
    <fieldset><legend>Transformation</legend>
        <table>
            <tr>
                <td><label for="name">Name</label></td>
                <td><form:input path="name" id="name" type="text" name="name"/></td>
            </tr>
            <tr>
                <td><label for="name">Content Type</label></td>
                <td><form:input path="contentType" id="contentType" type="text" name="contentType"/></td>
            </tr>
            <c:if test="${mode == 'create'}">
            <tr>
                <td><label for="name">XSLT file</label></td>
                <td><input inputid="fileData" type="file" name="file"/></td>
            </tr>
            </c:if>
            <tr>
                <td></td>
                <td><input type="submit" value="Save" /></td>
            </tr>
        </table>
    </fieldset>
</form:form>
<%@include file="../footer.jsp" %>