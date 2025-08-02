<%@ page import="com.javarush.kazakov.entity.Result" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Result result = (Result) session.getAttribute("result");
    session.setAttribute("currentQuest", null);
%>
<html>
<head>
    <title>Result</title>
</head>
<body>
<h1>Result</h1>
<h2><%=result.getText()%>
</h2>
<a href="/quest">Back to quests list</a>
</body>
</html>
