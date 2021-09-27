package com.example.hiep.download;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import java.awt.PageAttributes;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadVertical extends MainVerticle implements IRequestHandler {

    private final String DOWNLOAD_PAGE = "static/download.html";
    private final String FILE_HERO3_DOWNLOAD_PATH = "D:\\Games\\Heroes_3 WoG 3.8.2.rar";
    private final String FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH = "D:\\Games\\[Ryuublogger] Koutetsu_majo_annerose_eng.zip";
    private final String FILE_MEGAMAN5_DOWNLOAD_PATH = "C:\\Users\\Admin\\Downloads\\megamanx5.exe";
    private final String FILE_MOBAZ_DOWNLOAD_PATH = "C:\\Users\\Admin\\Downloads\\MobaZ3.zip";

    private final int SLOW_SPEED = 1024;
    private final int MEDIUM_SPEED = 4048;

    private final long MEGABYTE = 1024 * 1024;

    private static final int MAX_SPEED = 8 * 1024;
    private final long ONE_SECOND = 1000;
    private long downloadedWhithinOneSecond = 0L;
    private long lastTime = System.currentTimeMillis();

    private void doGet(RoutingContext context) {
        try {
            context.response().end(Files.readString(Path.of(RESOURCER_DEFAULT, DOWNLOAD_PAGE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download(RoutingContext context) {
        int index = Integer.parseInt(context.normalizedPath().substring(context.normalizedPath().lastIndexOf("/") + 1));
        switch (index) {
            case 1:
                downloadUsingSendFile(context, FILE_HERO3_DOWNLOAD_PATH);
                break;
            case 2:
                downloadUsingSendBuffer_ApplicationJson(context, FILE_MEGAMAN5_DOWNLOAD_PATH);
                break;
            case 3:
                downloadUsingSendBuffer_OctetStream(context, FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH);
                break;
            case 4:
                downloadUsingSendBuffer_OctetStream(context, FILE_MOBAZ_DOWNLOAD_PATH);
//                FILE_SYSTEM.readFile(FILE_HERO3_DOWNLOAD_PATH, handler -> {
//                    if (handler.succeeded()) {
//                        Buffer buffer = handler.result();
//                        int sizeReaded = 0;
//                        do {
//                            long currentTime;
//                            if (downloadedWhithinOneSecond >= MAX_SPEED
//                                    && (((currentTime = System.currentTimeMillis()) - lastTime) < ONE_SECOND)) {
//                                try {
//                                    Thread.sleep(ONE_SECOND - (currentTime - lastTime));
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                downloadedWhithinOneSecond = 0;
//                                lastTime = System.currentTimeMillis();
//                            }
//
//                            context.response()
//                                    .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
//                                    .putHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"")
//                                    .putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked")
//                                    .end(buffer.getBuffer(sizeReaded, sizeReaded + SLOW_SPEED));
//                            sizeReaded += SLOW_SPEED;
//                        } while (sizeReaded < buffer.length());
//                    }
//                });
                break;
            default:
//                    try {
//                InputStream is = new FileInputStream(new File(FILE_DOWNLOAD_PATH));
//                BufferedInputStream bis = new BufferedInputStream(is);
//                while (bis.read() != -1) {
//                    Buffer buffer = new BufferImpl();
//                    buffer.appendBytes(bis.readAllBytes());
//                    response.end(buffer);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                String filename = FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH.substring(FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH.lastIndexOf("\\") + 1, FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH.length());
                FILE_SYSTEM.readFile(FILE_KOUTETSU_MAJO_ANNEROSE_DOWNLOAD_PATH, handler -> {
                    if (handler.result() == null) {
                        System.out.println(handler.result());
                    } else {
                        downloadUsingSendByte_OctetStream(context, filename, handler.result());
                    }
                });
                break;
        }
    }

    private void downloadUsingSendFile(RoutingContext context, String path) {
        String filename = path.substring(path.lastIndexOf("\\") + 1, path.length());
        context.response().
                putHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .putHeader(HttpHeaders.TRANSFER_ENCODING, "chunked")
                .sendFile(path);
    }

    private void downloadUsingSendBuffer_OctetStream(RoutingContext context, String path) {
        String filename = path.substring(path.lastIndexOf("\\") + 1, path.length());
        FILE_SYSTEM.readFile(path, handler -> {
            if (handler.succeeded()) {
                System.out.println("Read file octecstream - buffer");
                context.response()
                        .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .end(handler.result());
            } else {
                System.out.println("Read file: " + filename + " failed");
            }
        });
    }

    private void downloadUsingSendByte_OctetStream(RoutingContext context, String filename, Buffer buffer) {
        System.out.println("Read file octecstream - byte");
        System.out.println(buffer.length());
        context.response()
                .putHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .end(buffer);
    }

    private void downloadUsingSendBuffer_ApplicationJson(RoutingContext context, String path) {
        String filename = path.substring(path.lastIndexOf("\\") + 1, path.length());
        FILE_SYSTEM.readFile(path, handler -> {
            if (handler.succeeded()) {
                System.out.println("Read file json");
                context.response()
                        .putHeader("Content-Type", "application/json")
                        .end(handler.result());
            } else {
                System.out.println("Read file: " + filename + " failed");
            }
        });
    }

    @Override
    public void handleRequest() {
        ROUTER.get("/download").handler(context -> doGet(context));
        ROUTER.getWithRegex("/download/.*").handler(context -> download(context));
    }

    public class CustomInputStream extends InputStream {

        private static final int MAX_SPEED = 8 * 1024;
        private final long ONE_SECOND = 1000;
        private long downloadedWhithinOneSecond = 0L;
        private long lastTime = System.currentTimeMillis();

        private final InputStream inputStream;

        public CustomInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            lastTime = System.currentTimeMillis();
        }

        @Override
        public int read() throws IOException {
            long currentTime;
            if (downloadedWhithinOneSecond >= MAX_SPEED
                    && (((currentTime = System.currentTimeMillis()) - lastTime) < ONE_SECOND)) {
                try {
                    Thread.sleep(ONE_SECOND - (currentTime - lastTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downloadedWhithinOneSecond = 0;
                lastTime = System.currentTimeMillis();
            }
            int res = inputStream.read();
            if (res >= 0) {
                downloadedWhithinOneSecond++;
            }
            return res;
        }

//        Stream in;
//        long timestamp = System.currentTimeMillis();
//        int counter = 0;
//        int INTERVAL = 1000; // one second
//        int LIMIT = 1000; // bytes per INTERVAL
//
//        /**
//         * Read one byte with rate limiting
//         */
//        @Override
//        public int read() {
//            if (counter > LIMIT) {
//                long now = System.currentTimeMillis();
//                if (timestamp + INTERVAL >= now) {
//                    Thread.sleep(timestamp + INTERVAL - now);
//                }
//                timestamp = now;
//                counter = 0;
//            }
//            int res = in.read();
//            if (res >= 0) {
//                counter++;
//            }
//            return res;
//        }
        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }
}
