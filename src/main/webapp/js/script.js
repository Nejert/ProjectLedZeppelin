function changeLogin() {
    const form = document.getElementById('changeForm');
    if (form) {
        form.remove();
    } else {
        const formString = `
        <form id="changeForm" class="d-flex" method="post">
            <input type="hidden" name="change" value="login"/>
            <input id="newLogin" name="newLogin" class="form-control" type="text" style="margin-right: 5px;" placeholder="New login" />
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
            <input type="hidden" name="change" value="password"/>
            <input id="oldPassword" name="oldPassword" class="form-control" type="password" placeholder="Old password" />
            <input id="newPassword" name="newPassword" class="form-control" type="password" placeholder="New password" />
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
            <input type="hidden" name="change" value="delete"/>
            <div class="alert alert-success" role="alert" style="background: var(--bs-orange);margin-bottom: 0px;padding: 7px;">
                <span><strong>Are you sure?</strong></span>
            <div>
                <button name="yes" class="btn btn-primary" type="submit" style="background: var(--bs-danger);margin-right: 7px;">Yes</button>
                <button name="no" class="btn btn-primary" type="submit">No</button>
            </div>
        </div>
        </form>`;
        let deleteProfileAnchor = document.getElementById('deleteProfile');
        deleteProfileAnchor.insertAdjacentHTML('afterend', alertString);
    }
}

function loadImage() {
    let imageFile = document.getElementById('imageFile');
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