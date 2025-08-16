<%@ page import="com.javarush.kazakov.entity.Result" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp"%>
<%
    Object resultObj = session.getAttribute("result");
    Result result = null;
    if (resultObj != null) {
        result = (Result) resultObj;
    }
    boolean isVictory = result.isVictory();
    String bannerColor = isVictory ? "bg-success" : "bg-danger";
    String bannerText = isVictory ? "You succeed" : "You fail";
%>
<section class="py-4 py-xl-5">
    <div class="container">
        <div class="text-white <%=bannerColor%> border rounded border-0 d-flex flex-column justify-content-between flex-lg-row p-4 p-md-5">
            <div class="pb-2 pb-lg-1">
                <h2 class="fw-bold mb-2"><%=result.getText()%></h2>
                <p class="mb-0"><%=bannerText%></p>
            </div>
            <div class="my-2"><a class="btn btn-light fs-5 py-2 px-4" role="button" href="${pageContext.request.contextPath}/">Home</a></div>
        </div>
    </div>
</section>
<%@include file="parts/footer.jsp"%>