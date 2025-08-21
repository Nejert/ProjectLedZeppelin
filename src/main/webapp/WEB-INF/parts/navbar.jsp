<%@ page import="com.javarush.kazakov.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String requestURI = request.getRequestURI();
    String statActive = requestURI.contains("statistics")?"active":"";
    String createActive = requestURI.contains("create-quest")?"active":"";
%>
<nav class="navbar navbar-dark navbar-expand-md bg-dark py-3" style="border-bottom: 3px solid var(--bs-gray) ;">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/"><span
                class="bs-icon-sm bs-icon-rounded bs-icon-primary d-flex justify-content-center align-items-center me-2 bs-icon"><svg
                xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16"
                class="bi bi-question-square-fill">
                            <path d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm3.496 6.033a.237.237 0 0 1-.24-.247C5.35 4.091 6.737 3.5 8.005 3.5c1.396 0 2.672.73 2.672 2.24 0 1.08-.635 1.594-1.244 2.057-.737.559-1.01.768-1.01 1.486v.105a.25.25 0 0 1-.25.25h-.81a.25.25 0 0 1-.25-.246l-.004-.217c-.038-.927.495-1.498 1.168-1.987.59-.444.965-.736.965-1.371 0-.825-.628-1.168-1.314-1.168-.803 0-1.253.478-1.342 1.134-.018.137-.128.25-.266.25h-.825zm2.325 6.443c-.584 0-1.009-.394-1.009-.927 0-.552.425-.94 1.01-.94.609 0 1.028.388 1.028.94 0 .533-.42.927-1.029.927z"></path>
                        </svg></span><span>Quest</span></a>
        <button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-6"><span
                class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse flex-grow-0 order-md-first" id="navcol-6">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link <%=statActive%>" href="${pageContext.request.contextPath}/statistics">Statistics</a></li>
                <li class="nav-item"><a class="nav-link <%=createActive%>" href="${pageContext.request.contextPath}/create-quest">Create Quest</a></li>
            </ul>
            <div class="d-md-none my-2">
                <button class="btn btn-light me-2" type="button">Button</button>
                <button class="btn btn-primary" type="button">Button</button>
            </div>
        </div>

        <div class="d-flex align-items-center">
            <%
                Object userObj = session.getAttribute(Attr.USER);
                User user = null;
                if (userObj == null) {
            %>
            <a class="btn btn-light me-2" role="button" href="${pageContext.request.contextPath}/sign-in">SignIn</a>
            <a class="btn btn-primary" role="button" href="${pageContext.request.contextPath}/sign-up">SignUp</a>
            <%
            } else {
                user = (User) userObj;
            %>
            <div class="d-flex align-items-center" style="padding-right: 5px;">
                <img class="rounded-circle" width="32" height="32"
                     src="/images/<%=user.getImage()%>"/>
                <a href="${pageContext.request.contextPath}/profile">
                    <h5 class="fw-bold text-primary mb-0"><strong><%=user.getLogin()%>
                    </strong></h5>
                </a>
            </div>
            <form id="signOutNav" action="${pageContext.request.contextPath}/sign-out" method="post">
            <a class="btn btn-primary" role="button" href="#" onclick="document.getElementById('signOutNav').submit();" style="background: var(--bs-red);">SignOut</a>
            </form>
            <%
                }
            %>
        </div>
    </div>
</nav>