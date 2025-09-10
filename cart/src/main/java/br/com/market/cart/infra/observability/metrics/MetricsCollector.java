package br.com.market.cart.infra.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class MetricsCollector {
    private final MeterRegistry meterRegistry;

    public MetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordRequest(String endpoint, String method) {
        Counter.builder("http_requests_total")
                .tag("endpoint", endpoint)
                .tag("method", method)
                .register(meterRegistry).increment();
    }

    public void recordLatency(String endpoint, long durationMs) {
        Timer.builder("http_request_latency")
                .tag("endpoint", endpoint)
                .register(meterRegistry)
                .record(durationMs, TimeUnit.MILLISECONDS);
    }
}
