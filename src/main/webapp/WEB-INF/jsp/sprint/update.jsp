<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1><spring:message code="label.sprint.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/sprint/update" commandName="sprint" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-title" class="control-group">
                <label for="sprint-title"><spring:message code="label.sprint.title"/>:</label>

                <div class="controls">
                    <form:input id="sprint-title" path="title"/>
                    <form:errors id="error-title" path="title" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-description" class="control-group">
                <label for="sprint-description"><spring:message code="label.sprint.description"/>:</label>

                <div class="controls">
                    <form:textarea id="sprint-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/sprint/${sprint.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-sprint-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.sprint.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>