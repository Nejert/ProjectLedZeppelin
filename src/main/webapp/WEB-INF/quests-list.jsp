<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container text-light text-bg-dark py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>Quests</h2>
        </div>
    </div>
    <div class="row gy-4 row-cols-1 row-cols-md-2 row-cols-xl-3">
        <%
            Object quests = session.getAttribute("questAuthorMap");
            if (quests != null) {
                Map<String, String> questAuthorMap = (Map<String, String>) quests;
                for (Map.Entry<String, String> questAuthor : questAuthorMap.entrySet()) {
        %>
        <div class="col">
            <div class="d-flex">
                <div class="px-3">
                    <h4><%=questAuthor.getKey()%>
                    </h4>
                    <%if (questAuthor.getValue() != null) {%>
                    <p style="margin-bottom: 7px;">
                        Author: <%=questAuthor.getValue()%> <%//TODO:mb link to profile%>
                    </p>
                    <%}%>
                    <a href="<%="quest/"+questAuthor.getKey().replaceAll("\\s+", "-").toLowerCase()%>">Start quest</a>
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

