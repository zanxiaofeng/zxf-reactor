package zxf.reactor.webclient;

import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientTests2 {
    public static void main(String[] args) throws InterruptedException {
        WebClient.builder().baseUrl("https://www.163.com")
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
                        System.out.println("###########################Body: " + new String(x));

                        return response.mutate().body(new String(x)).build();
                    });
                }))
                .build().get().retrieve()
                .bodyToMono(String.class)
                .subscribe(x-> System.out.println("###########################Body: " + x));

        Thread.sleep(100000);
    }
}
