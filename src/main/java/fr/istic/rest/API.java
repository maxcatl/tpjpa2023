package fr.istic.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Logger;

@Path("api")
public class API
{

    @GET
    public byte[] Get1() {
        try {
            return Files.readAllBytes(FileSystems.getDefault().getPath("src/main/webapp/swagger/dist/index.html"));
        } catch (IOException e) {
            return null;
        }
    }

    @GET
    @Path("{path:.*}")
    public byte[] Get(@PathParam("path") String path) {
        try {
            return Files.readAllBytes(FileSystems.getDefault().getPath("src/main/webapp/swagger/dist/"+path));
        } catch (IOException e) {
            return null;
        }
    }
}
