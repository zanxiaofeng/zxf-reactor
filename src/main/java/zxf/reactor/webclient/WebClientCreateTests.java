package zxf.reactor.webclient;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                        return response.mutate().body(new String(x)).build();
                    });
                }))
                .defaultHeader("ABC", "123")
                .defaultCookie("ABC", "123")
                .defaultUriVariables(Map.of("project", "123")).build();
        webClient3.get().retrieve().bodyToMono(String.class)
                .doFinally((x) -> System.out.println("#######################doFinally"))
                .doOnError((x) -> System.out.println("#######################doOnError"))
                .doOnNext((x) -> System.out.println("#######################doOnNext"))
                .doOnSuccess((x) -> System.out.println("#######################doOnSuccess"+x))
                .doOnTerminate(() -> System.out.println("#######################doOnTerminate"))
                .subscribe();

        Thread.sleep(1000000);
    }
}
