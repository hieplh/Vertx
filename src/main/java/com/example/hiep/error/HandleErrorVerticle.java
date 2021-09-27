package com.example.hiep.error;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HandleErrorVerticle extends MainVerticle implements IRequestHandler {
    
    public final String ERROR_PAGE = RESOURCER_DEFAULT + "static/error.html";
    
    private void doError(RoutingContext context) {
        HttpServerResponse response = context.response();
        JsonObject json = new JsonObject();
        json.put("host", HOST);
        
        ENGINE.render(json, ERROR_PAGE, result -> {
            if (result.succeeded()) {
                response.setStatusCode(404);
                response.end(result.result());
            } else {
                response.setStatusCode(500);
                response.end("Server got something error!!!");
            }
        });
    }
    
    @Override
    public void handleRequest() {
        ROUTER.route("/error.html").handler(f -> {
            doError(f);
        }).failureHandler(f -> f.response().end("Server got something error!!!"));
        ROUTER.route().last().handler(context -> {
            redirectError404(context);
        }).failureHandler(f -> f.response().end("Server got something error!!!"));
    }
    
    public void redirectError404(RoutingContext context) {
        context.response()
                .setStatusCode(301)
                .putHeader("Location", HOST + "/error.html")
                .end();
    }
}
