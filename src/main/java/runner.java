import java.sql.SQLException;
import java.sql.Time;

/**
 * Created by stopp on 4/24/2018.
 */
public class runner {
    public static void main(String[] args) throws SQLException {

            DbFacade db = new DbFacade();

            db.addMovie("Animal House", "0000000000001", "Comedy", "R", "English", new Time(90 * 60 * 1000), 1978);

            db.close();



    }
}
