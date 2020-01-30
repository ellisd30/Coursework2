function pageLoad() {
    checkLogin();
    document.getElementById("submitSearch").addEventListener("click", searchUsers);
}

function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    let menuHTML = '';

    if (username === undefined) {

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a> or <a href='/client/signup.html'>Sign up</a>";
        menuHTML = "<a href=\"index.html\">Home</a>";

    } else {

        menuHTML = "<a href=\"index.html\">Home</a>\n" +
            "            <a href=\"profile.html\">My Profile</a>\n" +
            "            <a href=\"food.html\">Food Database</a>\n" +
            "            <a href=\"exercise.html\">Exercise Database</a>\n"
        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";

    }

    document.getElementById("dropdown-content").innerHTML = menuHTML;
    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function searchUsers(event) {
    console.log("SearchUsers");
    event.preventDefault();

    const form = document.getElementById("searchUsers");
    const formData = new FormData(form);

    fetch("/admin/searchUsers", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(users => {

        if (users.hasOwnProperty('error')) {
            alert(users.error);
        } else {
            let usersHTML = '';
            let number = 0;


            for (let user of users) {
                usersHTML += '<form id="user">' +
                    '                    <div class="useritem">\n' +
                    '                    <div class="useridname">\n' +
                    '                       UserID: ' + user.userid + ' • Username: ' + user.username + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                     Full name: ' + user.firstname + ' ' + user.lastname + '\n' +
                    '                    </div>\n' +
                    '                    <div class="moredetails">\n' +
                    '                        Gender: ' + user.gender + '\n' +
                    '                    </div>\n<br>' +
                    `<button class='deleteuserbutton' data-id='${user.userid}'>Delete User</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("searchResults").innerHTML = usersHTML;

            let deleteUserButtons = document.getElementsByClassName("deleteuserbutton");
            for (let button of deleteUserButtons) {
                button.addEventListener("click", deleteUser);
            }
        }
    });
}