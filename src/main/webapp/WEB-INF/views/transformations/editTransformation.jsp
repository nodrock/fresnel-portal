<%-- 
    Document   : editService
    Created on : Apr 27, 2012, 1:57:47 PM
    Author     : nodrock
--%>

<%@include file="../header.jsp" %>
<form:form modelAttribute="transformation" action="saveTransformation.htm" method="post" enctype="multipart/form-data">
    <form:hidden path="id" name="id" />
    <p>
        <label for="name" class="left">Name</label>
        <form:input path="name" id="name" type="text" name="name" class="text w_40"/>
        <br />
    </p>
    <p>
        <label for="contentType" class="left">Content Type</label>
        <form:input path="contentType" id="contentType" type="text" name="contentType" class="text w_40"/>
        <br />
    </p>
    <c:if test="${transformation.id == null}">
        <p>
            <label for="file" class="left">XSLT file</label>
            <input inputid="fileData" type="file" name="file" class="w_40"/>
            <br/>
        </p>
    </c:if>
    <p>
        <label class="left">&nbsp;</label>
        <a href="" class="button form_submit" ><span>Save transformation</span></a>
        <input type="submit" value="Save transformation" class="novisible" />
    </p>
</form:form>
<%@include file="../footer.jsp" %>