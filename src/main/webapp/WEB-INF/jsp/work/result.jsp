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
    <h1>Search work hours</h1>
    <div class="well page-content">
        <c:choose>
            <c:when test="${empty works}">
                <p>There are no work hours found.</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ works }" var="work">
                    <div class="well well-small">
                        Sprint title: <c:out value="${work.sprintTitle}" />
                        Name: <c:out value="${work.name}" />
                        Start: <c:out value="${work.start}" />
                        End: <c:out value="${work.end}" />
                        
                        <a href="/work/update/${work.id}" class="btn btn-primary">Update</a>
       					<a href="/work/delete/${work.id}" class="btn btn-primary">Delete</a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>