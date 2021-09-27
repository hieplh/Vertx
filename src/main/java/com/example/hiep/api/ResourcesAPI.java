package com.example.hiep.api;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import com.example.hiep.error.HandleErrorVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.Random;

public class ResourcesAPI extends MainVerticle implements IRequestHandler {

    private void getStatic(RoutingContext request) {
        HttpServerResponse response = request.response();
        String filePath = getPath(request);
        FILE_SYSTEM.readFile(filePath, result -> {
            if (result.succeeded()) {
                response.sendFile(filePath);
            } else {
                response.end();
            }
        });
    }

    private void getImage(RoutingContext request) {
        HttpServerResponse response = request.response();
        String filePath;
        
        if (request.normalizedPath().endsWith("gif")) {
            filePath = getRandGif(FILE_SYSTEM, request.normalizedPath());
        } else {
            filePath = RESOURCER_DEFAULT + request.normalizedPath();
        }

        Buffer buffer = FILE_SYSTEM.readFileBlocking(filePath);
        if (buffer != null) {
            response.sendFile(filePath);
        } else {
            response.end();
        }
    }

    private String getRandGif(FileSystem fileSystem, String path) {
        List<String> listFiles = fileSystem.readDirBlocking(RESOURCER_DEFAULT + path);
        Random rand = new Random();
        return listFiles.get(rand.nextInt(listFiles.size()));
    }

    private void doError(RoutingContext request) {
        new HandleErrorVerticle().redirectError404(request);
    }

    @Override
    public void handleRequest() {
        ROUTER.routeWithRegex("/(css|js)/.*")
                .handler(request -> getStatic(request))
                .failureHandler(f -> doError(f));
        ROUTER.routeWithRegex("(?!/error.html).*\\.html")
                .handler(request -> getStatic(request))
                .failureHandler(f -> doError(f));
        ROUTER.routeWithRegex("/img/.*")
                .handler(request -> getImage(request))
                .failureHandler(f -> doError(f));
    }
}
