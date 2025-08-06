package com.bookingplatform.config;

import brave.Tracing;
import brave.baggage.BaggageField;
import brave.baggage.CorrelationScopeConfig;
import brave.baggage.CorrelationScopeCustomizer;
import brave.baggage.CorrelationScopeConfig.SingleCorrelationField;
import brave.context.log4j2.ThreadContextScopeDecorator;
import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.sampler.Sampler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;
import io.micrometer.tracing.propagation.Propagator;
import io.micrometer.tracing.brave.bridge.BravePropagator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TracingConfig {
    private static final Logger logger = LogManager.getLogger(TracingConfig.class);
    static {
        // Enable MDC context propagation for Reactor
        Hooks.enableAutomaticContextPropagation();
    }

    @Bean
    public brave.Tracing braveTracing() {
        return Tracing.newBuilder()
                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder()
                        .addScopeDecorator(mdcScopeDecorator())
                        .build())
                .sampler(Sampler.ALWAYS_SAMPLE)
                .build();
    }

    @Bean
    public Tracer tracer(brave.Tracing braveTracing) {
        return new BraveTracer(
                braveTracing.tracer(),
                new BraveCurrentTraceContext(braveTracing.currentTraceContext()),
                new BraveBaggageManager()
        );
    }

    @Bean
    public CorrelationScopeCustomizer correlationScopeCustomizer() {
        return builder -> {
            // Create and configure fields
            BaggageField traceIdField = BaggageField.create("traceId");
            BaggageField spanIdField = BaggageField.create("spanId");
            
            // Clear existing fields and add our fields
            builder.clear()
                .add(SingleCorrelationField.create(traceIdField))
                .add(SingleCorrelationField.create(spanIdField));
        };
    }

    @Bean
    public CurrentTraceContext.ScopeDecorator mdcScopeDecorator() {
        // Create the fields
        BaggageField traceIdField = BaggageField.create("traceId");
        BaggageField spanIdField = BaggageField.create("spanId");
        
        // Build the scope decorator with our fields
        return ThreadContextScopeDecorator.newBuilder()
            .clear()
            .add(SingleCorrelationField.create(traceIdField))
            .add(SingleCorrelationField.create(spanIdField))
            .build();
    }

    @Bean
    public BraveBaggageManager braveBaggageManager() {
        return new BraveBaggageManager();
    }

    @Bean
    public Propagator propagator(brave.Tracing tracing) {
        return new BravePropagator(tracing);
    }

    @Bean
    public ObservationRegistry observationRegistry(Tracer tracer, Propagator propagator) {
        ObservationRegistry registry = ObservationRegistry.create();
        
        // Add tracing handlers
        registry.observationConfig()
            .observationHandler(new DefaultTracingObservationHandler(tracer))
            .observationHandler(new PropagatingReceiverTracingObservationHandler(tracer, propagator))
            .observationHandler(new PropagatingSenderTracingObservationHandler(tracer, propagator));
            
        return registry;
    }
    
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
    
    @Bean
    public WebFilter mdcContextFilter(Tracer tracer) {
        return (exchange, chain) -> {
            // Get or create a span for the current request
            Span currentSpan = tracer.currentSpan();
            
            if (currentSpan != null) {
                // Create a new MDC context map
                Map<String, String> contextMap = new HashMap<>();
                
                // Add trace and span IDs to the context
                String traceId = currentSpan.context().traceId();
                String spanId = currentSpan.context().spanId();
                
                // Add to MDC for synchronous logging
                MDC.put("traceId", traceId);
                MDC.put("spanId", spanId);
                
                // Add to context for reactive logging
                contextMap.put("traceId", traceId);
                contextMap.put("spanId", spanId);
                
                // Log the trace and span IDs for debugging
                logger.debug("MDC Filter - Setting traceId: {}, spanId: {}", traceId, spanId);
                
                // Create a new context with the updated MDC
                Context context = Context.of("mdc", contextMap);
                
                // Continue the chain with the updated context
                return chain.filter(exchange)
                    .contextWrite(ctx -> context)
                    .doOnEach(signal -> {
                        // Update MDC for each signal in the reactive stream
                        if (!signal.isOnComplete() && !signal.isOnError()) {
                            // Only update if we have a value
                            signal.getContextView().<Map<String, String>>getOrEmpty("mdc")
                                .ifPresent(mdc -> {
                                    logger.trace("Updating MDC from signal: {}", mdc);
                                    MDC.setContextMap(mdc);
                                });
                        }
                    })
                    .doFinally(signalType -> {
                        // Clear MDC after the request is complete
                        logger.trace("Clearing MDC");
                        MDC.clear();
                    });
            } else {
                logger.warn("No current span found in mdcContextFilter");;
            }
            
            // If there's no current span, just continue the chain
            return chain.filter(exchange);
        };
    }
}
