<%@ page import="java.util.List" %>
<%@ page import="com.javarush.kazakov.dto.quest.QuestTo" %>
<%@ page import="com.javarush.kazakov.dto.quest.AnswerTo" %>
<%@include file="parts/header.jsp" %>
<%
    Object questObj = session.getAttribute(Attr.QUEST);
    QuestTo quest = null;
    if (questObj != null) {
        quest = (QuestTo) questObj;
    }
%>
<section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <h2><%=quest.title()%>
                </h2>
            </div>
        </div>
        <div class="row d-flex justify-content-center">
            <div class="col-5 d-xl-flex flex-column align-items-xl-start">
                <h4><%=quest.currentQuestion().title()%><br/></h4>
                <form method="post" class="d-xl-flex flex-column justify-content-xl-start align-items-xl-start">
                    <%
                        List<AnswerTo> answers = quest.currentQuestion().answers();
                        for (int i = 0; i < answers.size(); i++) {
                    %>
                    <div class="form-check d-xl-flex justify-content-xl-center align-items-xl-center">
                        <input id="answer-<%=i%>" name="answer" class="form-check-input" type="radio" value="<%=i%>" style="margin-right: 4px;"/>
                        <label class="form-check-label fs-5" for="answer-<%=i%>"><%=answers.get(i).title()%>
                        </label>
                    </div>
                    <%}%>
                    <button class="btn btn-primary" type="submit" style="margin-top: 7px;">Submit</button>
                </form>
            </div>
        </div>
    </div>
</section>
<%@include file="parts/footer.jsp" %>