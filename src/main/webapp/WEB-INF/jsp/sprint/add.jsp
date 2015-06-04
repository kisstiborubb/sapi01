<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.sprint.add.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/sprint/add" commandName="sprint" method="POST" enctype="utf8">
            <div id="control-group-title" class="control-group">
                <label for="story-title"><spring:message code="label.story.title"/>:</label>

                <div class="controls">
                    <form:input id="story-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="story-description"><spring:message code="label.story.description"/>:</label>

                <div class="controls">
                    <form:textarea id="story-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
           <div id="control-group-stories" class="control-group">
                <label for="stories"><spring:message code="Stories"/>:</label>
                                
                <c:forEach var="story" items="${stories}">
                	<div class="checkbox">
                		<form:checkbox path="stories" value="${story.description}" label="${story.title}" />
                		<form:errors path="stories" cssClass="error" />
                	</div>                	               
                </c:forEach>   
            </div> 
            <div class="action-buttons">
                <a href="/sprint/list" class="btn"><spring:message code="label.cancel"/></a>
                <button id="add-story-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.add.story.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>