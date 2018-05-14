/**
 * Created by stopp on 4/23/2018.
 */
import java.sql.*;
import org.junit.*;

import javax.xml.transform.Result;

import static org.junit.Assert.*;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

public class DbFacadeTest {

    @Test
    public void testOpenClose() throws SQLException {
        DbFacade db = new DbFacade();
        assertTrue(db.getConn() != null);
    }

    @Test
    public void testAddReview() throws SQLException {
        DbFacade db = new DbFacade();
        assertTrue(db.addReview("stoppede", "445", "Thought the first one was better", "3"));
        assertTrue(db.addReview("stoppede", "3322", "That dundee was hilarious, keep em comin hogan", "5"));
        assertFalse(db.addReview("stoppede", "3322", "That dundee was hilarious, keep em comin hogan", "5"));
        db.close();
    }

    @Test
    public void testAddUser() throws SQLException {
        DbFacade db = new DbFacade();
        assertTrue(db.addUser("barry","lichen","theGuy","pass1",1,0));
        assertFalse(db.addUser("barry","lichen","theGuy","pass1",1,0));
        assertFalse(db.addUser("tommy","gunn","theGuy","failure",2,0));
        assertTrue(db.addUser("barry","lichen","theOtherGuy","pass1",1,0));
        db.close();
    }

    @Test
    public void testAddMovie() throws SQLException {

        DbFacade db = new DbFacade();
        assertTrue(db.addMovie("Animal House", "0000000000001", "Comedy", "R", "English", new Time(90 * 60 * 1000), 1978));
        assertFalse(db.addMovie("Animal House 2", "0000000000001", "Comedy", "R", "English", new Time(90 * 60 * 1000), 1980));
        assertTrue(db.addMovie("Animal House", "1000000000000", "Horror", "R", "English", new Time(90 * 60 * 1000), 1994));
        db.close();
    }

    @Test
    public void testCountViews() throws SQLException {
        DbFacade db = new DbFacade();
        ResultSet r = null;

        r = db.countViews("223");
        r.next();
        int result = r.getInt(1);
        assertEquals(result,2);
        r = db.countViews("445");
        r.next();
        result = r.getInt(1);
        assertEquals(result,1);
        r = db.countViews("745");
        r.next();
        result = r.getInt(1);
        assertEquals(result,1);
        db.close();
    }

    @Test
    public void testSelectByGenreAndRating() throws SQLException {
        DbFacade db = new DbFacade();
        ResultSet r = null;

        r = db.selectByGenreAndRating("Action",3);
        r.next();
        String result = r.getString(1);
        assertEquals(result.trim(),"RED 2".trim());
        db.close();
    }

    @Test
    public void testSelectReviewByCommentLength() throws SQLException {
        DbFacade db = new DbFacade();
        ResultSet r = null;

        r = db.selectReviewByCommentLength(20,true);
        while(r.next()){
            assertTrue(r.getString(4).length() < 20);
        }
        r = db.selectReviewByCommentLength(20,false);
        while(r.next()){
            assertTrue(r.getString(4).length() > 20);
        }
        db.close();
    }
}
