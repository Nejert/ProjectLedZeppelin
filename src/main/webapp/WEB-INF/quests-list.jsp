<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container text-light text-bg-dark py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>Quests</h2>
        </div>
    </div>
    <div class="row gy-4 row-cols-1 row-cols-md-2 row-cols-xl-3">
        <%
            Object quests = session.getAttribute("questsList");
            if (quests != null) {
                List<String> questsList = (List<String>) quests;
                for (int i = 0; i < questsList.size(); i++) {
        %>
        <div class="col">
            <div class="d-flex">
                <div class="px-3">
                    <h4><%=questsList.get(i)%>
                    </h4>
                    <a href="<%="quest/"+questsList.get(i).replaceAll("\\s+", "-").toLowerCase()%>">Start quest</a>
                </div>
            </div>
        </div>
        <%
                }
            }
        %>
    </div>
</div>

