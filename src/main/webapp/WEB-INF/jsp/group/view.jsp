<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/group.view.js"></script>
</head>
<body>
    <div id="group-id" class="hidden">${group.id}</div>
    <h1><spring:message code="label.group.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="group-description"><c:out value="${group.description}"/></h2>
        <div>
            <p><c:out value="${group.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/group/update/${group.id}" class="btn btn-primary"><spring:message code="label.update.group.link"/></a>
            <a id="delete-group-link" class="btn btn-primary"><spring:message code="label.delete.group.link"/></a>
        </div>
    </div>
    <script id="template-delete-group-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-group-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.group.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.group.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-group-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-group-button" href="#" class="btn btn-primary"><spring:message code="label.delete.group.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>