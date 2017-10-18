package calc;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class ServerHost {
	public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver loading success!");
          //у MySQL обязательно есть системная база,
            //к ней и будем создавать соединение.
            String url = "jdbc:sqlserver://DESKTOP-4927RAE\\Mariya:1433;databaseName=Eugeniya";
            String name = "Mariya";
            String password = "";
            try {
                Connection con = DriverManager.getConnection(url, name, password);
                System.out.println("Connected.");
                con.close();
                System.out.println("Disconnected.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
