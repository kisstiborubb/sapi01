<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Manage work hours for sprint</title>
</head>
<body>
    <h1>Manage work hours for sprint (<c:out value="${sprint.title}"/>)</h1>
    <div>
        <a href="/work/add/${sprint.id}" id="add-button" class="btn btn-primary">Add</a>
    </div>
    <div id="sprint-list" class="page-content">
        <c:choose>
            <c:when test="${empty works}">
                <p>There are no work hours added.</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ works }" var="work">
                    <div class="well well-small">
                        Name: <c:out value="${work.name}"/>
                        Start: <spring:eval expression="work.start" />
                        End: <spring:eval expression="work.end" />
                        
                        <a href="/work/update/${work.id}" class="btn btn-primary">Update</a>
       					<a href="/work/delete/${work.id}" class="btn btn-primary">Delete</a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>