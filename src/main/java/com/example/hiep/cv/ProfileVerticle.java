package com.example.hiep.cv;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import com.example.hiep.error.HandleErrorVerticle;
import com.example.hiep.helper.Constant;
import com.example.hiep.helper.Utils;
import com.example.hiep.user.Info;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.List;

public class ProfileVerticle extends MainVerticle implements IRequestHandler {

    private final String INFO_PAGE = RESOURCER_DEFAULT + "static/profile.html";

    private void doGet(RoutingContext context) {
        List<Info> listInfo = (List<Info>) context.vertx().getOrCreateContext().get(Constant.LIST_INFO);
        Info user = listInfo.stream().filter(e -> {
            return Utils.getShortenName(e.getFullname(), false).equals(context.normalizedPath().split("/")[2]);
        }).findFirst().orElse(null);

        if (user == null) {
            doError(context);
        } else {
            HttpServerResponse response = context.response();
            JsonObject json = new JsonObject();
            json.put("host", HOST).put("user", user);
            ENGINE.render(json, INFO_PAGE, result -> {
                if (result.succeeded()) {
                    response.end(result.result()); // response body to browser
                } else {
                    System.out.println("Error: " + result.cause().getMessage());
                    doError(context);
                }
            }); // take 3s done
            // doSomething else // take 5s done
        }
    }

    private void getCV(RoutingContext context) {
        Buffer buffer;
        String filePath = RESOURCER_DEFAULT + context.normalizedPath();
        filePath = filePath.replaceAll("//", "/");
        HttpServerResponse response = context.response();
        if (FILE_SYSTEM.existsBlocking(filePath)) {
            buffer = FILE_SYSTEM.readFileBlocking(filePath);
            if (buffer != null) {
                response.sendFile(filePath);
            } else {
                doError(context);
            }
        } else {
            filePath = filePath.replaceFirst("/cv", "");
            if (FILE_SYSTEM.existsBlocking(filePath)) {
                buffer = FILE_SYSTEM.readFileBlocking(filePath);
                if (buffer != null) {
                    response.sendFile(filePath);
                } else {
                    doError(context);
                }
            } else {
                doError(context);
            }
        }
    }

    private void getStatic(RoutingContext context) {
        String pathParam = context.normalizedPath().replaceFirst("/cv", "");
        context.redirect(pathParam);
    }

    private void doError(RoutingContext context) {
        new HandleErrorVerticle().redirectError404(context);
    }

    @Override
    public void handleRequest() {
        ROUTER.routeWithRegex("/cv/\\w+")
                .handler(context -> doGet(context))
                .failureHandler(f -> doError(f));
        ROUTER.routeWithRegex("/cv/(css|js)/.*\\.(css|js)")
                .handler(context -> getStatic(context))
                .failureHandler(f -> doError(f));
        ROUTER.routeWithRegex("/cv/.*\\.pdf")
                .handler(context -> getCV(context))
                .failureHandler(f -> doError(f));
    }
}