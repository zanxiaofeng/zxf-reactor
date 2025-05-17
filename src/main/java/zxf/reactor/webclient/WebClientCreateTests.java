package zxf.reactor.webclient;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.eclipse.jetty.client.HttpClient;
import zxf.reactor.webclient.factory.ClientHttpConnectorFactory;

import java.net.URI;
import java.util.Map;

public class WebClientCreateTests {
    public static void main(String[] args) throws InterruptedException {
//        WebClient webClient1 = WebClient.create();
//        System.out.println(webClient1.get().uri("https://www.163.com").retrieve().bodyToMono(String.class)
//                .doFinally((x) -> System.out.println("#######################doFinally"))
//                .doOnTerminate(() -> System.out.println("#######################doOnTerminate"))
//                .doOnError((x) -> System.out.println("#######################doOnError"))
//                .doOnSuccess((x) -> System.out.println("#######################doOnSuccess"))
//                .doOnNext((x) -> System.out.println("#######################doOnNext"))
//                .block());
//
//
//        WebClient webClient2 = WebClient.create("https://www.163.com");
//        webClient2.get().retrieve().bodyToMono(String.class)
//                .doFinally((x) -> System.out.println("#######################doFinally"))
//                .doOnError((x) -> System.out.println("#######################doOnError"))
//                .doOnNext((x) -> System.out.println("#######################doOnNext"))
//                .doOnSuccess((x) -> System.out.println("#######################doOnSuccess" + x))
//                .doOnTerminate(() -> System.out.println("#######################doOnTerminate"))
//                .subscribe();




        WebClient webClient3 = WebClient.builder().baseUrl("https://www.163.com?project={project}")
                .clientConnector(ClientHttpConnectorFactory.jettyClientHttpConnector())
                .filter(ExchangeFilterFunction.ofRequestProcessor((reqest)->{
                    System.out.println("URL: " + reqest.url());
                    System.out.println("Method: " + reqest.method());
                    System.out.println("Headers: " + reqest.headers());
                    System.out.println("Cookie: " + reqest.cookies());
                    return Mono.just(reqest);
                }))
                .filter(ExchangeFilterFunction.ofResponseProcessor(response -> {
                    return response.bodyToMono(byte[].class).map(x->{
                        System.out.println("Status: " + response.statusCode());
                        System.out.println("Headers: " + response.headers().asHttpHeaders());
                        System.out.println("Body: " + new String(x));

                        return response.mutate().body(Flux.just(new DefaultDataBufferFactory().wrap(x))).build();
                    });
                }))
                .defaultHeader("ABC", "123")
                .defaultCookie("ABC", "123")
                .defaultUriVariables(Map.of("project", "123")).build();
        WebClient.ResponseSpec  responseSpec = webClient3.get().retrieve();

        responseSpec.bodyToMono(String.class)
                .doFinally((x) -> System.out.println("#######################doFinally"))
                .doOnError((x) -> System.out.println("#######################doOnError"))
                .doOnNext((x) -> System.out.println("#######################doOnNext"))
                .doOnSuccess((x) -> System.out.println("#######################doOnSuccess"+x))
                .doOnTerminate(() -> System.out.println("#######################doOnTerminate"))
                .subscribe();

        Thread.sleep(10000);

        System.out.println("XXXXXXXXXX" + responseSpec.bodyToMono(String.class).block());

        Thread.sleep(10000);


//        WebClient webClient4 = WebClient.builder().baseUrl("https://www.163.com?project={project}")
//                .defaultHeader("ABC", "123")
//                .defaultCookie("ABC", "123")
//                .defaultUriVariables(Map.of("project", "123")).build();
//        Mono<String> response = webClient4.get().retrieve().bodyToMono(String.class);
//
//        response
//                .doFinally((x) -> System.out.println("#######################doFinally"))
//                .doOnError((x) -> System.out.println("#######################doOnError"))
//                .doOnNext((x) -> System.out.println("#######################doOnNext"))
//                .doOnSuccess((x) -> System.out.println("#######################doOnSuccess" + x))
//                .doOnTerminate(() -> System.out.println("#######################doOnTerminate"))
//                .subscribe();
//
//        Thread.sleep(10000);
//
//        System.out.println("XXXXXXXXXX" + response.block());
//
//        Thread.sleep(10000);
    }
}
