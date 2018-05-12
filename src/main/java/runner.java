import java.sql.SQLException;
import java.sql.Time;

import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

/**
 * Created by stopp on 4/24/2018.
 */
public class runner {
    public static void main(String[] args) throws SQLException {

            DbFacade db = new DbFacade();

            ProjectController controller = new ProjectController();

            Spark.get("/homepage", controller::displayHome);


            Spark.post("/post-stevensPage", controller::displayGenrePost);

            //db.addMovie("Animal House", "0000000000001", "Comedy", "R", "English", new Time(90 * 60 * 1000), 1978);

            db.close();




    }

    public static Object renderTemplate(Map<String, Object> data, String path){
        return new HandlebarsTemplateEngine().render(new ModelAndView(data,path));
    }
}
