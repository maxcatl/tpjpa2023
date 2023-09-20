package rest;

import com.mysql.cj.log.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Logger;

@Path("api")
public class Swagger
{
    private static Logger logger = Logger.getLogger(Swagger.class.getName());

    @GET
    public byte[] Get1() {
        try {
            logger.info("lol");
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
