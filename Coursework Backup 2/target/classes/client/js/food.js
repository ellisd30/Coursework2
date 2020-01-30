function pageLoad() {
    checkLogin();
    foodEatenToday();
    document.getElementById("submitSearch").addEventListener("click", searchFood);
    document.getElementById("addFood").addEventListener("click", addFoodDiv);
    document.getElementById("listMyFoodItems").addEventListener("click", listMyFood);
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

function addFoodDiv(event) {
    event.preventDefault();
    let addFoodHTML = '<form id="addFoodForm">\n' +
        '              <h3>Add new food</h3>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodname">Food Name: </label>\n' +
        '                    <input style="margin-top: 12px;" type="text" name="foodname" id="foodname"> <!--name="foodname" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodgroup">Main Food Group: </label>\n' +
        '                    <select name="group">\n' +
        '                         <option value="Carbohydrate">Carbohydrate</option>\n' +
        '                         <option value="Dairy">Dairy</option>\n' +
        '                         <option value="Fat">Fat</option>\n' +
        '                         <option value="Protein">Protein</option>\n' +
        '                    </select> <!--name="group" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodgroup">Secondary Food Group: </label>\n' +
        '                    <select name="group2">\n' +
        '                         <option value=null>None</option>\n' +
        '                         <option value="Carbohydrate">Carbohydrate</option>\n' +
        '                         <option value="Dairy">Dairy</option>\n' +
        '                         <option value="Fat">Fat</option>\n' +
        '                         <option value="Protein">Protein</option>\n' +
        '                    </select> <!--name="group2" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="calories">Calories (kcal): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="calories" id="calories">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="brand">Brand: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="brand" id="brand">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="serving">Serving: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="serving" id="serving">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="totfat">Total Fat (g): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="totfat" id="totfat">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="satfat">Saturated Fat (g):</label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="satfat" id="satfat">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="cholesterol">Cholesterol: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="cholesterol" id="cholesterol">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="sodium">Sodium: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="sodium" id="sodium">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="totcarbs">Total Carbohydrates (g): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="totcarbs" id="totcarbs">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="fibre">Fibre: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="fibre" id="fibre">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="sugars">Sugars: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="sugars" id="sugars">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="protein">Protein: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="protein" id="protein">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <input type="submit" value="Add Food" id="addFoodButton">\n' +
        '                </div>\n' +
        '            </form>';
    document.getElementById("searchResults").innerHTML = addFoodHTML;
    document.getElementById("addFoodButton").addEventListener("click", addFood);
}

function addFood(event) {

    const form = document.getElementById("addFoodForm");
    const formData = new FormData(form);

    fetch("/food/add", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {

            alert("Added food successfully");
            console.log("Added food successfully");

            window.location.href = '/client/food.html';
        }
        console.log("here");
    });
}

function foodEatenToday() {
    console.log("FoodEaten");

    fetch("/users/listFoodToday", {method: 'get'}
    ).then(response => response.json()
    ).then(foods => {

        if (foods.hasOwnProperty('error')) {
            alert(foods.error);
        } else {
            let foodHTML = '';

            for (let food of foods) {
                foodHTML += '<div class="fooditem">\n' +
                    '                    <div class="foodname">\n' +
                    '                        ' + food.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                        ' + food.serving + ' Serving • Calories: ' + food.calories + ' • Brand: ' + food.brand +'\n' +
                    '                    </div>\n' +
                    '                    <div class="moredetails">\n' +
                    '                        Protein: ' + food.protein + 'g • Fat: ' + food.fat + 'g • Carbohydrates: ' + food.carbs + 'g\n' +
                    '                    </div>\n<br>' +
                                        `<button class='deletefromFoodEaten' data-id='${food.time}'>Delete Food Eaten</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("foodeatentoday").innerHTML = foodHTML;

            let deleteFoodButtons = document.getElementsByClassName("deletefromFoodEaten");
            for (let button of deleteFoodButtons) {
                button.addEventListener("click", deleteFoodEaten);
            }
        }
    });
}

function listMyFood() {
    console.log("My Food");
    event.preventDefault();

    fetch("/users/listUserFood", {method: 'get'}
    ).then(response => response.json()
    ).then(foods => {

        if (foods.hasOwnProperty('error')) {
            alert(foods.error);
        } else {
            let foodHTML = '';

            for (let food of foods) {
                foodHTML += '<div class="fooditem">\n' +
                    '                    <div class="foodname">\n' +
                    '                        ' + food.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                        ' + food.serving + ' Serving • Calories: ' + food.calories + ' • Brand: ' + food.brand +'\n' +
                    '                    </div>\n' +
                    '                    <div class="moredetails">\n' +
                    '                        Protein: ' + food.protein + 'g • Fat: ' + food.fat + 'g • Carbohydrates: ' + food.carbs + 'g\n' +
                    '                    </div>\n<br>' +
                    `<button class='addtoFoodEaten' data-id='${food.foodid}'>Add Food Eaten</button>` +
                    `<button class='editFoodItem' data-id='${food.foodid}'>Edit Food</button>` +
                    `<button class='deleteFoodItem' data-id='${food.foodid}'>Delete Food</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("searchResults").innerHTML = foodHTML;

            let addFoodButtons = document.getElementsByClassName("addtoFoodEaten");
            for (let button of addFoodButtons) {
                button.addEventListener("click", addFoodEaten);
            }
            let editFoodButtons = document.getElementsByClassName("editFoodItem");
            for (let button of editFoodButtons) {
                button.addEventListener("click", editFood);
            }
            let deleteFoodButtons = document.getElementsByClassName("deleteFoodItem");
            for (let button of deleteFoodButtons) {
                button.addEventListener("click", deleteFood);
            }
        }
    });
}

function deleteFood(event) {
    const id = event.target.getAttribute("data-id");
    let formData = new FormData();
    formData.append("foodid", id);
    console.log(formData);

    fetch('/food/delete', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert("Food deleted");
            pageLoad();
        }
    });
}

