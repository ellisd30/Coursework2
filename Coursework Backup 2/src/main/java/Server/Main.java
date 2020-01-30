package Server;

import java.sql.*;
import java.util.Scanner;

import Controllers.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;

public class Main {

        public static Connection db = null; //Behaves like a global variable

        public static void main(String[] args) {

        openDatabase("Database.db");

        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            System.out.println("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        /*public static void main(String[] args) {
            openDatabase("Database.db");
            // code to get data from, write to the database etc goes here!
            //UserInfo.updateUser();
            UserInfo.listUsers();
            //UserInfo.addUser();
            Food.listFood();
            Moods.listMoods();
            Exercises.listExercises();
            closeDatabase();
        }*/

        private static void openDatabase(String dbFile) {
            try  {
                Class.forName("org.sqlite.JDBC"); //loads database driver
                SQLiteConfig config = new SQLiteConfig(); //does database settings
                config.enforceForeignKeys(true);
                db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties()); //opens database files
                System.out.println("Database connection successfully established.");
            } catch (Exception exception) { //catches any errors
                System.out.println("Database connection error: " + exception.getMessage());
            }

        }

        private static void closeDatabase(){
            try {
                db.close(); //closes the database file
                System.out.println("Disconnected from database.");
            } catch (Exception exception) { //error catching
                System.out.println("Database disconnection error: " + exception.getMessage());
            }
        }

}


