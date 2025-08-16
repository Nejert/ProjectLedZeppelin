<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="parts/header.jsp" %>
<h1 class="text-center text-light">Create quest</h1>
<%--<div class="container d-flex flex-column justify-content-center align-items-center justify-content-xl-center">--%>
<%--    <ul id="root">--%>
<%--                <li id="quest-name">--%>
<%--                    <label class="form-label">Quest name</label>--%>
<%--                    <input id="quest-name" class="form-control-sm" type="text"/>--%>
<%--                </li>--%>
<%--                <li id="question" class="question">--%>
<%--                    <label class="form-label border rounded border-0" style="background: var(--bs-blue);">Question</label>--%>
<%--                    <input id="question" class="form-control-sm" type="text"/>--%>
<%--                    <button class="btn btn-primary btn-sm" type="button" style="background: var(--bs-orange);">Add answer--%>
<%--                    </button>--%>
<%--                    <ul>--%>
<%--                        <li id="answer" class="answer">--%>
<%--                            <label class="form-label border rounded border-0"--%>
<%--                                   style="background: var(--bs-orange);">Answer</label>--%>
<%--                            <input id="answer" class="form-control-sm" type="text"/>--%>
<%--                            <button class="btn btn-primary btn-sm" type="button">Add next question</button>--%>
<%--                            <button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                            <ul>--%>
<%--                                <li id="result" class="result">--%>
<%--                                    <label class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label>--%>
<%--                                    <input id="result" class="form-control-sm" type="text"/>--%>
<%--                                    <input type="checkbox"/><label class="form-label">Victory</label>--%>
<%--                                </li>--%>
<%--                            </ul>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--    </ul>--%>
<%--    <button id="confirm" class="btn btn-primary bg-danger" type="button">Confirm</button>--%>
<%--</div>--%>

<div class="container d-flex flex-column justify-content-center align-items-center justify-content-xl-center">
    <ul id="root">
<%--        <li id="quest-name"><label class="form-label">Quest name</label><input id="quest-name" class="form-control-sm" type="text" value="JavaRush Quest" /></li>--%>
<%--        <li id="question" class="question"><label class="form-label border rounded border-0" style="background: var(--bs-blue);">Question</label><input id="question" class="form-control-sm" type="text" value="Ты потерял память. Принять вызов НЛО?" /><button class="btn btn-primary btn-sm" type="button" style="background: var(--bs-orange);">Add answer</button>--%>
<%--            <ul>--%>
<%--                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Отклонить вызов" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                    <ul>--%>
<%--                        <li id="result" class="result"><label class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label><input id="result-text" class="form-control-sm" type="text" value="Ты отклонил вызов. Поражение" /><input type="checkbox" /><label class="form-label">Victory</label></li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Принять вызов" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                    <ul>--%>
<%--                        <li id="question" class="question"><label class="form-label border rounded border-0" style="background: var(--bs-blue);">Question</label><input id="quest-name" class="form-control-sm" type="text" value="Ты принял вызов. Поднимаешься на мостик к капитану?" /><button class="btn btn-primary btn-sm" type="button" style="background: var(--bs-orange);">Add answer</button>--%>
<%--                            <ul>--%>
<%--                                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Отказаться подниматься на мостик" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                                    <ul>--%>
<%--                                        <li id="result" class="result"><label class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label><input id="result" class="form-control-sm" type="text" value="Ты не пошел на переговоры. Поражение" /><input type="checkbox" /><label class="form-label">Victory</label></li>--%>
<%--                                    </ul>--%>
<%--                                </li>--%>
<%--                                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Подняться на мостик" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                                    <ul>--%>
<%--                                        <li id="question" class="question"><label class="form-label border rounded border-0" style="background: var(--bs-blue);">Question</label><input id="question" class="form-control-sm" type="text" value="Ты поднялся на мостик. Ты кто?" /><button class="btn btn-primary btn-sm" type="button" style="background: var(--bs-orange);">Add answer</button>--%>
<%--                                            <ul>--%>
<%--                                                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Солгать о себе" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                                                    <ul>--%>
<%--                                                        <li id="result" class="result"><label class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label><input id="result" class="form-control-sm" type="text" value="Твою ложь разоблачили. Поражение" /><input type="checkbox" /><label class="form-label">Victory</label></li>--%>
<%--                                                    </ul>--%>
<%--                                                </li>--%>
<%--                                                <li id="answer" class="answer"><label class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label><input id="answer" class="form-control-sm" type="text" value="Рассказать правду о себе" /><button class="btn btn-primary btn-sm" type="button">Add next question</button><button class="btn btn-success btn-sm" type="button">Add result</button>--%>
<%--                                                    <ul>--%>
<%--                                                        <li id="result" class="result"><label class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label><input id="result" class="form-control-sm" type="text" value="Тебя вернули домой. Победа" /><input type="checkbox" checked /><label class="form-label">Victory</label></li>--%>
<%--                                                    </ul>--%>
<%--                                                </li>--%>
<%--                                            </ul>--%>
<%--                                        </li>--%>
<%--                                    </ul>--%>
<%--                                </li>--%>
<%--                            </ul>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </li>--%>
    </ul><button id="confirm" class="btn btn-primary bg-danger" type="button">Confirm</button>
</div>
<script src="${pageContext.request.contextPath}/js/create.js"></script>
<%@include file="parts/footer.jsp" %>
