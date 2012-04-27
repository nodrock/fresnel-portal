<%-- 
    Document   : editService
    Created on : Apr 27, 2012, 1:57:47 PM
    Author     : nodrock
--%>

<%@include file="../header.jsp" %>
<h1>Services <c:if test="${mode == 'create'}">create</c:if><c:if test="${mode == 'edit'}">edit</c:if></h1>
<form:form modelAttribute="service" action="saveService.htm" method="post" enctype="multipart/form-data">
    <form:hidden path="id" name="id" />
    <fieldset><legend>Upload Project</legend>
        <table>
            <tr>
                <td><label for="name">Name</label></td>
                <td><form:input path="name" id="name" type="text" name="name"/></td>
            </tr>
            <tr>
                <td><label for="name">URL</label></td>
                <td><form:input path="url" id="name" type="text" name="name"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Upload" /></td>
            </tr>
        </table>
    </fieldset>
</form:form>
<%@include file="../footer.jsp" %>