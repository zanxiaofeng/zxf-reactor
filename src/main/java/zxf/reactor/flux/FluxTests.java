package zxf.reactor.flux;

import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class FluxTests {
    public static void main(String[] args) {
        case_1();
        case_2();
        case_3();
    }

    private static void case_1() {
        System.out.println("case 1: onNext|onNext|onComplete");
        Flux.just(1, 2).subscribe(x -> {
            System.out.println("onNext: " + x);
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 1");
        });
    }

    private static void case_2() {
        System.out.println("case 2: onComplete");
        Flux.empty().subscribe(x -> {
            System.out.println("onNext: will not be touched");
        }, e -> {
            System.out.println("onError: will not be touched");
        }, () -> {
            System.out.println("onComplete: case 2");
        });
    }

    private static void case_3() {
        System.out.println("case 3: onNext|onNext|onError");
        AtomicInteger initial = new AtomicInteger();
        Flux.generate(synchronousSink -> {
            Integer current = initial.incrementAndGet();
            if (current < 3) {
                synchronousSink.next(current);
                return;
            }
            if (current == 3) {
                synchronousSink.error(new RuntimeException("case 3"));
                return;
            }
            if (current < 6) {
                synchronousSink.next(current);
                return;
            }
            synchronousSink.complete();
        }).subscribe(x -> {
            System.out.println("onNext: " + x);
        }, e -> {
            System.out.println("onError: " + e.toString());
        }, () -> {
            System.out.println("onComplete: will not be touched");
        });
    }
}
