<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.group.list.page.title"/></title>
</head>
<body>
    <h1><spring:message code="label.group.list.page.title"/></h1>
    <div>
        <a href="/group/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.group.link"/></a>
    </div>
    <div id="group-list" class="page-content">
        <c:choose>
            <c:when test="${empty groups}">
                <p><spring:message code="label.group.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ groups}" var="group">
                    <div class="well well-small">
                        <a id="group-${group.id}" href="/group/${group.id}"><c:out value="${group.description}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>