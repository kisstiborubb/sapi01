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
    <h1><spring:message code="label.group.update.page.title"/></h1>
    <div class="well page-content">
        <form:form action="/group/update" commandName="group" method="POST" enctype="utf8">
            <form:hidden path="id"/>            
            <div id="control-group-description" class="control-group">
                <label for="group-description"><spring:message code="label.group.description"/>:</label>

                <div class="controls">
                    <form:textarea id="group-description" path="description"/>
                    <form:errors id="error-description" path="description" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/group/${group.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-group-button" type="submit" class="btn btn-primary"><spring:message
                        code="label.update.group.button"/></button>
            </div>
        </form:form>
    </div>
</body>
</html>