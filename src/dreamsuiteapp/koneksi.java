package dreamsuiteapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
 
public class koneksi {
    public static Connection con;
    public static Statement stm;
    
    public static Connection getKoneksi(){
        try {
            String url ="jdbc:mysql://localhost:4306/dreamsuite";
            String user="root";
            String pass="";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con =DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            System.out.println("koneksi berhasil");
        } catch (Exception e) {
            System.err.println("koneksi gagal" +e.getMessage());
        }
        return con;
    }
    
}
