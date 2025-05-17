package zxf.reactor.webclient.logging;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Slf4j
public class LoggingExchangeFilter implements ExchangeFilterFunction {
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return next.exchange(interceptRequest(request)).map(this::interceptResponseByJoin);
    }

    private ClientRequest interceptRequest(ClientRequest request) {
        return ClientRequest.from(request)
                .body((outputMessage, context) -> request.body().insert(new ClientHttpRequestDecorator(outputMessage) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        return super.writeWith(Mono.from(body).doOnSuccess(dataBuffer -> logRequest(request, dataBuffer, log::info)));
                    }
                }, context))
                .build();
    }

    public ClientResponse interceptResponse(ClientResponse response) {
        Flux<DataBuffer> body = response.bodyToFlux(DataBuffer.class)
                .doOnNext(x -> System.out.println("doOnNext"))
                .doOnCancel(() -> System.out.println("doOnCancel"))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnError((x) -> System.out.println("doOnError"))
                .doOnRequest(x -> System.out.println("doOnRequest"))
                .doOnSubscribe(x -> System.out.println("doOnSubscribe"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"));
        return ClientResponse.from(response).body(body).build();
    }

    public ClientResponse interceptResponseByJoin(ClientResponse response) {
        Flux<DataBuffer> body1 = response.bodyToFlux(DataBuffer.class)
                .doOnNext(x -> System.out.println("doOnNext"))
                .doOnCancel(() -> System.out.println("doOnCancel"))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnError((x) -> System.out.println("doOnError"))
                .doOnRequest(x -> System.out.println("doOnRequest"))
                .doOnSubscribe(x -> System.out.println("doOnSubscribe"))
                .doOnTerminate(() -> System.out.println("doOnTerminate"));
        Flux<DataBuffer> body2 = DataBufferUtils.join(body1).doOnNext(dataBuffer -> {
            log.info("Before read response, readPosition={}, readableByteCount={}", dataBuffer.readPosition(), dataBuffer.readableByteCount());
            logResponse(response, dataBuffer, log::info);
            log.info("After read response, readPosition={}, readableByteCount={}", dataBuffer.readPosition(), dataBuffer.readableByteCount());
        }).flux();
        return ClientResponse.from(response).body(body2).build();
    }

    private void logRequest(ClientRequest request, DataBuffer dataBuffer, Consumer<String> logger) {
        logger.accept("=================================================Request begin=================================================");
        logger.accept("URI             : " + request.url());
        logger.accept("Methed          : " + request.method());
        logger.accept("Headers         : " + removeSensitiveHeaders(request.headers()));
        logger.accept("Request Body    : " + dataBuffer.toString(StandardCharsets.UTF_8));
        logger.accept("=================================================Request end=================================================");
    }

    private void logResponse(ClientResponse response, DataBuffer dataBuffer, Consumer<String> logger) {
        logger.accept("=================================================Response begin=================================================");
        logger.accept("Status code     : " + response.statusCode());
        logger.accept("Headers         : " + response.headers().asHttpHeaders());
        logger.accept("Response Body   : " + dataBuffer.toString(StandardCharsets.UTF_8));
        logger.accept("=================================================Response end=================================================");
    }

    private HttpHeaders removeSensitiveHeaders(HttpHeaders originalHttpHeaders) {
        HttpHeaders clearHttpHeaders = new HttpHeaders();
        clearHttpHeaders.putAll(originalHttpHeaders);
        clearHttpHeaders.remove("Token");
        clearHttpHeaders.remove("Authorization");
        return clearHttpHeaders;
    }
}
