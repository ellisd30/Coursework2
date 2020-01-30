function pageLoad() {
    let now = new Date();

    let myHTML = '<div style="text-align:center;">'
        + '<h1 style="font-size: 50px">The number 1 FREE fitness tool</h1>'
       + '<h3 style="font-size: 40px">Join today</h3>';



    document.getElementById("insideTextHome").innerHTML = myHTML;
    checkLogin();
}
function checkLogin() {

    let username = Cookies.get("username");
    let admin = Cookies.get("admin");
    console.log(admin);

    let logInHTML = '';

    let menuHTML = '';

    if (username === undefined) {

        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.style.visibility = "hidden";
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.style.visibility = "hidden";
        }

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a> or <a href='/client/signup.html'>Sign up</a>";
        menuHTML = "<a href=\"index.html\">Home</a>";
    } else {
        if (admin === true) {
            menuHTML = "<a href=\"index.html\">Home</a>\n" +
                "            <a href=\"profile.html\">My Profile</a>\n" +
                "            <a href=\"food.html\">Food Database</a>\n" +
                "            <a href=\"exercise.html\">Exercise Database</a>\n";
            logInHTML = "<div id='adminbutton'>" +
                "<button id=\"accessAdmin\">Admin</button>" +
                "</div>\n" +
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
