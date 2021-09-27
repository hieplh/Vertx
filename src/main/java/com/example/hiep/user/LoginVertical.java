package com.example.hiep.user;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import com.example.hiep.error.HandleErrorVerticle;
import com.example.hiep.helper.Constant;
import com.example.hiep.error.Error;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import java.util.List;

public class LoginVertical extends MainVerticle implements IRequestHandler {

    private void doPost(RoutingContext context) {
        String username = context.request().getFormAttribute("username");
        String password = context.request().getFormAttribute("password");

        List<Info> listInfo = (List<Info>) context.vertx().getOrCreateContext().get(Constant.LIST_INFO);
        Info account = checkLogin(listInfo, username, password);

        HttpServerResponse response = context.response();
        if (account != null) {
            Session session = context.session();
            session.put(Constant.USER, account);

            response.end();
        } else {
            Error error = new Error();
            error.setErrMsg(Constant.LOGIN_ERROR);

            JsonObject json = new JsonObject();
            json.put(Constant.ERROR, error);

            response.end(json.encodePrettily());
        }
    }

    private Info checkLogin(List<Info> listInfo, String username, String password) {
        return listInfo.stream()
                .filter(e -> (e.getIndex().equals(username) && e.getShortname().equals(password)))
                .findFirst()
                .orElse(null);
    }

    private void doError(RoutingContext context) {
        new HandleErrorVerticle().redirectError404(context);
    }

    @Override
    public void handleRequest() {
        ROUTER.post("/login")
                .handler(context -> doPost(context))
                .failureHandler(f -> doError(f));
    }
}
