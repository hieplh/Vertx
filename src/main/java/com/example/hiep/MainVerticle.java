package com.example.hiep;

import com.example.hiep.api.ResourcesAPI;
import com.example.hiep.cv.ProfileVerticle;
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
        mUnicodeASCII.put("á", "a");
        mUnicodeASCII.put("à", "a");
        mUnicodeASCII.put("ả", "a");
        mUnicodeASCII.put("ã", "a");
        mUnicodeASCII.put("ạ", "a");
        mUnicodeASCII.put("ă", "a");
        mUnicodeASCII.put("ắ", "a");
        mUnicodeASCII.put("ằ", "a");
        mUnicodeASCII.put("ẳ", "a");
        mUnicodeASCII.put("ẵ", "a");
        mUnicodeASCII.put("ặ", "a");
        mUnicodeASCII.put("â", "a");
        mUnicodeASCII.put("ấ", "a");
        mUnicodeASCII.put("ầ", "a");
        mUnicodeASCII.put("ẩ", "a");
        mUnicodeASCII.put("ẫ", "a");
        mUnicodeASCII.put("ậ", "a");
        mUnicodeASCII.put("ầ", "a");
        mUnicodeASCII.put("Á", "A");
        mUnicodeASCII.put("À", "A");
        mUnicodeASCII.put("Ả", "A");
        mUnicodeASCII.put("Ã", "A");
        mUnicodeASCII.put("Ạ", "A");
        mUnicodeASCII.put("Ă", "A");
        mUnicodeASCII.put("Ắ", "A");
        mUnicodeASCII.put("Ằ", "A");
        mUnicodeASCII.put("Ẳ", "A");
        mUnicodeASCII.put("Ẵ", "A");
        mUnicodeASCII.put("Ặ", "A");
        mUnicodeASCII.put("Â", "A");
        mUnicodeASCII.put("Ấ", "A");
        mUnicodeASCII.put("Ầ", "A");
        mUnicodeASCII.put("Ẩ", "A");
        mUnicodeASCII.put("Ẫ", "A");
        mUnicodeASCII.put("Ậ", "A");
        mUnicodeASCII.put("Ầ", "A");
        mUnicodeASCII.put("í", "i");
        mUnicodeASCII.put("ì", "i");
        mUnicodeASCII.put("ỉ", "i");
        mUnicodeASCII.put("ĩ", "i");
        mUnicodeASCII.put("ị", "i");
        mUnicodeASCII.put("I", "I");
        mUnicodeASCII.put("Í", "I");
        mUnicodeASCII.put("Ì", "I");
        mUnicodeASCII.put("Ỉ", "I");
        mUnicodeASCII.put("Ĩ", "I");
        mUnicodeASCII.put("Ị", "I");
        mUnicodeASCII.put("ú", "u");
        mUnicodeASCII.put("ù", "u");
        mUnicodeASCII.put("ủ", "u");
        mUnicodeASCII.put("ũ", "u");
        mUnicodeASCII.put("ụ", "u");
        mUnicodeASCII.put("ư", "u");
        mUnicodeASCII.put("ứ", "u");
        mUnicodeASCII.put("ừ", "u");
        mUnicodeASCII.put("ử", "u");
        mUnicodeASCII.put("ữ", "u");
        mUnicodeASCII.put("ự", "u");
        mUnicodeASCII.put("Ú", "U");
        mUnicodeASCII.put("Ù", "U");
        mUnicodeASCII.put("Ủ", "U");
        mUnicodeASCII.put("Ũ", "U");
        mUnicodeASCII.put("Ụ", "U");
        mUnicodeASCII.put("Ư", "U");
        mUnicodeASCII.put("Ứ", "U");
        mUnicodeASCII.put("Ừ", "U");
        mUnicodeASCII.put("Ử", "U");
        mUnicodeASCII.put("Ữ", "U");
        mUnicodeASCII.put("Ự", "U");
        mUnicodeASCII.put("é", "e");
        mUnicodeASCII.put("è", "e");
        mUnicodeASCII.put("ẻ", "e");
        mUnicodeASCII.put("ẽ", "e");
        mUnicodeASCII.put("ẹ", "e");
        mUnicodeASCII.put("ê", "e");
        mUnicodeASCII.put("ế", "e");
        mUnicodeASCII.put("ề", "e");
        mUnicodeASCII.put("ể", "e");
        mUnicodeASCII.put("ễ", "e");
        mUnicodeASCII.put("ệ", "e");
        mUnicodeASCII.put("É", "E");
        mUnicodeASCII.put("È", "E");
        mUnicodeASCII.put("Ẻ", "E");
        mUnicodeASCII.put("Ẽ", "E");
        mUnicodeASCII.put("Ẹ", "E");
        mUnicodeASCII.put("Ê", "E");
        mUnicodeASCII.put("Ế", "E");
        mUnicodeASCII.put("Ề", "E");
        mUnicodeASCII.put("Ể", "E");
        mUnicodeASCII.put("Ễ", "E");
        mUnicodeASCII.put("Ệ", "E");
        mUnicodeASCII.put("ó", "o");
        mUnicodeASCII.put("ò", "o");
        mUnicodeASCII.put("ỏ", "o");
        mUnicodeASCII.put("õ", "o");
        mUnicodeASCII.put("ọ", "o");
        mUnicodeASCII.put("ô", "o");
        mUnicodeASCII.put("ố", "o");
        mUnicodeASCII.put("ồ", "o");
        mUnicodeASCII.put("ổ", "o");
        mUnicodeASCII.put("ỗ", "o");
        mUnicodeASCII.put("ộ", "o");
        mUnicodeASCII.put("ơ", "o");
        mUnicodeASCII.put("ớ", "o");
        mUnicodeASCII.put("ờ", "o");
        mUnicodeASCII.put("ở", "o");
        mUnicodeASCII.put("ỡ", "o");
        mUnicodeASCII.put("ợ", "o");
        mUnicodeASCII.put("Ó", "O");
        mUnicodeASCII.put("Ò", "O");
        mUnicodeASCII.put("Ỏ", "O");
        mUnicodeASCII.put("Õ", "O");
        mUnicodeASCII.put("Ọ", "O");
        mUnicodeASCII.put("Ô", "O");
        mUnicodeASCII.put("Ố", "O");
        mUnicodeASCII.put("Ồ", "O");
        mUnicodeASCII.put("Ổ", "O");
        mUnicodeASCII.put("Ỗ", "O");
        mUnicodeASCII.put("Ộ", "O");
        mUnicodeASCII.put("Ơ", "O");
        mUnicodeASCII.put("Ớ", "O");
        mUnicodeASCII.put("Ờ", "O");
        mUnicodeASCII.put("Ở", "O");
        mUnicodeASCII.put("Ỡ", "O");
        mUnicodeASCII.put("Ợ", "O");
        mUnicodeASCII.put("ý", "y");
        mUnicodeASCII.put("ỳ", "y");
        mUnicodeASCII.put("ỷ", "y");
        mUnicodeASCII.put("ỹ", "y");
        mUnicodeASCII.put("ỵ", "y");
        mUnicodeASCII.put("Ý", "Y");
        mUnicodeASCII.put("Ỳ", "Y");
        mUnicodeASCII.put("Ỷ", "Y");
        mUnicodeASCII.put("Ỹ", "Y");
        mUnicodeASCII.put("Ỵ", "Y");
        mUnicodeASCII.put("đ", "d");
        mUnicodeASCII.put("Đ", "D");
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
