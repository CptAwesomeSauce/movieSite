import java.sql.SQLException;
import java.sql.Time;

import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

import static spark.Spark.before;
import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created by stopp on 4/24/2018.
 */
public class runner {
    public static void main(String[] args) throws SQLException {

        DbFacade db = new DbFacade();
        ProjectController controller = new ProjectController();

        Spark.get("/homepage", controller::displayHome);

        post( "/authenticate", controller::postLoginForm);
        post( "/deauthenticate", controller::releaseLogin);
        post("/post-stevensPage", controller::displayGenrePost);
        get("/newuser", controller::createNewUser);
        get("/user/userhome", controller::getUserHome);
        get("/mod/modhome", controller::getModHome);
        get("/admin/adminhome", controller::getAdminHome);

            //db.addMovie("Animal House", "0000000000001", "Comedy", "R", "English", new Time(90 * 60 * 1000), 1978);
        before("/admin/*", controller::adminBeforeFilter);
        before("/mod/*", controller::modBeforeFilter);
        before("/user/*", controller::userBeforeFilter);
            db.close();




    }

    public static Object renderTemplate(Map<String, Object> data, String path){
        return new HandlebarsTemplateEngine().render(new ModelAndView(data,path));
    }
}
