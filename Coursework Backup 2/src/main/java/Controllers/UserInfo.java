package Controllers;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Scanner;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("users/")
public class UserInfo {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listUsers(@CookieParam("token") String token) {
        /*if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }*/
        System.out.println("users/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, FirstName, LastName FROM UserInfo");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("FirstName", results.getString(3));
                item.put("LastName", results.getString(4));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("profile")
    @Produces(MediaType.APPLICATION_JSON)
    public String userProfile(@CookieParam("token") String token) {
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("users/profile");
        //JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Username, FirstName, LastName, Level, ImageLink, WeightInKG, HeightInCM, TargetWeight, RecCaloriesPerDay, TimeFrameWeeks FROM UserInfo WHERE token = ?");
            ps.setString(1, token);
            ps.execute();
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("username", results.getString(1));
                item.put("firstname", results.getString(2));
                item.put("lastname", results.getString(3));
                item.put("level", results.getString(4));
                item.put("imagelink", results.getString(5));
                item.put("currentweight", results.getDouble(6));
                item.put("height", results.getDouble(7));
                item.put("targetweight", results.getDouble(8));
                item.put("reccaloriesperday", results.getInt(9));
                item.put("timeframeweeks", results.getInt(10));
                return item.toString();
            } else {
                return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
            }
            //return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("totalcaloriesburnt")
    @Produces(MediaType.APPLICATION_JSON)
    public String totalCaloriesBurnt(@CookieParam("token") String token) {
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("users/totalcaloriesburnt");
        try {
            int UserID = getUserID(token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT TotalCalories FROM ExerciseDone WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            int total = 0;
            while (results.next()) {
                int number = results.getInt(1);
                total = total + number;
            }
            JSONObject item = new JSONObject();
            item.put("total", total);
            return item.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Error, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("totalminutesexercise")
    @Produces(MediaType.APPLICATION_JSON)
    public String totalMinutesExercise(@CookieParam("token") String token) {
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("users/totalminutesexercise");
        try {
            int UserID = getUserID(token);
            PreparedStatement ps = Main.db.prepareStatement("SELECT TimeSpent FROM ExerciseDone WHERE UserID = ?");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            int total = 0;
            while (results.next()) {
                int number = results.getInt(1);
                total = total + number;
            }
            JSONObject item = new JSONObject();
            item.put("total", total);
            return item.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Error, please see server console for more info.\"}";
        }
    }

    public static int getUserID(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps.setString(1, token);
            ResultSet results = ps.executeQuery();
            int UserID = 0;
            if (results.next()) {
                UserID = results.getInt(1);
            }
            return UserID;
        } catch (Exception exception) {
            return 0;
        }
    }


    @GET
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@CookieParam("token") String token) throws Exception {
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("users/delete/");
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM UserInfo WHERE token = ?");

            ps.setString(1, token);
            ps.execute();
            return "{User deleted from the database.}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    public static void addUser() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Username: ");
            String Username = input.nextLine();
            System.out.println("Password: ");
            String Password = input.nextLine();
            System.out.println("Email: ");
            String Email = input.nextLine();
            System.out.println("First Name: ");
            String FirstName = input.nextLine();
            System.out.println("Last Name: ");
            String LastName = input.nextLine();
            System.out.println("Date of Birth: ");
            String DOB = input.nextLine();
            System.out.println("Gender: ");
            String Gender = input.nextLine();
            System.out.println("Height in CM: ");
            int Height = input.nextInt();
            System.out.println("Weight in KG: ");
            int Weight = input.nextInt();
            System.out.println("Target weight: ");
            int TargetWeight = input.nextInt();

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO UserInfo (Username, Password, EmailAddress, FirstName, LastName, DateOfBirth, Gender, HeightInCM, WeightInKG, TargetWeight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, Username);
            ps.setString(2, Password);
            ps.setString(3, Email);
            ps.setString(4, FirstName);
            ps.setString(5, LastName);
            ps.setString(6, DOB);
            ps.setString(7, Gender);
            ps.setInt(8, Height);
            ps.setInt(9, Weight);
            ps.setInt(10,TargetWeight);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static int calculateRecCals(String username) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT HeightInCM, WeightInKG, TargetWeight, TimeFrameWeeks, Gender, ActivityLevel FROM UserInfo WHERE Username = ?");
            ps.setString(1, username);
            ResultSet results = ps.executeQuery();
            double height = 0;
            double weight = 0;
            double targetweight = 0;
            int timeframeweeks = 0;
            String gender = "";
            int activitylevel = 0;
            int wholecaloriesperday = 0;

            if (results.next()) {
                height = results.getDouble(1);
                weight = results.getDouble(2);
                targetweight = results.getDouble(3);
                timeframeweeks = results.getInt(4);
                gender = results.getString(5);
                activitylevel = results.getInt(6);
            }

            int age = calculateAge(username);

            if (gender.equals("Male")) {
                double doubleBMR = 0;
                if (activitylevel == 1) {
                    doubleBMR = ((66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age))*1.2); //Can times this value by activity level to make it more accurate
                } else if (activitylevel == 2) {
                    doubleBMR = ((66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age))*1.375);
                } else if (activitylevel == 3) {
                    doubleBMR = ((66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age))*1.55);
                } else if (activitylevel == 4) {
                    doubleBMR = ((66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age))*1.725);
                } else {
                    doubleBMR = ((66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age))*1.9);
                }
                //Calculate weight wanting to lose
                double weighttolose = (weight-targetweight);
                double caloriestolose = (weighttolose*7700);

                //Calculate timeframeweeks * BMR
                int timeframedays = (timeframeweeks*7);
                double timeframecalories = (timeframedays*doubleBMR);

                //Calculate timeframecalories-caloriestolose /timeframedays
                double caloriesperday = ((timeframecalories-caloriestolose)/timeframedays);
                wholecaloriesperday = (int) caloriesperday;
            } else {
                double doubleBMR = 0;
                if (activitylevel == 1) {
                    doubleBMR = ((655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age))*1.2); ///Level 1 activity level
                } else if (activitylevel == 2) {
                    doubleBMR = ((655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age))*1.375);
                } else if (activitylevel == 3) {
                    doubleBMR = ((655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age))*1.55);
                } else if (activitylevel == 4) {
                    doubleBMR = ((655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age))*1.725);
                } else {
                    doubleBMR = ((655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age))*1.9);
                }

                //Calculate weight wanting to lose
                double weighttolose = (weight-targetweight);
                double caloriestolose = (weighttolose*7700);

                //Calculate timeframeweeks * BMR
                int timeframedays = (timeframeweeks*7);
                double timeframecalories = (timeframedays*doubleBMR);

                //Calculate timeframecalories-caloriestolose /timeframedays
                double caloriesperday = ((timeframecalories-caloriestolose)/timeframedays);
                wholecaloriesperday = (int) caloriesperday;
            }

            return wholecaloriesperday;


        } catch (Exception exception) {
            return 0;
        }

    }

    public static int calculateAge(String username) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT DateOfBirth FROM UserInfo WHERE Username = ?");
            ps.setString(1, username);
            ResultSet results = ps.executeQuery();
            String dob = "";
            if (results.next()) {
                 dob = results.getString(1);
            }

            String year = dob.substring(0, 4);
            String month = dob.substring(5, 7);
            String day = dob.substring(8, 10);
            int intday = Integer.parseInt(day);
            int intmonth = Integer.parseInt(month);
            int intyear = Integer.parseInt(year);

            //direct age calculation
            LocalDate l = LocalDate.of(intyear, intmonth, intday); //specify year, month, date directly
            LocalDate now = LocalDate.now(); //gets localDate
            Period diff = Period.between(l, now); //difference between the dates is calculated
            return diff.getYears();

        } catch (Exception exception) {
            System.out.println("Error");
            return 0;
        }
    }

    @POST
    @Path("/changePassword")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUserPassword(@CookieParam("token") String token){
        if (!Users.validToken(token)) { //Checks to see if the user is logged in; important when changing a password
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Old password: ");
            String OldPassword = input.nextLine();
            if (getOldPass(token, OldPassword)) { //If the results of getOldPass() are true, allow the user to set new password.
                PreparedStatement ps = Main.db.prepareStatement("UPDATE UserInfo SET Password = ? WHERE Token = ?");
                System.out.println("New Password: ");
                String NewPassword = input.nextLine();
                ps.setString(1, NewPassword);
                ps.setString(2, token);
                ps.executeUpdate();

                return "Password updated";
            } else {
                return "{\"error\": \"Incorrect password\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Error, please see server console for more info.\"}";
        }
    }

    public boolean getOldPass(String token, String OldPassword) throws SQLException {
        try {
            PreparedStatement correctoldpass = Main.db.prepareStatement("SELECT Password FROM UserInfo WHERE Token = ?");
            correctoldpass.setString(1, token);

            ResultSet results = correctoldpass.executeQuery();
            String correctpass = "";
            while (results.next()) {
                correctpass = results.getString(1);
            }

            return correctpass.equals(OldPassword);

        } catch (SQLException exception) {
            System.out.println("Database error: " + exception.getMessage());
            return Boolean.parseBoolean(null);
        }
    }

    @GET
    @Path("getuser/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUser(@PathParam("id") Integer id, @CookieParam("token") String token) throws Exception {
        if (!Users.validToken(token)) { //MAY NOT BE NEEDED HERE - USED TO CHECK IF THE USER IS LOGGED IN
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        if (id == null) {
            throw new Exception("User's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("users/getuser/" + id);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Username FROM UserInfo WHERE UserId = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("UserID", id);
                item.put("Username", results.getString(1));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

/*    public static void calculateRecCals(String username){
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Gender, HeightInCM, WeightInKG, TargetWeight, TimeFrameWeeks, DateOfBirth FROM UserInfo WHERE Username = ?");
            ps.setString(1, username);
            ResultSet results = ps.executeQuery();
            String gender = "";
            double height = 0;
            double weight = 0;
            double targetweight;
            double timeframeweeks;
            String dob;
            //java.util.Date today = new java.util.Date(); **Find out how to work out someone's birthday from their date of birth (Today - DOB)

            while (results.next()) {
                gender = results.getString("Gender");
                height = results.getDouble("HeightInCM");
                weight = results.getDouble("WeightInKG");
                targetweight = results.getDouble("TargetWeight");
                timeframeweeks = results.getInt("TimeFrameWeeks");
                dob = results.getString("DateOfBirth");
            }
            if (gender.equals("Male")) {
                int BMR = 66.47+(13.75*weight)+(5.003*height)-(6.755*age);
            } else {
                int BMR = 655.1+(9.563* weight) + (1.85*height)-(4.676*age);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }


    } */
@POST
@Path("addFoodEaten")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public static String addFoodEaten(@CookieParam("token") String token, @FormDataParam("foodid") int foodid){
    if (!Users.validToken(token)) {
        return "{\"error\": \"You don't appear to be logged in.\"}";
    }
    try {
        System.out.println("users/addFoodEaten");

        PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
        ps2.setString(1, token);
        ResultSet logoutResults = ps2.executeQuery();
        int userid = 0;
        if (logoutResults.next()) {
            userid = logoutResults.getInt(1);
        }

        PreparedStatement ps3 = Main.db.prepareStatement("SELECT Calories FROM Food WHERE FoodID = ?");
        ps3.setInt(1, foodid);
        ResultSet logoutResults2 = ps3.executeQuery();
        int calories = 0;
        if (logoutResults2.next()) {
            calories = logoutResults2.getInt(1);
        }
        System.out.println(calories);

        System.out.println("users/addFoodEaten 2");

        java.util.Date today = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

        System.out.println("foodid" + foodid);
        System.out.println("userid" + userid);
        System.out.println(String.valueOf(Date));
        System.out.println(String.valueOf(new Time(today.getTime())));
        System.out.println(calories);

        PreparedStatement ps = Main.db.prepareStatement("INSERT INTO FoodEaten (FoodID, UserID, Date, Time, TotalCalories) VALUES (?, ?, ?, ?, ?)");

        ps.setInt(1, foodid);
        ps.setInt(2, userid);
        ps.setString(3, String.valueOf(Date));
        ps.setString(4, String.valueOf((new Time(today.getTime()))));
        ps.setInt(5, calories);
        ps.executeUpdate();

        return "{Food added! users/addFoodEaten}";

    } catch (Exception exception) {
        System.out.println("Database error: " + exception.getMessage());
        return "{\"error\": \"Server side error!\"}";
    }
}

    @POST
    @Path("deleteFoodEaten")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteFoodEaten(@CookieParam("token") String token, @FormDataParam("time") String time){
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            System.out.println("users/deleteFoodEaten");

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            System.out.println("time" + time);
            System.out.println("userid" + userid);
            System.out.println(String.valueOf(Date));

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM FoodEaten WHERE UserID = ? AND Time = ? AND Date = ?");

            ps.setInt(1, userid);
            ps.setString(2, time);
            ps.setString(3, String.valueOf(Date));
            ps.executeUpdate();

            return "{Food deleted! users/deleteFoodEaten}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @GET
    @Path("listFoodToday")
    @Produces(MediaType.APPLICATION_JSON)
    public String listFoodEaten(@CookieParam("token") String token) {
        System.out.println("users/listFoodToday");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }
            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            //Complicated SQL Join Statement - write about this
            PreparedStatement ps = Main.db.prepareStatement("SELECT FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving, Protein, TotalFat, TotalCarbs, Time FROM Food FD, FoodEaten FE WHERE FD.FoodID = FE.FoodID AND FE.UserID = ? AND Date = ?");
            ps.setInt(1, userid);
            ps.setString(2, String.valueOf(Date));

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("serving", results.getInt(6));
                item.put("calories", results.getInt(4));
                item.put("brand", results.getString(5));
                item.put("protein", results.getDouble(7));
                item.put("fat", results.getDouble(8));
                item.put("carbs", results.getDouble(9));
                item.put("time", results.getString(10));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list food, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("listUserFood")
    @Produces(MediaType.APPLICATION_JSON)
    public String listUserFood(@CookieParam("token") String token) {
        System.out.println("users/listUserFood");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            //Complicated SQL Join Statement - write about this
            PreparedStatement ps = Main.db.prepareStatement("SELECT FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving, Protein, TotalFat, TotalCarbs, FoodID FROM Food WHERE UserID = ?");
            ps.setInt(1, userid);

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("serving", results.getInt(6));
                item.put("calories", results.getInt(4));
                item.put("brand", results.getString(5));
                item.put("protein", results.getDouble(7));
                item.put("fat", results.getDouble(8));
                item.put("carbs", results.getDouble(9));
                item.put("foodid", results.getString(10));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list food, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("exerciseDoneToday")
    @Produces(MediaType.APPLICATION_JSON)
    public String listExerciseDoneToday(@CookieParam("token") String token) {
        System.out.println("users/exerciseDoneToday");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }
            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            //Complicated SQL Join Statement - write about this
            PreparedStatement ps = Main.db.prepareStatement("SELECT ExerciseName, ExerciseType, TotalCalories, TimeSpent, METs, Time FROM Exercises EE, ExerciseDone ED WHERE EE.ExerciseID = ED.ExerciseID AND ED.UserID = ? AND Date = ?");
            ps.setInt(1, userid);
            ps.setString(2, String.valueOf(Date));

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("type", results.getString(2));
                item.put("calories", results.getInt(3));
                item.put("timespent", results.getInt(4));
                item.put("mets", results.getDouble(5));
                item.put("time", results.getString(6));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list exercises, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("addExerciseDone")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String addExerciseDone(@CookieParam("token") String token, @FormDataParam("exerciseid") int exerciseid, @FormDataParam("timespent") int timespent, @FormDataParam("mets") int mets){
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            System.out.println("users/addExerciseDone");

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            int totalcaloriesburnt = calculateCaloriesBurnt(userid, timespent, mets);
            System.out.println(totalcaloriesburnt);

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            System.out.println("exerciseid" + exerciseid);
            System.out.println("userid" + userid);
            System.out.println(String.valueOf(Date));
            System.out.println(String.valueOf(new Time(today.getTime())));
            System.out.println(timespent);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO ExerciseDone (ExerciseID, UserID, Date, Time, TotalCalories, TimeSpent) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setInt(1, exerciseid);
            ps.setInt(2, userid);
            ps.setString(3, String.valueOf(Date));
            ps.setString(4, String.valueOf((new Time(today.getTime()))));
            ps.setInt(5, totalcaloriesburnt);
            ps.setInt(6, timespent);
            ps.executeUpdate();

            return "{Exercise added! users/addExerciseDone}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    public static int calculateCaloriesBurnt(int userid, int timespent, int mets) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT WeightInKG FROM UserInfo WHERE UserID = ?");
            ps.setInt(1, userid);
            ResultSet results = ps.executeQuery();
            double weight = 0;

            if (results.next()) {
                weight = results.getDouble(1);
            }

            double caloriesburnt = ((mets*3.5*weight)/200)*timespent;
            int wholecaloriesburnt = (int) caloriesburnt;

            return wholecaloriesburnt;

        } catch (Exception exception) {
            return 0;
        }
    }

    @POST
    @Path("deleteExerciseDone")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteExerciseDone(@CookieParam("token") String token, @FormDataParam("time") String time){
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            System.out.println("users/deleteExerciseDone");

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            System.out.println("time" + time);
            System.out.println("userid" + userid);
            System.out.println(String.valueOf(Date));

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM ExerciseDone WHERE UserID = ? AND Time = ? AND Date = ?");

            ps.setInt(1, userid);
            ps.setString(2, time);
            ps.setString(3, String.valueOf(Date));
            ps.executeUpdate();

            return "{Exercise removed! users/deleteExerciseDone}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @GET
    @Path("moodsFeltToday")
    @Produces(MediaType.APPLICATION_JSON)
    public String moodsFeltToday(@CookieParam("token") String token) {
        System.out.println("users/moodsFeltToday");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }
            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            //Complicated SQL Join Statement - write about this
            PreparedStatement ps = Main.db.prepareStatement("SELECT MoodName, MoodDesc, Reason, Time FROM Moods MS, MoodsFelt MF WHERE MS.MoodID = MF.MoodID AND MF.UserID = ? AND Date = ?");
            ps.setInt(1, userid);
            ps.setString(2, String.valueOf(Date));

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("desc", results.getString(2));
                item.put("reason", results.getString(3));
                item.put("time", results.getString(4));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list moods, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("addMoodFelt")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String addMoodFelt(@CookieParam("token") String token, @FormDataParam("moodid") int moodid, @FormDataParam("reasonfelt") String reason){
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            System.out.println("users/addMoodFelt");

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            System.out.println("moodid" + moodid);
            System.out.println("userid" + userid);
            System.out.println(String.valueOf(Date));
            System.out.println(String.valueOf(new Time(today.getTime())));
            System.out.println(reason);

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO MoodsFelt (MoodID, UserID, Date, Time, Reason) VALUES (?, ?, ?, ?, ?)");

            ps.setInt(1, moodid);
            ps.setInt(2, userid);
            ps.setString(3, String.valueOf(Date));
            ps.setString(4, String.valueOf((new Time(today.getTime()))));
            ps.setString(5, reason);
            ps.executeUpdate();

            return "{Mood added! users/addMoodFelt}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("deleteMoodFelt")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteMoodFelt(@CookieParam("token") String token, @FormDataParam("time") String time){
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            System.out.println("users/deleteMoodFelt");

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());

            System.out.println("time" + time);
            System.out.println("userid" + userid);
            System.out.println(String.valueOf(Date));

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM MoodsFelt WHERE UserID = ? AND Time = ? AND Date = ?");

            ps.setInt(1, userid);
            ps.setString(2, time);
            ps.setString(3, String.valueOf(Date));
            ps.executeUpdate();

            return "{Mood removed! users/deleteMoodFelt}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }
}
