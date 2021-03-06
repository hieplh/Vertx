package com.example.hiep;

import com.example.hiep.api.ResourcesAPI;
import com.example.hiep.cv.ProfileVerticle;
import com.example.hiep.demo.Calculating;
import com.example.hiep.download.DownloadVertical;
import com.example.hiep.error.HandleErrorVerticle;
import com.example.hiep.helper.Constant;
import com.example.hiep.homepage.HomePageVerticle;
import com.example.hiep.user.Info;
import com.example.hiep.user.LoginVertical;
import com.example.hiep.user.SignupVertical;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    public static Router ROUTER;
    public static HttpServer SERVER;
    public static FileSystem FILE_SYSTEM;
    public static ThymeleafTemplateEngine ENGINE;
    public static String HOST = "http://localhost";
//    public static String HOST = "http://cvonline.ddns.net";
    public static final Map<String, String> mUnicodeASCII = new HashMap<>();

    public final String RESOURCER_DEFAULT = "./src/main/resources/";
//    public final String RESOURCER_DEFAULT = "./resources/";

    public final List<IRequestHandler> listRequestHandler;

    public MainVerticle() {
        this.listRequestHandler = new ArrayList<>();
        initUnicodeASCII();
    }

    @Override
    public void start() throws Exception {
        SERVER = vertx.createHttpServer();
        ROUTER = Router.router(vertx);
//        router.route().handler(StaticHandler.create()); // default resources is webroot
//        router.route().handler(StaticHandler.create("static")); // override root resource
        ROUTER.route().handler(SessionHandler.create(
                LocalSessionStore.create(vertx, LocalSessionStore.DEFAULT_SESSION_MAP_NAME, 10 * 60 * 1000)));
        ROUTER.route().handler(BodyHandler.create(RESOURCER_DEFAULT + "img"));
        ENGINE = ThymeleafTemplateEngine.create(vertx);
        FILE_SYSTEM = vertx.fileSystem();

        initInfo();
        initHandlerRequest();
        SERVER.requestHandler(ROUTER)
                .listen(8080, http -> {
                    if (http.succeeded()) {
                        HOST = HOST + ":" + SERVER.actualPort();
                        context.put("host", HOST);
                        System.out.println("HTTP Server started on port " + SERVER.actualPort());
                    } else {
                        System.out.println(http.cause().getMessage());
                    }
                });
    }

    private void initHandlerRequest() {
        listRequestHandler.add(new HomePageVerticle());
        listRequestHandler.add(new ProfileVerticle());
        listRequestHandler.add(new ResourcesAPI());
        listRequestHandler.add(new HandleErrorVerticle());
        listRequestHandler.add(new LoginVertical());
        listRequestHandler.add(new SignupVertical());
        listRequestHandler.add(new DownloadVertical());
        listRequestHandler.add(new Calculating());

        listRequestHandler.forEach(e -> e.handleRequest());
    }

    private void initInfo() throws IOException, FileNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        List<Info> listInfo = loadInfo();
        context.put(Constant.LIST_INFO, listInfo);
    }

    private List<Info> loadInfo() throws FileNotFoundException, IOException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(
                new File(RESOURCER_DEFAULT + "profile.properties")), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(isr);
        String s;
        boolean isNew = false;
        Field field;
        List<Info> listInfo = new ArrayList<>();
        while ((s = bf.readLine()) != null) {
            if (s.startsWith("index=")) {
                listInfo.add(new Info());
                isNew = true;
            } else if (s.trim().isEmpty()) {
                isNew = false;
            }

            if (isNew) {
                String[] tmp = s.split("=", 2);
                Info info = listInfo.get(listInfo.size() - 1);
                field = info.getClass().getDeclaredField(tmp[0].toLowerCase());
                field.setAccessible(true);
                field.set(info, tmp[1]);
                if (tmp[0].equalsIgnoreCase("fullname")) {
                    info.setShortname(tmp[1]);
                }
            }
        }

        return listInfo;
    }

    private void initUnicodeASCII() {
        mUnicodeASCII.put("??", "a");
        mUnicodeASCII.put("??", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("??", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("??", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("??", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("???", "a");
        mUnicodeASCII.put("??", "A");
        mUnicodeASCII.put("??", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("??", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("??", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("??", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("???", "A");
        mUnicodeASCII.put("??", "i");
        mUnicodeASCII.put("??", "i");
        mUnicodeASCII.put("???", "i");
        mUnicodeASCII.put("??", "i");
        mUnicodeASCII.put("???", "i");
        mUnicodeASCII.put("I", "I");
        mUnicodeASCII.put("??", "I");
        mUnicodeASCII.put("??", "I");
        mUnicodeASCII.put("???", "I");
        mUnicodeASCII.put("??", "I");
        mUnicodeASCII.put("???", "I");
        mUnicodeASCII.put("??", "u");
        mUnicodeASCII.put("??", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("??", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("??", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("???", "u");
        mUnicodeASCII.put("??", "U");
        mUnicodeASCII.put("??", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("??", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("??", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("???", "U");
        mUnicodeASCII.put("??", "e");
        mUnicodeASCII.put("??", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("??", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("???", "e");
        mUnicodeASCII.put("??", "E");
        mUnicodeASCII.put("??", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("??", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("???", "E");
        mUnicodeASCII.put("??", "o");
        mUnicodeASCII.put("??", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("??", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("??", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("??", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("???", "o");
        mUnicodeASCII.put("??", "O");
        mUnicodeASCII.put("??", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("??", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("??", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("??", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("???", "O");
        mUnicodeASCII.put("??", "y");
        mUnicodeASCII.put("???", "y");
        mUnicodeASCII.put("???", "y");
        mUnicodeASCII.put("???", "y");
        mUnicodeASCII.put("???", "y");
        mUnicodeASCII.put("??", "Y");
        mUnicodeASCII.put("???", "Y");
        mUnicodeASCII.put("???", "Y");
        mUnicodeASCII.put("???", "Y");
        mUnicodeASCII.put("???", "Y");
        mUnicodeASCII.put("??", "d");
        mUnicodeASCII.put("??", "D");
    }

    protected String getPath(RoutingContext request) {
        String filePath = RESOURCER_DEFAULT + "static/" + request.normalizedPath();
        return filePath.replaceAll("//", "/");
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
