function pageLoad() {
    fetch('/users/profile', {method: 'get'}
    ).then(response => response.json()
    ).then(users => {

        if (users.hasOwnProperty('error')) {
            alert(users.error);
        } else {

            document.getElementById("profileusername").innerHTML = users.username;
            document.getElementById("level").innerHTML = "Level: " + users.level;
            document.getElementById("profilefullname").innerHTML = users.firstname + ' ' + users.lastname;
            document.getElementById("box1").innerHTML = "<p>Current weight: " + users.currentweight + "kg</p>\n" +
                "        <p>Height: " + users.height + "cm</p>\n" +
                "        <p>Target weight: " + users.targetweight + "kg</p>\n" +
                "        <p>Goal time frame to reach target weight: " + users.timeframeweeks + " weeks</p>\n" +
                "        <p>Recommended calories per day: " + users.reccaloriesperday + "</p>\n"

        }
        document.getElementById("addMoodFeltButton").addEventListener("click", addMoodDiv);

        checkLogin();
        moodsFeltToday();

    });

    fetch('/users/totalcaloriesburnt', {method: 'get'}
    ).then(response => response.json()
    ).then(users => {

        if (users.hasOwnProperty('error')) {
            alert(users.error);
        } else {
            document.getElementById("calsburnt").innerHTML = users.total + "<br> Calories Burnt";
        }
    });

    fetch('/users/totalminutesexercise', {method: 'get'}
    ).then(response => response.json()
    ).then(users => {

        if (users.hasOwnProperty('error')) {
            alert(users.error);
        } else {
            document.getElementById("exercisedone").innerHTML = users.total + "<br> Minutes Spent Exercising";

        }

    });

}

function checkLogin() {

    let username = Cookies.get("username");

    let logInHTML = '';

    let menuHTML = '';

    if (username === undefined) {

        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a> or <a href='/client/signup.html'>Sign up</a>";
        menuHTML = "<a href=\"index.html\">Home</a>";
        window.location.href = '/client/index.html';

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


var i = 0;
function move() {
    if (i == 0) {
        i = 1;
        var elem = document.getElementById("myBar");
        var width = 1;
        var id = setInterval(frame, 10);
        function frame() {
            if (width >= 100) {
                clearInterval(id);
                i = 0;
            } else {
                width++;
                elem.style.width = width + "%";
            }
        }
    }
}

function moodsFeltToday() {
    console.log("MoodsFelt");

    fetch("/users/moodsFeltToday", {method: 'get'}
    ).then(response => response.json()
    ).then(moods => {

        if (moods.hasOwnProperty('error')) {
            alert(moods.error);
        } else {
            let moodHTML = '';

            for (let mood of moods) {
                moodHTML += '<div class="mooditem">\n' +
                    '                    <div class="moodname">\n' +
                    '                        ' + mood.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="mooddescription">\n' +
                    '                    Description: ' + mood.desc + '\n' +
                    '                    </div>\n' +
                    '                    <div class="moodreason">\n' +
                    '                        Reason: ' + mood.reason + '\n' +
                    '                    </div>\n' +
                    `<button class='deletefromMoodsFelt' style="margin-top:5px;" data-id='${mood.time}'>Delete Mood Felt</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("moodsfelttoday").innerHTML = moodHTML;

            let deleteMoodButtons = document.getElementsByClassName("deletefromMoodsFelt");
            for (let button of deleteMoodButtons) {
                button.addEventListener("click", deleteMoodFelt);
            }
        }
    });
}

function deleteMoodFelt(event){

    let time = event.target.getAttribute("data-id");
    let formData = new FormData();
    formData.append("time", time);
    console.log(formData);

    fetch('/users/deleteMoodFelt', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                console.log("Mood deleted");
                window.location.href = '/client/profile.html';
                pageLoad();
            }
        }
    );
    pageLoad();
}

function addMoodDiv(event) {
    event.preventDefault();
    let addMoodFeltHTML = '<form id="addMoodForm">\n' +
        '              <h3>Add new mood felt</h3>\n' +
        '                <div class="addMoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="moodid">Mood: </label>\n' +
        '                    <select name="moodid">\n' +
        '                         <option value="1">Disappointed</option>\n' +
        '                         <option value="2">Happy</option>\n' +
        '                         <option value="3">Sad</option>\n' +
        '                         <option value="4">Angry</option>\n' +
        '                         <option value="5">Annoyed</option>\n' +
        '                         <option value="6">Depressed</option>\n' +
        '                         <option value="7">Lonely</option>\n' +
        '                         <option value="8">Calm</option>\n' +
        '                         <option value="9">Cheerful</option>\n' +
        '                         <option value="10">Hopeful</option>\n' +
        '                         <option value="11">Fearful</option>\n' +
        '                         <option value="12">Anxious</option>\n' +
        '                         <option value="13">Excited</option>\n' +
        '                    </select> <!--name="moodid" becomes an @FormDataParam -->\n' +
        '                    </div><br>\n' +
        '                <div class="addMoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="reasonfelt">Reason: </label>\n' +
        '                    <input style="margin-top: 12px;" type="text" name="reasonfelt" id="reasonfelt"> <!--name="foodname" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addMoodDiv">\n' +
        '                    <input type="submit" value="Add Mood Felt" id="addMoodFelt">\n' +
        '                </div>\n' +
        '            </form>';
    document.getElementById("moodsfelttoday").innerHTML = addMoodFeltHTML;
    document.getElementById("addMoodFelt").addEventListener("click", addMoodFelt);
}

function addMoodFelt(event){

    let formData = new FormData(addMoodForm);
    console.log(formData);

    fetch('/users/addMoodFelt', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                console.log("Mood added");
            }
        }
    );
}

