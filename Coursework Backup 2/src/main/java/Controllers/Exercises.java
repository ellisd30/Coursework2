package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

@Path("exercises/")
public class Exercises {
    public static void listExercises() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT ExerciseID, ExerciseName, ExerciseType, METs FROM Exercises");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int ExerciseID = results.getInt(1);
                String ExerciseName = results.getString(2);
                String ExerciseType = results.getString(3);
                String METs = results.getString(4);
                System.out.println("ExerciseID: " + ExerciseID + ", Exercise Name: " + ExerciseName + ", Exercise Type: " + ExerciseType + ", METs: " + METs);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void addExercise() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Exercise Name: ");
            String ExerciseName = input.nextLine();
            System.out.println("Exercise Type: ");
            String ExerciseType = input.nextLine();
            System.out.println("METs: ");
            String METs = input.nextLine();

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Exercises (ExerciseName, ExerciseType, METs) VALUES (?, ?, ?)");

            ps.setString(1, ExerciseName);
            ps.setString(2, ExerciseType);
            ps.setString(3, METs);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    @POST
    @Path("listSearch")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String listSearchFood(@FormDataParam("query") String query) {
        System.out.println("exercises/listSearch");
        JSONArray list = new JSONArray();
        try {
            System.out.println(query);
            String searchquery = '%'+query+'%';
            System.out.println(searchquery);
            PreparedStatement ps = Main.db.prepareStatement("SELECT ExerciseName, ExerciseType, METs, ExerciseID FROM Exercises WHERE ExerciseName LIKE ?");
            ps.setString(1, searchquery);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("name", results.getString(1));
                item.put("type", results.getString(2));
                item.put("mets", results.getDouble(3));
                item.put("exerciseid", results.getInt(4));
                list.add(item);
            }
            System.out.println(list.toString());
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list exercises, please see server console for more info.\"}";
        }
    }
}
