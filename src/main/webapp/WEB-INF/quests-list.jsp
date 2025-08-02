<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quests</title>
</head>
<body>
<h1>Quests</h1>
<%
    List<String> questsList = (List<String>) session.getAttribute("questsList");
    if (questsList != null) {
        for (int i = 0; i < questsList.size(); i++) {
            out.println("<a href=\"/quest/%s\">%d. %s</a></br>"
                    .formatted(questsList.get(i).replaceAll("\\s+", "-").toLowerCase(), i + 1, questsList.get(i)));
        }
    }
%>
</body>
</html>
