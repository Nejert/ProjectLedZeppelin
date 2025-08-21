<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp" %>
<h1 class="text-center text-light">Create quest</h1>
<div class="container d-flex flex-column justify-content-center align-items-center justify-content-xl-center">
    <a id="template" href="#">load template</a>
    <ul id="root">
    <%--Generated content--%>
    </ul>
    <button id="confirm" class="btn btn-primary bg-danger" type="button">Confirm</button>
</div>
<%--<script type="module" src="${pageContext.request.contextPath}/js/param.js"></script>--%>
<script type="module" src="${pageContext.request.contextPath}/js/create.js"></script>
<%@include file="parts/footer.jsp" %>
