<%@include file="header.jsp" %>
<form action="upload.htm" method="post" enctype="multipart/form-data">
    <p>
        <label for="file" class="left">File</label>
        <input id="file" type="file" name="file"/>
        <br />
    </p>
    <p>
        <label for="submitBtn" class="left">&nbsp;</label>
        <a href="" class="button form_submit" ><span>Upload project</span></a>
        <input id="submitBtn" type="submit" value="Upload" class="novisible" />
    </p>
</form>
<%@include file="footer.jsp" %>