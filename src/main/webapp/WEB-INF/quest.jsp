<%@ page import="com.javarush.kazakov.entity.Quest" %>
<%@ page import="com.javarush.kazakov.entity.Answer" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        Quest quest = (Quest) session.getAttribute("currentQuest");
        String questName = quest.getQuestName();
    %>
    <title><%=questName%>
    </title>
</head>
<body>
<h1><%=questName%>
</h1>
<form method="post">
    <h2><%=quest.getCurrentQuestion().getText()%>
    </h2>
    <%
        List<Answer> answers = quest.getCurrentQuestion().getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            out.println("<div>");
            out.println("<input type=\"radio\" id=\"answer_" + i + "\" name=\"answer\" value=\"" + i + "\" />");
            out.println("<label for=\"answer_" + i + "\">" + answer.getText() + "</label>");
            out.println("</div>");
        }
    %>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
