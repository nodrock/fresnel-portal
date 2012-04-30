<%@include file="header.jsp" %>
<form action="upload.htm" method="post" enctype="multipart/form-data">
<!--    <p>     
        <label for="file" class="left">File</label>
        <span style="position: relative; display: inline-block;">          
            <a href="" class="button form_file"><small class="icon save"></small><span>Choose file</span></a>
            <input id="xx" type="text" class="text w_40" />
            <input id="file" onchange="$(this).siblings('.text').val($(this).val())" type="file" name="file" style="position:absolute; bottom: 0; left: 0; width: 100%; height: 100%; opacity: 0;"/>
        </span>
       
        <br />
    </p>-->
    <p>     
        <label for="file" class="left">File</label>
        <input id="file" type="file" name="file" />
        <br />
    </p>
    <p>
        <label for="submitBtn" class="left">&nbsp;</label>
        <a href="" class="button form_submit" ><span>Upload project</span></a>
        <input id="submitBtn" type="submit" value="Upload" class="novisible" />
    </p>
</form>
<%@include file="footer.jsp" %>