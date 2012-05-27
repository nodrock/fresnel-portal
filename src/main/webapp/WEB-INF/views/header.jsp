<%-- 
    Document   : header.jsp
    Created on : Apr 27, 2012, 1:59:21 PM
    Author     : nodrock
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<title>Fresnel portal</title>
                <link rel="stylesheet" href="<c:url value="/resources/css/style.css"/>" type="text/css" media="screen" charset="utf-8" />
		<script src="<c:url value="/resources/js/jquery.js"/>" type="text/javascript" charset="utf-8"></script>
		<script src="<c:url value="/resources/js/global.js"/>" type="text/javascript" charset="utf-8"></script>
		<script src="<c:url value="/resources/js/modal.js"/>" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<div id="header">
			<div class="col w5 bottomlast">
                                <a href="<c:url value="/index.htm"/>" class="logo">
                                    <h1>Fresnel portal</h1>
				</a>
			</div>
			
			<div class="clear"></div>
		</div>
		<div id="wrapper">
			<div id="minwidth">
				<div id="holder">
					<div id="menu">
						<div id="left"></div>
						<div id="right"></div>
						<ul>
							<li>
                                                            <a href="<c:url value="/index.htm"/>" <c:if test="${currentSection == 'fresnel_projects'}">class="selected"</c:if>><span><fmt:message key="fresnel_projects"/></span></a>
							</li>
							<li>
                                                            <a href="<c:url value="/services/services.htm"/>" <c:if test="${currentSection == 'services_management'}">class="selected"</c:if>><span><fmt:message key="services_management"/></span></a>
							</li>
							<li>
                                                            <a href="<c:url value="/transformations/transformations.htm"/>" <c:if test="${currentSection == 'transformations_management'}">class="selected"</c:if>><span><fmt:message key="transformations_management"/></span></a>
							</li>
						</ul>
						<div class="clear"></div>
					</div>
					<div id="submenu">
						<div class="modules_left">							
						</div>
						<div class="title">
                                                    <fmt:message key="${currentPage}"/>
						</div>
						<div class="modules_right">
						</div>
					</div>
					<div id="desc">
						<div class="body">
                                                    <div id="messages">
                                                        <c:forEach items="${messages}" var="message">                                                        
                                                            <div class="${message.type}">
                                                                <div class="tl"></div><div class="tr"></div>
                                                                <div class="desc">
                                                                    <p><fmt:message key="${message.text}"/></p>
                                                                </div>
                                                                <div class="bl"></div><div class="br"></div>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="clear"></div>