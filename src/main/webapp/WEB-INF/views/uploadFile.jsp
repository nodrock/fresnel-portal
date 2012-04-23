<%-- 
    Document   : uploadFile
    Created on : Apr 21, 2012, 5:03:23 PM
    Author     : nodrock
--%>

<form action="upload.htm" method="post" enctype="multipart/form-data">
    <fieldset><legend>Upload Project</legend>
        <table>
            <tr>
                <td><label for="fileData">File</label></td>
                <td><input id="fileData" type="file" name="file"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Upload" /></td>
            </tr>
        </table>
    </fieldset>
</form>