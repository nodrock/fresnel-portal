<%-- 
    Document   : fresnelDocument
    Created on : Apr 22, 2012, 4:40:39 PM
    Author     : nodrock
--%>

<%@include file="header.jsp" %>
<h1><c:out value="${project.title}" /></h1>
<p>Found: <c:out value="${fn:length(lenses)}" /> lenses, <c:out value="${fn:length(formats)}" /> formats, <c:out value="${fn:length(groups)}" /> groups</p>

<h2>Lenses:</h2>
<table> 
    <tr>
        <th class="w_40">URI</th>
        <th class="w_30">Label</th>
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

<br />

<h2>Formats:</h2>
<table> 
    <tr>
        <th class="w_40">URI</th>
        <th class="w_30">Label</th>
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

<br />

<h2>Groups:</h2>
<table> 
    <tr>
        <th class="w_40">URI</th>
        <th class="w_30">Label</th>
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

<br />
<hr />
<h1><fmt:message key="fresnel_rendering_parameters" /></h1>
<form action="render.htm" method="post">
    <p>
        <label for="selectedGroup" class="left">Select group:</label>
        <select id="selectedGroup" name="selectedGroup" class="w_40">
            <c:forEach items="${groups}" var="group">
                <option value="${group.URI}" label="${group.URI}" />
            </c:forEach>
        </select>
        <br /> 
    </p>
    <p>
        <label for="selectedService" class="left">Select service:</label>

        <select id="selectedService" name="selectedService" class="w_40">
            <option value="0" label="Semantic Web Client" />
            <c:forEach items="${services}" var="service">
                <option value="${service.id}" label="${service.name}" />
            </c:forEach>
        </select>
        <br />
    </p>
    <p>
        <label for="selectedTransformation" class="left">Transformation:</label>

        <select id="selectedTransformation" name="selectedTransformation" class="w_40">
            <option value="0" label="No transformation" />
            <c:forEach items="${transformations}" var="transformation">
                <option value="${transformation.id}" label="${transformation.name}" />
            </c:forEach>
        </select>
        <br />
    </p>
    <p>

        <label for="submitBtn" class="left">&nbsp;</label>
        <a href="" class="button form_submit" ><span>Render Document</span></a>
        <input id="submitBtn" type="submit" value="Render Document" class="novisible" />
    </p>

</form>
<%@include file="footer.jsp" %>
