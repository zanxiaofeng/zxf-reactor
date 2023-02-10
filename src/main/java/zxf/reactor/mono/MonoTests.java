package zxf.reactor.mono;

import reactor.core.publisher.Mono;

public class MonoTests {
    public static void main(String[] args) {
        System.out.println("case 1: onNext|onComplete");
        Mono.just("test").subscribe(x -> {
            System.out.println("onNext: " + x);
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 1");
        });

        System.out.println("case 2: onComplete");
        Mono.empty().subscribe(x -> {
            System.out.println("onNext: will not be touched");
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 2");
        });

        System.out.println("case 3: onError");
        Mono.error(new RuntimeException("case 3")).subscribe(x -> {
            System.out.println("onNext: will not be touched");
        }, e -> {
            System.out.println("onError: " + e.toString());
        }, () -> {
            System.out.println("onComplete: will not be touched");
        });
    }
}
