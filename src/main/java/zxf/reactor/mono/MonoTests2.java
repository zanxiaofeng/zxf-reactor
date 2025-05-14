package zxf.reactor.mono;

import reactor.core.publisher.Mono;

public class MonoTests2 {
    public static void main(String[] args) {
        Mono.empty()
                .then()
                .doOnSuccess(i -> System.out.println("On success: " + i))
                .doOnError(i -> System.out.println("On error: " + i))
                .doOnNext(i -> System.out.println("On next: " + i))
                .block();

        Mono.empty()
                .then(Mono.just("Good bye 1"))
                .doOnSuccess(i -> System.out.println("On success: " + i))
                .doOnError(i -> System.out.println("On error: " + i))
                .doOnNext(i -> System.out.println("On next: " + i))
                .block();

        Mono.just("Hello World")
                .then(Mono.just("Good bye 2"))
                .doOnSuccess(i -> System.out.println("On success: " + i))
                .doOnError(i -> System.out.println("On error: " + i))
                .doOnNext(i -> System.out.println("On next: " + i))
                .block();

        try {
            Mono.error(new RuntimeException("Something wrong 1"))
                    .then(Mono.just("Good bye 3"))
                    .doOnSuccess(i -> System.out.println("On success: " + i))
                    .doOnError(i -> System.out.println("On error: " + i))
                    .doOnNext(i -> System.out.println("On next: " + i))
                    .block();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Mono.error(new RuntimeException("Something wrong 2"))
                    .then(Mono.error(new RuntimeException("Something very wrong")))
                    .doOnSuccess(i -> System.out.println("On success: " + i))
                    .doOnError(i -> System.out.println("On error: " + i))
                    .doOnNext(i -> System.out.println("On next: " + i))
                    .block();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
