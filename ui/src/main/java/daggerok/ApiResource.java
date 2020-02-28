package daggerok;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("api")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {

  @GET
  @Path("names")
  public List<String> getNames() {
    return Arrays.asList(
        "ololo",
        "trololo"
    );
  }

  @GET
  @Path("greetings")
  public String getGreetings() {
    return Json.createObjectBuilder()
               .add("en", "Hello")
               .add("ru", "Превед")
               .add("ua", "Привiд")
               .add("0lb@nsk1y", "Ololo trololo...")
               .build()
               .toString();
  }
}
