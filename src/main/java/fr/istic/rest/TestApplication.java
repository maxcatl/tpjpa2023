package fr.istic.rest;

import java.util.HashSet;
import java.util.Set;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class TestApplication extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {

        final Set<Class<?>> clazzes = new HashSet<>();

        clazzes.add(OpenApiResource.class);
        clazzes.add(EleveRessource.class);
        clazzes.add(ProfesseurRessource.class);
        clazzes.add(RDVRessource.class);
        clazzes.add(API.class);

        return clazzes;
    }
}