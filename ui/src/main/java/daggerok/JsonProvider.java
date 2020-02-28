package daggerok;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class JsonProvider extends JacksonJaxbJsonProvider { }
