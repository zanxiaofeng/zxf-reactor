package zxf.reactor.flux;

import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class FluxTests {
    public static void main(String[] args) {
        System.out.println("case 1: onNext|onNext|onComplete");
        Flux.just(1, 2)
                .doOnNext((x) -> {
                    System.out.println("doOnNext: " + x);
                })
                .doOnError(e -> {
                    System.out.println("doOnError: will not be touched");
                })
                .doOnComplete(() -> {
                    System.out.println("doOnComplete: case 1");
                })
                .subscribe(x -> {
                    System.out.println("onNext: " + x);
                }, e -> {
                    System.out.println("onError: will not be touched");
                }, () -> {
                    System.out.println("onComplete: case 1");
                });


        System.out.println("\ncase 2: onComplete");
        Flux.empty()
                .doOnNext((x) -> {
                    System.out.println("doOnNext: will not be touched");
                })
                .doOnError(e -> {
                    System.out.println("doOnError: will not be touched");
                })
                .doOnComplete(() -> {
                    System.out.println("doOnComplete: case 2");
                })
                .subscribe(x -> {
                    System.out.println("onNext: will not be touched");
                }, e -> {
                    System.out.println("onError: will not be touched");
                }, () -> {
                    System.out.println("onComplete: case 2");
                });


        System.out.println("\ncase 3: onNext|onNext|onError");
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
                })
                .doOnNext((x) -> {
                    System.out.println("doOnNext: " + x);
                })
                .doOnError(e -> {
                    System.out.println("doOnError: " + e.toString());
                })
                .doOnComplete(() -> {
                    System.out.println("doOnComplete: will not be touched");
                })
                .subscribe(x -> {
                    System.out.println("onNext: " + x);
                }, e -> {
                    System.out.println("onError: " + e.toString());
                }, () -> {
                    System.out.println("onComplete: will not be touched");
                });
    }
}
