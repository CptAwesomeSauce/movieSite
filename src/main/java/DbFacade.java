import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by stopp on 4/13/2018.
 */

public class DbFacade implements AutoCloseable {

    private Connection conn;
    private boolean connected;
    private String url;
    private String username;
    private String password;

    public DbFacade() {
        System.out.println("this is working and building as desired");
        //open connection to DB
        Connection conn = null;
        //url = "jdbc:mariadb://mal.cs.plu.edu:3306/367_2018_yellow";
        url = "jdbc:mariadb://127.0.0.1:2000/367_2018_yellow";
        username = "yellow_2018";
        password = "367rocks!";
        connected = false;

        try {
            conn = DriverManager.getConnection(url, username, password);
            connected = true;
            if(conn!=null){System.out.println("we have a connection");}
        } catch (SQLException e) {
            System.out.println("connetion error:{" + e.getMessage() + "}");
        }
    }

    public boolean getConnected(){
        return connected;
    }

    //close DB connection
    public void close() {
        //close connection to DB when finished
        try {
            if (conn != null) {
                conn.close();
                connected = false;
            }
        } catch (SQLException e) {
            System.out.println("connetion closure error:{" + e.getMessage() + "}");
        }
    }

    //add new review to table
    public boolean addReview(String userID, String isanID, String comments, String rating) {
        Date date = new Date();
        Object param = new Timestamp(date.getTime());

        try {
            String sql = "INSERT INTO review (dateTime, User_ID, isanID, comments, rating) VALUES(?, ?, ?, ?, ?);";
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setObject(1, param);
            pstmt.setString(2, userID);
            pstmt.setString(3, isanID);
            pstmt.setString(4, comments);
            pstmt.setString(5, rating);
            int count = pstmt.executeUpdate();
            conn.close();
            if(count > 0)
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("insert error:{" + e.getMessage() + "}");
            return false;
        }

    }

    //add new user to table
    public boolean addUser(String fname, String lname, String userID,
                         String uPass, int type, int blocked) {
        try {
            String sql = "INSERT INTO user VALUES (?,?,?,?,?,?);";
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, userID);
            pstmt.setString(4, uPass);
            pstmt.setInt(5, type);
            pstmt.setInt(6, blocked);
            int count = pstmt.executeUpdate();
            conn.close();
            if(count > 0)
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("insert error:{" + e.getMessage() + "}");
            return false;
        }
    }

    //add new movie to table
    public boolean addMovie(String title, String isanID, String genre, String mpaa, String lang, Time length, int date) {
        try {
            String sql = "INSERT INTO movie(title, isan_ID, genre, MPAA_rating, language, length, date) VALUES( ?, ?, ?, ?, ?, ?, ?)";
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, title);
            pstmt.setString(2, isanID);
            pstmt.setString(3, genre);
            pstmt.setString(4, mpaa);
            pstmt.setString(5, lang);
            pstmt.setTime(6, length);
            pstmt.setInt(7, date);
            int count = pstmt.executeUpdate();
            conn.close();
            if(count > 0)
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("insert err:{" + e.getMessage() + "}");
            return false;
        }

    }

    //counts the users who have accessed a movie
    public ResultSet countViews(String isanID) {
        ResultSet r = null;
        try {
            String sql =   "SELECT COUNT(*) FROM movie_user WHERE ISAN_ID = ? ";
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, isanID);
            r = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Report Views failed: " + e.getMessage());
        }
        return r;
    }
    //need add UPDATE review

    // SELECT movie by genre and ratings given have at least 1 rating higher than given
    //may update to average review when more reviews have populated
    public ResultSet selectByGenreAndRating(String genre, int rating) {
        ResultSet r = null;

        try {
        String sql = "SELECT * FROM movie, review "+
                "WHERE movie.ISAN_ID=review.isanID AND movie.genre LIKE ? AND review.rating >= ?";
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, "%"+genre+"%");
            pstmt.setInt(2, rating );
            r = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Report Views failed: " + e.getMessage());
        }
        return r;
    }

    //select reviews by comment length (> or <)
    public ResultSet selectReviewByCommentLength(int length, boolean lessThan) {
        ResultSet r = null;
        String sql;
        try {
            if(lessThan) {
                sql = "SELECT * FROM review WHERE LENGTH(review.comments) < ?";
            }else {
                sql = "SELECT * FROM review WHERE LENGTH(review.comments) > ?";
            }
            conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setInt(1, length);
            r = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Report Views failed: " + e.getMessage());
        }
        return r;
    }

    //populate the movie table with data from csv
    public void resetMovieTable() {
        String fName = "C:/Users/stopp/Documents/movieCSV/movieTestCase.csv";
        FileParse fp = new FileParse(fName);
        ArrayList<String[]> list = fp.parseCSV(28);
        DBPopulation pop = new DBPopulation(url,username,password);
        pop.populateMovies();

    }
}