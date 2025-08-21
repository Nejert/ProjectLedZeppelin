import * as Param from "./param.js";

function getQuestNode(value = "") {
    let li = document.createElement('li');
    li.id = "quest-name";
    li.innerHTML = `
        <label class="form-label">Quest name</label>
        <input id="quest-name-input" class="form-control-sm" type="text"/>
    `;
    li.querySelector("#quest-name-input").value = value;
    return li;
}

function getQuestionNode(value = "", children = []) {
    let li = document.createElement('li');
    li.id = "question";
    li.className = "question";
    li.innerHTML = `
        <label id="question-label" class="form-label border rounded border-0" style="background: var(--bs-blue);">Question</label>
        <input id="question-input" class="form-control-sm" type="text" />
        <button id="add-answer" class="btn btn-primary btn-sm" type="button" style="background: var(--bs-orange);">Add answer</button>
        <ul></ul>
    `;
    li.children["add-answer"].onclick = () => {
        addAnswer(li)
    };
    li.querySelector("#question-input").value = value;
    if (children !== null) {
        let questionUl = li.querySelector("ul");
        children.forEach(e => questionUl.append(e));
    }
    return li;
}

function getAnswerNode(value = "", child = null) {
    let li = document.createElement('li');
    li.id = "answer";
    li.className = "answer";
    li.innerHTML = `
        <label id="answer-label" class="form-label border rounded border-0" style="background: var(--bs-orange);">Answer</label>
        <input id="answer-input" class="form-control-sm" type="text" />        
        <ul></ul>
    `;
    let input = li.querySelector("#answer-input");
    input.value = value;
    if (child !== null) {
        if (child.id === "question") {
            input.after(getAddResultButton(li));
        } else if (child.id === "result") {
            input.after(getAddQuestionButton(li));
        }
        let answerUl = li.querySelector("ul");
        answerUl.append(child);
    } else {
        input.after(getAddQuestionButton(li));
        input.after(getAddResultButton(li));
    }
    return li;
}

function getAddQuestionButton(li) {
    let button = document.createElement('button');
    button.id = "add-question";
    button.className = "btn btn-primary btn-sm";
    button.type = "button";
    button.textContent = "Add next question";
    button.onclick = () => {
        addQuestion(li)
    };
    return button;
}

function getAddResultButton(li) {
    let button = document.createElement('button');
    button.id = "add-result";
    button.className = "btn btn-success btn-sm";
    button.type = "button";
    button.textContent = "Add result";
    button.onclick = () => {
        addResult(li)
    };
    return button;
}

function getResultNode(value = "", checked = false) {
    let li = document.createElement('li');
    li.id = "result";
    li.className = "result";
    li.innerHTML = `
        <label id="result-label" class="form-label border rounded border-0" style="background: var(--bs-green);">Result</label>
        <input id="result-input" class="form-control-sm" type="text" />
        <input id="result-checkbox" type="checkbox" /><label class="form-label">Victory</label>
    `;
    li.querySelector("#result-input").value = value;
    li.querySelector("#result-checkbox").checked = checked;

    return li;
}

function addAnswer(questionNode) {
    let ul = questionNode.querySelector("ul");
    ul.append(getAnswerNode());
}

function addQuestion(answerNode) {
    let ul = answerNode.querySelector("ul");
    if (ul.children.length !== 0) {
        answerNode.querySelector("#answer-input").after(getAddResultButton(answerNode));
        ul.remove();
        ul = answerNode.appendChild(document.createElement('ul'));
    }
    ul.append(getQuestionNode());
    answerNode.querySelector("#add-question").remove();
}

function addResult(answerNode) {
    let ul = answerNode.querySelector("ul");
    if (ul.children.length !== 0) {
        answerNode.querySelector("#answer-input").after(getAddQuestionButton(answerNode));
        ul.remove();
        ul = answerNode.appendChild(document.createElement('ul'));
    }
    ul.append(getResultNode());
    answerNode.querySelector("#add-result").remove();
}

function gatherData() {
    let root = document.getElementById("root");
    let quest = new Quest();
    quest.questName = root.querySelector("#quest-name").querySelector("#quest-name-input").value;
    quest.currentQuestion = getQuestion(root.querySelector("#question"));
    sendQuest(quest);
}

function sendQuest(quest) {
    fetch("/create-quest", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(quest),
        redirect: 'manual'
    }).then(() => window.location.replace("/"))
        .catch(error => console.error('Error:', error));
}

function getQuestion(questionNode) {
    let question = new Question();
    question.text = questionNode.querySelector("#question-input").value;
    question.answers = getAnswers(questionNode.querySelector("ul").children);
    return question;
}

function getAnswers(answerNodes) {
    let answers = [];
    for (const answerNode of answerNodes) {
        let answer = new Answer();
        answer.text = answerNode.querySelector("#answer-input").value;
        let answerNodeULElement = answerNode.querySelector("ul").firstChild;
        if (answerNodeULElement.id === "question") {
            answer.nextQuestion = getQuestion(answerNodeULElement);
            answer.endResult = null;
        } else if (answerNodeULElement.id === "result") {
            answer.endResult = getResult(answerNodeULElement);
            answer.nextQuestion = null;
        }
        answers.push(answer);
    }
    return answers;
}

function getResult(resultNode) {
    let result = new Result();
    result.text = resultNode.querySelector("#result-input").value;
    result.victory = resultNode.querySelector("#result-checkbox").checked;
    return result;
}

class Quest {
    questName;
    currentQuestion;
}

class Question {
    text;
    answers = [];
}

class Answer {
    text;
    nextQuestion;
    endResult;
}

class Result {
    text;
    victory;
}

function initTest() {
    let root = document.getElementById("root");
    let questNode = getQuestNode("JavaRush Quest EN");
    root.append(questNode);
    let firstQuestion = getQuestionNode("You've lost your memory. Do you accept the UFO call?", [
        getAnswerNode("Reject the call", getResultNode("You rejected incoming call. Defeat.")),
        getAnswerNode("Accept the call", getQuestionNode("You accepted incoming call. Are you going up to the bridge to see the captain?", [
            getAnswerNode("Refuse to go up to the bridge", getResultNode("You didn't negotiate. Defeat.")),
            getAnswerNode("Climb up to the bridge", getQuestionNode("You went up to the bridge. Who are you?", [
                getAnswerNode("Lie about yourself", getResultNode("Your lies have been exposed. Defeat.")),
                getAnswerNode("Tell the truth about yourself", getResultNode("You were brought home. Victory.", true))
            ]))
        ]))
    ]);
    root.append(firstQuestion);
}

function init() {
    let root = document.getElementById("root");
    root.append(getQuestNode());
    root.append(getQuestionNode());
}

//initTest();
init();
let confirmButton = document.getElementById("confirm");
confirmButton.onclick = gatherData;
