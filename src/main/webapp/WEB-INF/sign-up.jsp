<%@ page import="com.javarush.kazakov.config.constants.Param" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp" %>
<section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <h2>Sign up</h2>
            </div>
        </div>
        <div class="row d-flex justify-content-center">
            <div class="col-md-6 col-xl-4">
                <div class="card text-light bg-secondary mb-5">
                    <div class="card-body d-flex flex-column align-items-center">
                        <form class="d-flex flex-column my-auto justify-content-xl-center align-items-xl-center"
                              method="post" enctype="multipart/form-data">
                            <img id="avatar" class="rounded-circle"
                                 src="${pageContext.request.contextPath}/images/no-image.png"
                                 alt="/images/no-image.png"
                                 width="150" height="150" style="cursor: pointer;">
                            <input id="<%=Param.IMAGE_FILE%>" name="<%=Param.IMAGE_FILE%>" class="form-control invisible" type="file"
                                   accept="image/*" style="height: 0;"/>
                            <div class="mb-3">
                                <input class="form-control" type="text" name="<%=Param.LOGIN%>" placeholder="Login">
                            </div>
                            <div class="mb-3">
                                <input class="form-control" type="password" name="<%=Param.PASSWORD%>" placeholder="Password">
                            </div>
                            <div class="mb-3">
                                <button class="btn btn-primary d-block w-100" type="submit" style="margin-right: 132px;">SignUp</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<%@include file="parts/footer.jsp" %>
