package Controllers;


import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Scanner;
import java.util.UUID;

@Path("user/")
public class Users {

    @POST
    @Path("signup")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String signupUser(@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("emailaddress") String emailaddress, @FormDataParam("firstname") String firstname,
                             @FormDataParam("lastname") String lastname, @FormDataParam("dateofbirth") String dateofbirth, @FormDataParam("gender") String gender, @FormDataParam("heightincm") double height,
                             @FormDataParam("weightinkg") double weight, @FormDataParam("targetweight") double targetweight, @FormDataParam("timeframeweeks") Integer timeframe,
                             @FormDataParam("activitylevel") Integer activitylevel) {

        try {

            System.out.println("user/signup");

            PreparedStatement ps1 = Main.db.prepareStatement("INSERT INTO UserInfo (username, password, emailaddress, firstname, lastname, dateofbirth, gender, heightincm, weightinkg, targetweight, timeframeweeks, activitylevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps1.setString(1, username);
            ps1.setString(2, password);
            ps1.setString(3, emailaddress);
            ps1.setString(4, firstname);
            ps1.setString(5, lastname);
            ps1.setString(6, dateofbirth);
            ps1.setString(7, gender);
            ps1.setDouble(8, height);
            ps1.setDouble(9, weight);
            ps1.setDouble(10, targetweight);
            ps1.setInt(11, timeframe);
            ps1.setInt(12, activitylevel);
            ps1.executeUpdate();

            addRecCals(username);



            return "{User created! user/signup}";

        } catch (Exception exception){
            System.out.println("Database error during /user/signup: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    public static void addRecCals(String username) {
        try {
            int recCals = UserInfo.calculateRecCals(username);
            PreparedStatement ps1 = Main.db.prepareStatement("UPDATE UserInfo SET RecCaloriesPerDay = ? WHERE Username = ?");

            ps1.setInt(1, recCals);
            ps1.setString(2, username);
            ps1.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }


    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {

        try {

            System.out.println("user/login");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password, Admin FROM UserInfo WHERE Username = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();

            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);
                Boolean admin = loginResults.getBoolean(2);
                if (password.equals(correctPassword)) {

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE UserInfo SET Token = ? WHERE Username = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    userDetails.put("admin", admin);
                    return userDetails.toString();

                } else {
                    return "{\"error\": \"Incorrect password!\"}";
                }

            } else {
                return "{\"error\": \"Unknown user!\"}";
            }

        } catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }
    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE UserInfo SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error with token: " + exception.getMessage());
            return false;
        }
    }

    public static void updateWeight(int UserID) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("New weight: ");
            double newWeight = input.nextDouble();

            java.util.Date today = new java.util.Date();
            System.out.println(new java.sql.Time(today.getTime()));
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());
            System.out.println(Date);

            LocalTime vartime = LocalTime.now();
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO UserWeight (UserID, Weight, Date, Time) VALUES (?, ?, ?, ?) ");

            ps.setInt(1, UserID);
            ps.setDouble(2, newWeight);
            ps.setString(3, String.valueOf(Date));
            ps.setString(4, String.valueOf((new Time(today.getTime()))));
            ps.execute();

            setUserInfoWeight(UserID, newWeight);

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void setUserInfoWeight(int UserID, double newWeight) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE UserInfo SET WeightInKg = ? WHERE UserID = ?");

            ps.setDouble(1, newWeight);
            ps.setInt(2, UserID);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}

