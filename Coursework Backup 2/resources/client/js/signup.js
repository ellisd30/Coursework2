function pageLoad() {

        document.getElementById("signupButton").addEventListener("click", signup);

    checkLogin();
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

function signup(event) {

    event.preventDefault();

    const form = document.getElementById("signupForm");
    const formData = new FormData(form);

    fetch("/user/signup", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            alert("Signed up successfully");

            window.location.href = '/client/index.html';
        }
    });
}