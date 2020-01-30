package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("fruit/")
public class Fruit {

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String listFruits() {
        System.out.println("fruit/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Id, Name, Image, Colour, Size FROM Fruits");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("image", results.getString(3));
                item.put("colour", results.getString(4));
                item.put("size", results.getString(5));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list fruits, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFruit(@PathParam("id") Integer id) {
        System.out.println("fruit/get/" + id);
        JSONObject item = new JSONObject();
        try {
            if (id == null) {
                throw new Exception("Fruit's 'id' is missing in the HTTP request's URL.");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT Id, Name, Image, Colour, Size FROM Fruits WHERE Id = ?");
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                item.put("id", results.getInt(1));
                item.put("name", results.getString(2));
                item.put("image", results.getString(3));
                item.put("colour", results.getString(4));
                item.put("size", results.getString(5));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get fruit, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertFruit(@FormDataParam("name") String name,
                              @FormDataParam("image") String image,
                              @FormDataParam("colour") String colour,
                              @FormDataParam("size") String size) {
        try {
            if (name == null || image == null || colour == null || size == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("fruit/new");

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Fruits (Name, Image, Colour, Size) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, image);
            ps.setString(3, colour);
            ps.setString(4, size);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new fruit, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateFruit(@FormDataParam("id") Integer id,
                              @FormDataParam("name") String name,
                              @FormDataParam("image") String image,
                              @FormDataParam("colour") String colour,
                              @FormDataParam("size") String size) {

        try {
            if (id == null || name == null || image == null || colour == null || size == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("fruit/update id=" + id);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Fruits SET Name = ?, Image = ?, Colour = ?, Size = ? WHERE Id = ?");
            ps.setString(1, name);
            ps.setString(2, image);
            ps.setString(3, colour);
            ps.setString(4, size);
            ps.setInt(5, id);
            ps.execute();
            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update fruit, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFruit(@FormDataParam("id") Integer id) {

        try {
            if (id == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("fruit/delete id=" + id);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Fruits WHERE Id = ?");
            ps.setInt(1, id);
            ps.execute();

            return "{\"status\": \"OK\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to delete fruit, please see server console for more info.\"}";
        }
    }

}