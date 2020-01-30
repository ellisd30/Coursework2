package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Moods {
    public static void listMoods() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT MoodID, MoodName, MoodDesc FROM Moods");

            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int MoodID = results.getInt(1);
                String MoodName = results.getString(2);
                String MoodDesc = results.getString(3);
                System.out.println("MoodID: " + MoodID + ", Mood Name: " + MoodName + ", Mood Description: " + MoodDesc);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void addMood() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Mood Name: ");
            String MoodName = input.nextLine();
            System.out.println("Mood Description: ");
            String MoodDesc = input.nextLine();

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Moods (MoodName, MoodDesc) VALUES (?, ?)");

            ps.setString(1, MoodName);
            ps.setString(2, MoodDesc);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
