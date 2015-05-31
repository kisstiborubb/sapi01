<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/sprint.view.js"></script>
</head>
<body>
    <div id="sprint-id" class="hidden">${sprint.id}</div>
    <h1><spring:message code="label.sprint.view.page.title"/></h1>
    <div class="well page-content">
        <h2 id="sprint-title"><c:out value="${sprint.title}"/></h2>
        <div>
            <p><c:out value="${sprint.description}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/sprint/update/${sprint.id}" class="btn btn-primary"><spring:message code="label.update.sprint.link"/></a>
            <a id="delete-sprint-link" class="btn btn-primary"><spring:message code="label.delete.sprint.link"/></a>
        </div>
    </div>
    <script id="template-delete-sprint-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-sprint-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.sprint.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.sprint.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-sprint-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-sprint-button" href="#" class="btn btn-primary"><spring:message code="label.delete.sprint.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>