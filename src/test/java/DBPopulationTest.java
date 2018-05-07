import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by stopp on 4/24/2018.
 */
public class DBPopulationTest {

    @Test
    public void dBPopulationTest() throws SQLException {

        // This will populate the database with apx 5000 entries
        //requires empty table

            DbFacade db = new DbFacade();
            db.resetMovieTable();
            db.close();


    }
}
