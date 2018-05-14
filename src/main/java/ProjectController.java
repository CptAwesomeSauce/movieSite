import spark.Request;
import spark.Response;
import spark.Session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class ProjectController {

    public Object displayHome(Request req, Response resp){
            return runner.renderTemplate(null, "homepage.hbs");
    }

    public Object getUserHome(Request req, Response resp) {
        return runner.renderTemplate(null, "user-home.hbs");
    }

    public Object getModHome(Request req, Response resp) {
        return runner.renderTemplate(null, "admin-home.hbs");
    }

    public Object getAdminHome(Request req, Response resp) {
        return runner.renderTemplate(null, "admin-home.hbs");
    }

    public Object postLoginForm(Request req, Response resp) {
        String uname = req.queryParams("login_field");
        String pwd = req.queryParams("pword_field");
        try(DbFacade db = new DbFacade()) {
            int result = db.authenticateUser(uname, pwd);
            Session sess = req.session();
            sess.attribute("username", uname);
            sess.attribute("authenticated", true);
            if(result  == 1){
                sess.attribute("type", 1);
                return runner.renderTemplate(null, "suc-user.hbs");
            }else if(result == 2){
                sess.attribute("type", 2);
                return runner.renderTemplate(null, "suc-mod.hbs");
            }else if(result == 3) {
                sess.attribute("type", 2);
                return runner.renderTemplate(null, "suc-admin.hbs");
            }else{
                sess.attribute("type", 0);
                Map<String,Object> data = new HashMap<>();
                data.put("errorMsg", "Login failed!");
                return runner.renderTemplate(data, "homepage.hbs");
            }

        } catch(SQLException e) {
            resp.status(500);
            System.err.println("postLoginForm: " + e.getMessage());
            return "";
        }
    }

    public Object displayGenrePost(Request req, Response resp){
        String titleIn = req.queryParams("title_field");
        String genreIn = req.queryParams("genre_field");

        //If the user checked the "other" button, we want to redirect them
        //to another page where they can enter another genre.
        if (genreIn.compareTo("other") == 0)
            return runner.renderTemplate(null, "otherGenreForm.hbs");

        //else display steven's page with the list of movies that match
        return null;
    }

    public Object releaseLogin(Request req, Response resp){
        req.session().attribute("username", "");
        req.session().attribute("authenticated", false);
        req.session().attribute("type", 0);
        Map<String,Object> data = new HashMap<>();
        data.put("message", "Logged out successfully");
        return runner.renderTemplate(data, "homepage.hbs");
    }

    public void userBeforeFilter(Request req, Response resp) {
        Boolean auth = req.session().attribute("authenticated");
        int type = req.session().attribute("type");
        if( auth == null || (!auth) ) {
            if(type != 1)
                halt(401, "Access denied");
        }
    }

    public void modBeforeFilter(Request req, Response resp) {
        Boolean auth = req.session().attribute("authenticated");
        int type = req.session().attribute("type");
        if( auth == null || (!auth) ) {
            if(type != 2)
                halt(401, "Access denied");
        }
    }

    public void adminBeforeFilter(Request req, Response resp) {
        Boolean auth = req.session().attribute("authenticated");
        int type = req.session().attribute("type");
        if( auth == null || (!auth) ) {
            if(type != 3)
                halt(401, "Access denied");
        }
    }

}
