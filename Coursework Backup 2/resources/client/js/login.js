function pageLoad() {

    if(window.location.search === '?logout') {
        document.getElementById('mainContent').innerHTML = '<h1 style="color: white">Logging out, please wait...</h1>';
        logout();
    } else {
        document.getElementById("loginButton").addEventListener("click", login);
    }
    checkLogin();
}

function checkLogin() {

    let username = Cookies.get("username");
    let admin = Cookies.get("admin");

    let logInHTML = '';

    let menuHTML = '';

    if (username === undefined) {

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a> or <a href='/client/signup.html'>Sign up</a>";
        menuHTML = "<a href=\"index.html\">Home</a>";
    } else {
        if (admin === true) {
            menuHTML = "<a href=\"index.html\">Home</a>\n" +
                "            <a href=\"profile.html\">My Profile</a>\n" +
                "            <a href=\"food.html\">Food Database</a>\n" +
                "            <a href=\"exercise.html\">Exercise Database</a>\n";
            logInHTML = "<button id=\"accessAdmin\" style=\"position: absolute; top: 530px; margin-left:750px;\">Admin</button> " +
                "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";
        } else {

            menuHTML = "<a href=\"index.html\">Home</a>\n" +
                "            <a href=\"profile.html\">My Profile</a>\n" +
                "            <a href=\"food.html\">Food Database</a>\n" +
                "            <a href=\"exercise.html\">Exercise Database</a>\n"
            logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";
        }
    }

    document.getElementById("dropdown-content").innerHTML = menuHTML;
    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function login(event) {

    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    fetch("/user/login", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            Cookies.set("username", responseData.username);
            Cookies.set("token", responseData.token);
            Cookies.set("admin", responseData.admin);

            window.location.href = '/client/index.html';
        }
    });
}
function logout() {

    fetch("/user/logout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("username");
            Cookies.remove("token");
            Cookies.remove("admin");

            window.location.href = '/client/index.html';

        }
    });

}
