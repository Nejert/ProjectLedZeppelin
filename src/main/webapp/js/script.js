import * as Param from "./param.js";

function changeAvatar() {
    const form = document.getElementById('changeForm');
    if (form) {
        form.remove();
    } else {
        const formString = `
    <form id="changeForm" method="post" enctype="multipart/form-data">
        <input name="${Param.CHANGE_IMAGE}" class="form-control" type="file" accept="image/*" />
        <button class="btn btn-primary" type="submit">Apply</button>
    </form>
    `;
        let changeAvatarAnchor = document.getElementById('changeAvatarAnchor');
        changeAvatarAnchor.insertAdjacentHTML('afterend', formString);
    }
}

function changeLogin() {
    const form = document.getElementById('changeForm');
    if (form) {
        form.remove();
    } else {
        const formString = `
        <form id="changeForm" class="d-flex" method="post">
            <input type="hidden" name="${Param.CHANGE}" value="${Param.LOGIN}"/>
            <input id="${Param.NEW_LOGIN}" name="${Param.NEW_LOGIN}" class="form-control" type="text" style="margin-right: 5px;" placeholder="New login" />
            <button class="btn btn-primary" type="submit">Apply</button>
        </form>`;
        let changeLoginAnchor = document.getElementById('changeLoginAnchor');
        changeLoginAnchor.insertAdjacentHTML('afterend', formString);
    }
}

function changePassword() {
    const form = document.getElementById('changeForm');
    if (form) {
        form.remove();
    } else {
        const formString = `
        <form id="changeForm" method="post">
            <input type="hidden" name="${Param.CHANGE}" value="${Param.PASSWORD}"/>
            <input id="${Param.PASSWORD}" name="${Param.PASSWORD}" class="form-control" type="password" placeholder="Password" />
            <input id="${Param.NEW_PASSWORD}" name="${Param.NEW_PASSWORD}" class="form-control" type="password" placeholder="New password" />
            <button class="btn btn-primary" type="submit">Apply</button>
        </form>`;
        let changePasswordAnchor = document.getElementById('changePasswordAnchor');
        changePasswordAnchor.insertAdjacentHTML('afterend', formString);
    }
}

function deleteProfile() {
    const form = document.getElementById('changeForm');
    if (form) {
        form.remove();
    } else {
        const alertString = `
        <form id="changeForm" method="post">
            <input type="hidden" name="${Param.CHANGE}" value="${Param.DELETE}"/>
            <div class="alert alert-success" role="alert" style="background: var(--bs-orange);margin-bottom: 0px;padding: 7px;">
                <span><strong>Are you sure?</strong></span>
            <div>
                <button name="${Param.YES}" class="btn btn-primary" type="submit" style="background: var(--bs-danger);margin-right: 7px;">Yes</button>
                <button name="${Param.NO}" class="btn btn-primary" type="submit">No</button>
            </div>
        </div>
        </form>`;
        let deleteProfileAnchor = document.getElementById('deleteProfile');
        deleteProfileAnchor.insertAdjacentHTML('afterend', alertString);
    }
}

function loadImage() {
    let imageFile = document.getElementById(Param.IMAGE_FILE);
    imageFile.click();
    imageFile.onchange = (ev) => {
        let fileReader = new FileReader();
        fileReader.readAsDataURL(ev.target.files[0]);
        fileReader.onload = (event) => {
            document.getElementById('avatar').src = event.target.result;
        }
    }
}

function signOut() {
    fetch('/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'signOut=true'
    });
}

function setOnClick(elementId, func) {
    let element = document.getElementById(elementId);
    if (element !== null) {
        element.onclick = func;
    }
}

setOnClick("changeAvatarAnchor", changeAvatar);
setOnClick("changeLoginAnchor", changeLogin);
setOnClick("changePasswordAnchor", changePassword);
setOnClick("deleteProfile", deleteProfile);
setOnClick("avatar", loadImage);