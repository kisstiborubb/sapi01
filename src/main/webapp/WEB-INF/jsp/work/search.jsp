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
    <h1>Search work hours</h1>
    <div class="well page-content">
        <form:form action="/work/postsearch" commandName="search" method="POST" enctype="utf8">
            Define search criteria:
            
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
                <button type="submit" class="btn btn-primary">Search</button>
            </div>
        </form:form>
    </div>
</body>
</html>