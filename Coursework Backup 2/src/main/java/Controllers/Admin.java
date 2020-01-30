package Controllers;

import java.sql.*;
import java.util.Scanner;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("admin/")
public class Admin {
    @POST
    @Path("deleteUser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser(@FormDataParam("userid") String userid) throws Exception {
        System.out.println("admin/deleteUser");
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM UserInfo WHERE UserID = ?");

            ps.setString(1, userid);
            ps.execute();
            return "{User deleted from the database.}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("searchUsers")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String searchUsers(@FormDataParam("query") String query) {
        System.out.println("admin/searchUsers");
        JSONArray list = new JSONArray();
        try {
            System.out.println(query);
            String searchquery = '%'+query+'%';
            System.out.println(searchquery);
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, FirstName, LastName, Gender, Username FROM UserInfo WHERE Username LIKE ?");
            ps.setString(1, searchquery);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("userid", results.getInt(1));
                item.put("firstname", results.getString(2));
                item.put("lastname", results.getString(3));
                item.put("gender", results.getString(4));
                item.put("username", results.getString(5));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list users, please see server console for more info.\"}";
        }
    }
}
