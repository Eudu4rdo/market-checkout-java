package br.com.market.cart.infra.observability.logging;

import br.com.market.cart.infra.observability.metrics.MetricsCollector;
import br.com.market.cart.infra.observability.tracing.TraceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private final MetricsCollector metricsCollector;

    public LoggingInterceptor(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
        String requestId = UUID.randomUUID().toString();
        long startTime   = System.currentTimeMillis();

        MDC.put("requestId", requestId);
        MDC.put("requestMethod", httpServletRequest.getMethod());
        MDC.put("endpoint", httpServletRequest.getRequestURI());
        MDC.put("startTime", String.valueOf(startTime));

        TraceContext.startTrace();
        log.info("Request received: {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());

        httpServletRequest.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) {
        long startTime = (long) httpServletRequest.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        MDC.put("duration", String.valueOf(duration));
        MDC.put("statusCode", String.valueOf(httpServletResponse.getStatus()));

        metricsCollector.recordRequest(httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
        metricsCollector.recordLatency(httpServletRequest.getRequestURI(), duration);
        if (ex != null) {
            log.error("Request failed: {} {} status= {}, duration={}ms, requestID={}, traceID={}.", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), httpServletResponse.getStatus(), duration, MDC.get("requestId"), TraceContext.getCurrentTraceId(), ex);
        }else {
            log.info("Request completed: {} {} status= {},  duration={}ms, requestID={}, traceID={}.", httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), httpServletResponse.getStatus(), duration, MDC.get("requestId"), TraceContext.getCurrentTraceId());
        }
        TraceContext.endTrace();
        MDC.clear();
    }
}
