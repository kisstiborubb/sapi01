<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.sprint.list.page.title"/></title>
</head>
<body>
    <h1><spring:message code="label.sprint.list.page.title"/></h1>
    <div>
        <a href="/sprint/add" id="add-button" class="btn btn-primary"><spring:message code="label.add.story.link"/></a>
    </div>
    <div id="sprint-list" class="page-content">
        <c:choose>
            <c:when test="${empty sprints}">
                <p><spring:message code="label.story.list.empty"/></p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${sprints}" var="sprint">
                    <div class="well well-small">
                        <div>
                        	<a id="sprint-update-${sprint.id}" href="/sprint/update/${sprint.id}"><c:out value="${sprint.title}"/></a>
                        </div>
                        
                        <c:choose>
	                        <c:when test="${not empty stories}">
		                        <div class="action-buttons">
		                        	<a id="story-list-edit" href="/sprint/storyList/${sprint.id}">View Stories</a>
		                        </div>
	                         </c:when>
                         </c:choose>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>