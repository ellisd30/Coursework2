function pageLoad() {
    checkLogin();
    exerciseDoneToday();
    document.getElementById("submitSearch").addEventListener("click", searchExercises);
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

function exerciseDoneToday() {
    console.log("ExerciseDoneToday");

    fetch("/users/exerciseDoneToday", {method: 'get'}
    ).then(response => response.json()
    ).then(exercises => {

        if (exercises.hasOwnProperty('error')) {
            alert(exercises.error);
        } else {
            let exerciseHTML = '';

            for (let exercise of exercises) {
                exerciseHTML += '<div class="exerciseitem">\n' +
                    '                    <div class="exercisename">\n' +
                    '                        ' + exercise.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                      Type: ' + exercise.type + ' • METs: ' + exercise.mets +'\n' +
                    '                    </div>' +
                    '                    <div class="timespent">\n' +
                    '                    Time Spent (minutes): ' + exercise.timespent + '\n' +
                    '                    </div>\n' +
                    `                    <button class='deletefromExerciseDone' data-id='${exercise.time}'>Delete Exercise Done</button>` +
                    '                    </div>';
            }
            document.getElementById("exercisedonetoday").innerHTML = exerciseHTML;

            let deleteExerciseButtons = document.getElementsByClassName("deletefromExerciseDone");
            for (let button of deleteExerciseButtons) {
                button.addEventListener("click", deleteExerciseDone);
            }
        }
    });
}

function deleteExerciseDone(event) {
    let time = event.target.getAttribute("data-id");
    let formData = new FormData();
    formData.append("time", time);
    console.log(formData);

    fetch('/users/deleteExerciseDone', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                console.log("Exercise Done removed");
                pageLoad();
                window.location.href = '/client/exercise.html';
            }
        }
    );
    pageLoad();
    window.location.href = '/client/exercise.html';
}

function searchExercises(event) {
    console.log("SearchExercises");
    event.preventDefault();

    const form = document.getElementById("searchExercises");
    const formData = new FormData(form);

    fetch("/exercises/listSearch", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(exercises => {

        if (exercises.hasOwnProperty('error')) {
            alert(exercises.error);
        } else {
            let exerciseHTML = '';
            let number = 0;


            for (let exercise of exercises) {
                exerciseHTML += '<form id="exercises">' +
                    '                    <div class="exerciseitem">\n' +
                    '                    <div class="exercisename">\n' +
                    '                        ' + exercise.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                      Type: ' + exercise.type + ' • METs: ' + exercise.mets +'\n' +
                    '                    </div>' +
                    '                    <div class="timespent">\n' +
                    '                    <label style="margin-left:2px;" for="timespent">Time Spent (minutes): </label>\n' +
                    '                    <input style="margin-left:25px;" type="text" name="timespent" id="timespent">\n' +
                    '                    </div>\n' +
                    `<button class='addtoExerciseDone' data-id='${exercise.exerciseid}' data-id2='${exercise.mets}'>Add Exercise Done</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("searchResults").innerHTML = exerciseHTML;

            let addExerciseButtons = document.getElementsByClassName("addtoExerciseDone");
            for (let button of addExerciseButtons) {
                button.addEventListener("click", addExerciseDone);
            }
        }
    });
}

function addExerciseDone(event){
    const form = document.getElementById("exercises");
    let id = event.target.getAttribute("data-id");
    let mets = event.target.getAttribute("data-id2");
    let formData = new FormData(form);
    formData.append("exerciseid", id);
    formData.append("mets", mets);
    console.log(formData);

    fetch('/users/addExerciseDone', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                console.log("Exercise added");
            }
        }
    );
}