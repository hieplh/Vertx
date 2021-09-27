package com.example.hiep.homepage;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import com.example.hiep.error.HandleErrorVerticle;
import com.example.hiep.helper.Constant;
import com.example.hiep.user.Info;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

public class HomePageVerticle extends MainVerticle implements IRequestHandler {

    private final String HOME_PAGE = RESOURCER_DEFAULT + "static/index.html";

    private void doGet(RoutingContext context) {
        HttpServerResponse response = context.response();
        JsonObject json = new JsonObject();
        json.put("host", HOST).put(Constant.LIST_INFO, context.vertx().getOrCreateContext().get("listInfo"));
        
        Session session = context.session();
        Info account = (Info) session.get(Constant.USER);
        
        ENGINE.render(json, HOME_PAGE, result -> {
            if (result.succeeded()) {
                response.end(result.result());
            } else {
                System.out.println(result.cause().getMessage());
                doError(context);
            }
        });
    }

    private void doError(RoutingContext request) {
        new HandleErrorVerticle().redirectError404(request);
    }

    @Override
    public void handleRequest() {
        ROUTER.route("/").handler(context -> doGet(context))
                .failureHandler(failed -> doError(failed));
    }
}
