<%@ page import="com.javarush.kazakov.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/header.jsp"%>
<%--<%--%>
<%--    String userName = "Guest";--%>
<%--    User user = (User) session.getAttribute("user");--%>
<%--    if(user != null) {--%>
<%--        userName = user.getLogin();--%>
<%--    }--%>
<%--%>--%>
<%--<h3>You logged in as: <%=userName%></h3>--%>
<%--<div>--%>
<%--    <a href="/quest">Quests</a>--%>
<%--</div>--%>
<%--<div>--%>
<%--    <a href="/login">Login</a>--%>
<%--</div>--%>
<%@include file="quests-list.jsp"%>
<%@include file="parts/footer.jsp"%>
