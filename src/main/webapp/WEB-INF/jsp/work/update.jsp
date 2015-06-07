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
    <h1>Edit work hours</h1>
    <div class="well page-content">
        <form:form action="/work/saveupdate" commandName="work" method="POST" enctype="utf8">
        	<form:hidden path="id" />
        	<form:hidden path="sprintId" />
        
            <div class="control-group">
                <label for="name">Assignee name:</label>

                <div class="controls">
                    <form:input id="name" path="name"/>
                    <form:errors path="name" cssClass="help-inline"/>
                </div>
            </div>
            <div class="control-group">
                <label for="start">Start:</label>

                <div class="controls">
                    <form:input id="start" path="start"/>
                    <form:errors path="start" cssClass="help-inline"/>
                </div>
            </div>
            <div class="control-group">
                <label for="end">End:</label>

                <div class="controls">
                    <form:input id="end" path="end"/>
                    <form:errors path="end" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/" class="btn">Cancel</a>
                <button type="submit" class="btn btn-primary">Update</button>
            </div>
        </form:form>
    </div>
</body>
</html>