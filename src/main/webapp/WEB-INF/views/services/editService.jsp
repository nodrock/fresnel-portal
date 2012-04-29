<%-- 
    Document   : editService
    Created on : Apr 27, 2012, 1:57:47 PM
    Author     : nodrock
--%>

<%@include file="../header.jsp" %>
<h1>Services <c:if test="${mode == 'create'}">create</c:if><c:if test="${mode == 'edit'}">edit</c:if></h1>
<form:form modelAttribute="service" action="saveService.htm" method="post" enctype="multipart/form-data">
    <form:hidden path="id" name="id" />
    	<p>
            <label for="name" class="left">Name</label>
            <form:input path="name" id="name" type="text" name="name" class="text w_40"/>
            <br />
	</p>
	<p>
            <label for="url" class="left">URL</label>
            <form:input path="url" id="name" type="text" name="url" class="text w_40"/>
            <br />
	</p>
        <p>
            <label class="left">&nbsp;</label>
            <a href="" class="button form_submit" ><span>Save service</span></a>
            <input type="submit" value="Save service" class="novisible" />
        </p>
</form:form>
<%@include file="../footer.jsp" %>