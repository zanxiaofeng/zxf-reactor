package zxf.reactor.mono;

import reactor.core.publisher.Mono;

public class MonoTests {
    public static void main(String[] args) {
        Mono.empty()
                .subscribe(x -> System.out.println("case 1: will not touch"));

        Mono.empty()
                .map(x -> {
                    System.out.println("case 2: will not touch");
                    return "test";
                })
                .subscribe();

        Mono.empty().then()
                .subscribe(x -> System.out.println("case 3: will not touch"));

        Mono.empty().then()
                .map(x -> {
                    System.out.println("case 4: will not touch");
                    return "test";
                })
                .subscribe();

        Mono.empty().then()
                .doOnSuccess(x -> {
                    System.out.println("case 5: " + x);
                })
                .subscribe();

        Mono.empty().thenReturn("test")
                .map(x -> {
                    System.out.println("case 6: " + x);
                    return x + "-" + x;
                })
                .subscribe();

        Mono.just("test")
                .subscribe(x -> System.out.println("case 7: " + x));

        Mono.just("test").then()
                .subscribe(x -> System.out.println("case 8: will not touch" + x));

        Mono.just("test").then()
                .doOnNext(x -> System.out.println("case 9: will not touch" + x))
                .subscribe();
    }
}
