package com.example.hiep.user;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import java.util.Set;

public class SignupVertical extends MainVerticle implements IRequestHandler {
    
    private void doPost(RoutingContext context) {
        try {
            MultiMap forms = context.request().formAttributes();
            for (Map.Entry<String, String> form : forms) {
                System.out.println("Form: " + form);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("");
        
        try {
            Set<FileUpload> upload = context.fileUploads();
            System.out.println("upload size: " + upload.size());
            FileUpload file = upload.iterator().next();
            System.out.println("file charset: " + file.charSet());
            System.out.println("content transer encoding: " + file.contentTransferEncoding());
            System.out.println("content type: " + file.contentType());
            System.out.println("file name: " + file.fileName());
            System.out.println("name: " + file.name());
            System.out.println("upload file name: " + file.uploadedFileName());
            System.out.println("size: " + file.size());
            FILE_SYSTEM.moveBlocking(file.uploadedFileName(), RESOURCER_DEFAULT + "img/" + file.fileName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        context.response().end();
    }
    
    @Override
    public void handleRequest() {
        ROUTER.route(HttpMethod.POST, "/signup")
                .handler(context -> doPost(context));
    }
    
}
