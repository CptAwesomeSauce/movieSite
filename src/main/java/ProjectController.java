import spark.Request;
import spark.Response;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectController {

    public Object displayHome(Request req, Response resp){
            return runner.renderTemplate(null, "homepage.hbs");
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


}
