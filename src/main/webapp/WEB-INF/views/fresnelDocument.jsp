<%-- 
    Document   : fresnelDocument
    Created on : Apr 22, 2012, 4:40:39 PM
    Author     : nodrock
--%>

<%@include file="header.jsp" %>
<h1>Hello World!</h1>
<p>Found: <c:out value="${fn:length(lenses)}" /> lenses, <c:out value="${fn:length(formats)}" /> formats, <c:out value="${fn:length(groups)}" /> groups</p>

<h2>Lenses:</h2>
<table> 
    <tr>
        <th>URI</th>
        <th>Label</th>
        <th>Comment</th>
    </tr>
    <c:forEach items="${lenses}" var="lens">
        <tr>
            <td><c:out value="${lens.URI}"/></td>
            <td><c:out value="${lens.label}"/></td>
            <td><c:out value="${lens.comment}"/></td>  
        </tr>
    </c:forEach>
</table>  

<h2>Formats:</h2>
<table> 
    <tr>
        <th>URI</th>
        <th>Label</th>
        <th>Comment</th>
    </tr>
    <c:forEach items="${formats}" var="format">
        <tr>
            <td><c:out value="${format.URI}"/></td>
            <td><c:out value="${format.label}"/></td>
            <td><c:out value="${format.comment}"/></td>  
        </tr>
    </c:forEach>
</table>

<h2>Groups:</h2>
<table> 
    <tr>
        <th>URI</th>
        <th>Label</th>
        <th>Comment</th>
    </tr>
    <c:forEach items="${groups}" var="group">
        <tr>
            <td><c:out value="${group.URI}"/></td>
            <td><c:out value="${group.label}"/></td>
            <td><c:out value="${group.comment}"/></td>  
        </tr>
    </c:forEach>
</table>

<form action="render.htm" method="post">
    <fieldset><legend>Set visualization parameters</legend>
        <table>
            <tr>
                <td>Select group:</td>
                <td>
                    <select name="selectedGroup">
                        <c:forEach items="${groups}" var="group">
                            <option value="${group.URI}" label="${group.URI}" />
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Select service:</td>
                <td>
                    <select name="selectedService">
                        <option value="0" label="Semantic Web Client" />
                        <c:forEach items="${services}" var="service">
                            <option value="${service.id}" label="${service.name}" />
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Transformation:</td>
                <td>
                    <select name="selectedTransformation">
                        <option value="xhtml" label="XHTML" />
                        <c:forEach items="${groups}" var="group">
                            <option value="${group.URI}" label="${group.URI}" />
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Render document" /></td>
            </tr>
        </table>
    </fieldset>
</form>
<%@include file="footer.jsp" %>
