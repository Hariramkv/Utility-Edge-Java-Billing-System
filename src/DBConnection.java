import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/smart_utility",
                "root",
                "Sairam07&"
            );

            System.out.println("Connected Successfully");
            return con;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}