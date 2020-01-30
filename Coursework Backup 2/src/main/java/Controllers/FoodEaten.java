package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class FoodEaten {

    public static void deleteFood(int FoodID, int UserID, String Date, String Time) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM FoodEaten WHERE FoodID = ? AND UserID = ? AND Date = ? AND Time = ?");

            ps.setInt(1, FoodID);
            ps.setInt(2, UserID);
            ps.setString(3, Date);
            ps.setString(4, Time);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