function editFood(event) {
    event.preventDefault();
    const id = event.target.getAttribute("data-id");

    let editFoodHTML = '<form id="editFoodForm">\n' +
        '              <h3>Edit food item</h3>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodname">Food Name: </label>\n' +
        '                    <input style="margin-top: 12px;" type="text" name="foodname" id="foodname"> <!--name="foodname" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodgroup">Main Food Group: </label>\n' +
        '                    <select id="group" name="group">\n' +
        '                         <option value="Carbohydrate">Carbohydrate</option>\n' +
        '                         <option value="Dairy">Dairy</option>\n' +
        '                         <option value="Fat">Fat</option>\n' +
        '                         <option value="Protein">Protein</option>\n' +
        '                    </select> <!--name="group" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="foodgroup">Secondary Food Group: </label>\n' +
        '                    <select id="group2" name="group2">\n' +
        '                         <option value=null>None</option>\n' +
        '                         <option value="Carbohydrate">Carbohydrate</option>\n' +
        '                         <option value="Dairy">Dairy</option>\n' +
        '                         <option value="Fat">Fat</option>\n' +
        '                         <option value="Protein">Protein</option>\n' +
        '                    </select> <!--name="group2" becomes an @FormDataParam -->\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="calories">Calories (kcal): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="calories" id="calories">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="brand">Brand: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="brand" id="brand">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="serving">Serving: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="serving" id="serving">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="totfat">Total Fat (g): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="totfat" id="totfat">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="satfat">Saturated Fat (g):</label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="satfat" id="satfat">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="cholesterol">Cholesterol: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="cholesterol" id="cholesterol">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="sodium">Sodium: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="sodium" id="sodium">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="totcarbs">Total Carbohydrates (g): </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="totcarbs" id="totcarbs">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="fibre">Fibre: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="fibre" id="fibre">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="sugars">Sugars: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="sugars" id="sugars">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        '                    <label style="margin-left:2px;" for="protein">Protein: </label>\n' +
        '                    <input style="margin-left:25px;" type="text" name="protein" id="protein">\n' +
        '                </div>\n' +
        '                <div class="addFoodDiv">\n' +
        `                <button id='saveEditFoodButton' data-id='${id}'>Save Food</button>` +
        '                </div>\n' +
        '            </form>';

    fetch('/food/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(food => {

        if (food.hasOwnProperty('error')) {
            alert(food.error);
        } else {

            document.getElementById("searchResults").innerHTML = editFoodHTML;

            document.getElementById("foodname").value = food.name;
            document.getElementById("group").value = food.foodGroup;
            document.getElementById("group2").value = food.foodGroup2;
            document.getElementById("calories").value = food.calories;
            document.getElementById("brand").value = food.brand;
            document.getElementById("serving").value = food.serving;
            document.getElementById("totfat").value = food.fat;
            document.getElementById("satfat").value = food.saturatedfat;
            document.getElementById("cholesterol").value = food.cholesterol;
            document.getElementById("sodium").value = food.sodium;
            document.getElementById("totcarbs").value = food.carbs;
            document.getElementById("fibre").value = food.fibre;
            document.getElementById("sugars").value = food.sugars;
            document.getElementById("protein").value = food.protein;

            document.getElementById("saveEditFoodButton").addEventListener("click", saveEditFood);

        }

    });
}

function saveEditFood(event) {
    const id = event.target.getAttribute("data-id");
    const form = document.getElementById("editFoodForm");
    const formData = new FormData(form);
    formData.append("id", id);
    console.log(formData);

    fetch('/food/update', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert("Food updated");
            pageLoad();
        }
    });
}

function deleteFoodEaten(event){

    let time = event.target.getAttribute("data-id");
    let formData = new FormData();
    formData.append("time", time);
    console.log(formData);

    fetch('/users/deleteFoodEaten', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            } else {
                console.log("Food deleted");
                pageLoad();
            }
        }
    );
    pageLoad();
}


function searchFood(event) {
    console.log("SearchFood");
    event.preventDefault();

    const form = document.getElementById("searchFood");
    const formData = new FormData(form);

    fetch("/food/listSearch", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(foods => {

        if (foods.hasOwnProperty('error')) {
            alert(foods.error);
        } else {
            let foodHTML = '';
            let number = 0;


            for (let food of foods) {
                foodHTML += '<form id="food">' +
                    '                    <div class="fooditem">\n' +
                    '                    <div class="foodname">\n' +
                    '                        ' + food.name + '<hr>\n' +
                    '                    </div>\n' +
                    '                    <div class="simpledetails">\n' +
                    '                        ' + food.serving + ' Serving • Calories: ' + food.calories + ' • Brand: ' + food.brand +'\n' +
                    '                    </div>\n' +
                    '                    <div class="moredetails">\n' +
                    '                        Protein: ' + food.protein + 'g • Fat: ' + food.fat + 'g • Carbohydrates: ' + food.carbs + 'g\n' +
                    '                    </div>\n<br>' +
                                         `<button class='addtoFoodEaten' data-id='${food.foodid}'>Add Food Eaten</button>` +
                    '                </div>' +
                    '                </form>';
            }
            document.getElementById("searchResults").innerHTML = foodHTML;

            let addFoodButtons = document.getElementsByClassName("addtoFoodEaten");
            for (let button of addFoodButtons) {
                button.addEventListener("click", addFoodEaten);
            }
        }
    });
}

function addFoodEaten(event){

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("foodid", id);
        console.log(formData);

        fetch('/users/addFoodEaten', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    console.log("Food added");
                }
            }
        );
}
