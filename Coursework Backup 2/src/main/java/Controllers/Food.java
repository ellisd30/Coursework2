package Controllers;

import java.sql.*;
import java.util.Scanner;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("food/")
public class Food {
    @POST
    @Path("add")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String addFood(@CookieParam("token") String token, @FormDataParam("foodname") String foodname, @FormDataParam("group") String group, @FormDataParam("group2") String group2, @FormDataParam("calories") Integer calories,
                                 @FormDataParam("brand") String brand, @FormDataParam("serving") Integer serving, @FormDataParam("totfat") double totfat, @FormDataParam("satfat") double satfat, @FormDataParam("cholesterol") double cholesterol,
                                 @FormDataParam("sodium") double sodium, @FormDataParam("totcarbs") double totcarbs, @FormDataParam("fibre") double fibre, @FormDataParam("sugars") double sugars, @FormDataParam("protein") double protein) {
        if (!Users.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {

            PreparedStatement ps2 = Main.db.prepareStatement("SELECT UserID FROM UserInfo WHERE Token = ?");
            ps2.setString(1, token);
            ResultSet logoutResults = ps2.executeQuery();
            int userid = 0;
            if (logoutResults.next()) {
                userid = logoutResults.getInt(1);
            }

            System.out.println("food/add");

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Food (FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving, TotalFat, SaturatedFat, CholesterolMG, SodiumMG, TotalCarbs, Fibre, Sugars, Protein, UserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps.setString(1, foodname);
            ps.setString(2, group);
            ps.setString(3, group2);
            ps.setInt(4, calories);
            ps.setString(5, brand);
            ps.setInt(6, serving);
            ps.setDouble(7, totfat);
            ps.setDouble(8, satfat);
            ps.setDouble(9, cholesterol);
            ps.setDouble(10, sodium);
            ps.setDouble(11, totcarbs);
            ps.setDouble(12, fibre);
            ps.setDouble(13, sugars);
            ps.setDouble(14, protein);
            ps.setInt(15, userid);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }


    public static void deleteFood(int FoodID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Food WHERE FoodID = ?");

            //ps.setInt(1, id); - sets the ID of the food to be deleted from the database
            //ps.execute(); - executes the preparedStatement

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFoodItem(@FormDataParam("foodid") int id) {
    try {
        PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Food WHERE FoodID = ?");
        ps.setInt(1, id);
        ps.execute();

        return "{\"status\": \"OK\"}";

    } catch (Exception exception) {
        System.out.println("Database error: " + exception.getMessage());
        return "{\"error\": \"Unable to delete food, please see server console for more info.\"}";
    }
    }

    @GET
    @Path("list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listFood() {
        System.out.println("food/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT FoodID, FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving FROM Food");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("foodGroup", results.getString(3));
                item.put("foodGroup2", results.getString(4));
                item.put("calories", results.getInt(5));
                item.put("brand", results.getString(6));
                item.put("serving", results.getInt(7));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list food, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("listSearch")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listSearchFood(@FormDataParam("query") String query) {
        System.out.println("food/listSearch");
        JSONArray list = new JSONArray();
        try {
            System.out.println(query);
            String searchquery = '%'+query+'%';
            System.out.println(searchquery);
            PreparedStatement ps = Main.db.prepareStatement("SELECT FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving, Protein, TotalFat, TotalCarbs, FoodID FROM Food WHERE FoodName LIKE ?");
            ps.setString(1, searchquery);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("foodGroup", results.getString(2));
                item.put("foodGroup2", results.getString(3));
                item.put("calories", results.getInt(4));
                item.put("brand", results.getString(5));
                item.put("serving", results.getInt(6));
                item.put("protein", results.getDouble(7));
                item.put("fat", results.getDouble(8));
                item.put("carbs", results.getDouble(9));
                item.put("foodid", results.getInt(10));
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
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFood(@PathParam("id") Integer id, @CookieParam("token") String token) throws Exception {
        if (!Users.validToken(token)) { //MAY NOT BE NEEDED HERE - USED TO CHECK IF THE USER IS LOGGED IN
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        if (id == null) {
            throw new Exception("User's 'id' is missing in the HTTP request's URL.");
        }
        System.out.println("food/get/" + id);
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT FoodName, FoodGroup, FoodGroup2, Calories, Brand, Serving, TotalFat, SaturatedFat, CholesterolMG, SodiumMG, TotalCarbs, Fibre, Sugars, Protein FROM Food WHERE FoodID = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("name", results.getString(1));
                item.put("foodGroup", results.getString(2));
                item.put("foodGroup2", results.getString(3));
                item.put("calories", results.getInt(4));
                item.put("brand", results.getString(5));
                item.put("serving", results.getInt(6));
                item.put("protein", results.getDouble(7));
                item.put("fat", results.getDouble(8));
                item.put("saturatedfat", results.getDouble(9));
                item.put("cholesterol", results.getDouble(10));
                item.put("sodium", results.getDouble(11));
                item.put("carbs", results.getDouble(12));
                item.put("fibre", results.getDouble(13));
                item.put("sugars", results.getDouble(14));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFood(@FormDataParam("id") int id,
                             @FormDataParam("foodname") String foodname,
                              @FormDataParam("group") String group,
                              @FormDataParam("group2") String group2,
                              @FormDataParam("calories") int calories,
                              @FormDataParam("brand") String brand,
                              @FormDataParam("serving") int serving,
                              @FormDataParam("totfat") double totalfat,
                              @FormDataParam("satfat") double satfat,
                              @FormDataParam("cholesterol") double cholesterol,
                              @FormDataParam("sodium") double sodium,
                              @FormDataParam("totcarbs") double totalcarbs,
                              @FormDataParam("fibre") double fibre,
                              @FormDataParam("sugars") double sugars,
                              @FormDataParam("protein") double protein) {

        try {
            if (foodname == null || group == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("fruit/update id=" + id);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Food SET FoodName = ?, FoodGroup = ?, FoodGroup2 = ?, Calories = ?, Brand = ?, Serving = ?, TotalFat = ?, SaturatedFat = ?, CholesterolMG = ?, SodiumMG = ?, TotalCarbs = ?, Fibre = ?, Sugars = ?, Protein = ? WHERE FoodID = ?");
            ps.setString(1, foodname);
            ps.setString(2, group);
            ps.setString(3, group2);
            ps.setInt(4, calories);
            ps.setString(5, brand);
            ps.setInt(6, serving);
            ps.setDouble(7, totalfat);
            ps.setDouble(8, satfat);
            ps.setDouble(9, cholesterol);
            ps.setDouble(10, sodium);
            ps.setDouble(11, totalcarbs);
            ps.setDouble(12, fibre);
            ps.setDouble(13, sugars);
            ps.setDouble(14, protein);
            ps.setInt(15, id);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update fruit, please see server console for more info.\"}";
        }
    }
}


