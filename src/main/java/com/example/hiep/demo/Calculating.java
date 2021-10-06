package com.example.hiep.demo;

import com.example.hiep.IRequestHandler;
import com.example.hiep.MainVerticle;
import com.example.hiep.error.HandleErrorVerticle;
import io.vertx.ext.web.RoutingContext;

public class Calculating extends MainVerticle implements IRequestHandler {

    private final String ADD = "add";
    private final String SUBTRACT = "subtract";
    private final String MULTIPLY = "multiply";
    private final String DEVIDE = "divide";

    private void doGet(RoutingContext context) {
        String first = context.request().getParam("first");
        String second = context.request().getParam("second");
        String operater = context.request().getParam("operater");

        System.out.println(first);
        System.out.println(second);
        System.out.println(operater);

        double a;
        double b;
        String error = null;
        try {
            a = Double.parseDouble(first);
        } catch (NumberFormatException e) {
            a = 0;
        }
        try {
            b = Double.parseDouble(second);
        } catch (NumberFormatException e) {
            b = 0;
        }
        if (!operater.matches("^(add|subtract|multiply|divide)$")) {
            error = "The operator is not correct.\n"
                    + "Only add, subtract, multiply, devide (+, -, *, /)";
        }

        double result = 0;
        switch (operater) {
            case ADD:
                result = a + b;
                break;
            case SUBTRACT:
                result = a - b;
                break;
            case MULTIPLY:
                result = a * b;
                break;
            case DEVIDE:
                if (b == 0) {
                    error = "The devided number (second) must not be 0";
                } else {
                    result = a / b;
                }
                break;
            default:
                error = "The operator is not correct.\n"
                        + "Only add, subtract, multiply, devide (+, -, *, /)";
        }
        
        System.out.println(error);
        if (error != null) {
            context.response().end(String.valueOf(error));
        } else {
            context.response().end(String.valueOf(result));
        }
    }

    @Override
    public void handleRequest() {
        ROUTER.get("/calculate/:operater/:first/:second")
                .handler(context -> doGet(context))
                .failureHandler(f -> new HandleErrorVerticle().redirectError404(f));
    }
}
