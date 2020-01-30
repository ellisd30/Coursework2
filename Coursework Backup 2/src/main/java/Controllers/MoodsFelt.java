package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Scanner;

public class MoodsFelt {
    public static void addMoodFelt(Integer UserID){
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Mood ID: ");
            Integer moodID = input.nextInt();
            input.nextLine();
            System.out.println("Reason: ");
            String Reason = input.nextLine();

            java.util.Date today = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            java.sql.Date Date = new java.sql.Date(calendar.getTime().getTime());


            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO MoodsFelt (MoodID, UserID, Date, Time, Reason) VALUES (?, ?, ?, ?, ?)");

            ps.setInt(1, moodID);
            ps.setInt(2, UserID);
            ps.setString(3, String.valueOf(Date));
            ps.setString(4, String.valueOf((new Time(today.getTime()))));
            ps.setString(5, Reason);
            ps.executeUpdate();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void updateMoodReason(int UserID, int MoodID, String Time, String Date) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("New Reason: ");
            String Reason = input.nextLine();

            PreparedStatement ps = Main.db.prepareStatement("UPDATE MoodsFelt SET Reason = ? WHERE MoodID = ? AND UserID = ? AND Time = ? AND Date = ?");

            ps.setString(1, Reason);
            ps.setInt(2, MoodID);
            ps.setInt(3, UserID);
            ps.setString(4, Time);
            ps.setString(5, Date);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
}
