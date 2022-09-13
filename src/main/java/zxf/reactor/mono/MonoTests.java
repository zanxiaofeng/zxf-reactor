package zxf.reactor.mono;

import reactor.core.publisher.Mono;

public class MonoTests {
    public static void main(String[] args) {
        case_1();
        case_2();
        case_3();
    }

    private static void case_1() {
        System.out.println("case 1: onNext|onComplete");
        Mono.just("test").subscribe(x -> {
            System.out.println("onNext: " + x);
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 1");
        });
    }

    private static void case_2() {
        System.out.println("case 2: onComplete");
        Mono.empty().subscribe(x -> {
            System.out.println("onNext: will not be touched");
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 2");
        });
    }

    private static void case_3() {
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
