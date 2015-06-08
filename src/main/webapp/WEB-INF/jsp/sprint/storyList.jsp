<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title><spring:message code="label.sprint.list.page.title"/></title>
</head>
<body>
    <h1><spring:message code="label.storyList.page.title"/></h1>   
    <div id="sprint-list" class="page-content">
    	<form:form action="/sprint/storyList" commandName="sprint" method="POST" enctype="utf8">
    	 	
    	 	<!--  <c:forEach var="sprintStory" items="${sprintStories}">
             	<div class="checkbox">                	                		
             		<form:checkbox checked="checked" path="sprintStories" value="${sprintStory.id}" label="${sprintStory.title}" />   	          		
             	</div>   
             	             
             </c:forEach> -->
    	 
 	 		<c:forEach var="story" items="${stories}">
             	<div class="checkbox">                	                		
             		<form:checkbox path="stories" id="story-${story.id}" value="${story.id}" label="${story.title}" />   	          		
             	</div>                	             
             </c:forEach> 
             
            <div class="action-buttons">
            	<a href="/sprint/list" class="btn"><spring:message code="label.cancel"/></a>
            	<button id="save-button" type="submit" class="btn btn-primary"><spring:message
                    code="Save"/></button>
            </div> 
            </form:form>
    </div>
</body>
</html>