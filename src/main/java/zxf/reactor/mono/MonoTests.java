package zxf.reactor.mono;

import reactor.core.publisher.Mono;

public class MonoTests {
    public static void main(String[] args) {
        System.out.println("case 1: onNext|onComplete");

        Mono<String> just = Mono.just("test");
        just.subscribe(x -> {
            System.out.println("onNext-1: " + x + ".");
        }, e -> {
            System.out.println("onError-1: will not be touched.");
        }, () -> {
            System.out.println("onComplete-1: case 1.");
        });
        just.subscribe(x -> {
            System.out.println("onNext-2: " + x + ".");
        }, e -> {
            System.out.println("onError-2: will not be touched.");
        }, () -> {
            System.out.println("onComplete-2: case 1.");
        });
        System.out.println("case 1: " + just.block());

        System.out.println("case 2: onComplete");
        Mono.empty().subscribe(x -> {
            System.out.println("onNext: will not be touched.");
        }, e -> {
            System.out.println("onError: will not be touched.");
        }, () -> {
            System.out.println("onComplete: case 2.");
        });

        System.out.println("case 3: onError.");
        Mono.error(new RuntimeException("case 3")).subscribe(x -> {
            System.out.println("onNext: will not be touched.");
        }, e -> {
            System.out.println("onError: " + e.toString() + ".");
        }, () -> {
            System.out.println("onComplete: will not be touched.");
        });

        System.out.println("case 4: publish multiple times.");
        Mono<String> mono = Mono.just("multiple");
        mono.subscribe(x -> {
            System.out.println("onNext: " + x + ".");
        }, e -> {
            System.out.println("onError: will not be touched.");
        }, () -> {
            System.out.println("onComplete: case 4.1.");
        });
        mono.subscribe(x -> {
            System.out.println("onNext: " + x + ".");
        }, e -> {
            System.out.println("onError: will not be touched.");
        }, () -> {
            System.out.println("onComplete: case 4.2.");
        });
    }
}
