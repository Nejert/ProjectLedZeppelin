<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="parts/header.jsp" %>
<div class="container py-4 py-xl-5">
    <div class="row mb-4 mb-lg-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>Profile page</h2>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <div class="card border-0 shadow-none">
                <div class="card-body text-center text-light text-bg-dark d-flex flex-column align-items-center p-0">
                    <img class="rounded-circle mb-3 fit-cover" width="130" height="130"
                         src="https://cdn.bootstrapstudio.io/placeholders/1400x800.png">
                    <h5 class="fw-bold text-primary card-title mb-0"><strong><%=((User)user).login()%></strong></h5>
                    <div class="d-flex flex-column">
                        <a id="changeLoginAnchor" href="#" onclick="changeLogin()">Change login</a>
                        <a id="changePasswordAnchor" href="#" onclick="changePassword()">Change password</a>
                        <a id="deleteProfile" href="#" onclick="deleteProfile()">Delete profile</a>
                        <a href="#" style="color: var(--bs-red);">SignOut</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="parts/footer.jsp" %>