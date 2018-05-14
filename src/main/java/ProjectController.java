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





}
