package br.com.market.cart.infra.observability.tracing;

import java.util.UUID;

public class TraceContext {
    private static final ThreadLocal<String> currentTraceId = new ThreadLocal<>();

    public static void startTrace() {
        currentTraceId.set(UUID.randomUUID().toString());
    }

    public static String getCurrentTraceId() {
        return currentTraceId.get();
    }

    public static void endTrace() {
        currentTraceId.remove();
    }
}
