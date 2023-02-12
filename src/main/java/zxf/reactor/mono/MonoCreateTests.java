package zxf.reactor.mono;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/*
    Mono.empty()
    Mono.never()
    Mono.just...
    Mono.error...
    Mono.delay...
    Mono.create(...)
    Mono.from...
    Mono.ignoreElements(...)
    Mono.defer...
    Mono.zip...
    Mono.firstWith...
    Mono.sequenceEqual...
    Mono.using...
    Mono.when...
 */
public class MonoCreateTests {
    public static void main(String[] args) throws InterruptedException {
        //Simple
        subscribe("Mono.empty", Mono.empty());
        subscribe("Mono.never", Mono.never());
        subscribe("Mono.just", Mono.just(1));
        subscribe("Mono.justOrEmpty", Mono.justOrEmpty(null));
        subscribe("Mono.justOrEmpty", Mono.justOrEmpty(Optional.empty()));
        subscribe("Mono.error", Mono.error(new Error("Customer error")));
        subscribe("Mono.delay", Mono.delay(Duration.ofSeconds(10)));

        //By Functional
        subscribe("Mono.create", Mono.create(monoSink -> monoSink.success("I'm from Mono.create")));
        subscribe("Mono.fromCallable", Mono.fromCallable(() -> "I'm from Mono.fromCallable"));
        subscribe("Mono.fromRunnable", Mono.fromRunnable(() -> System.out.println("I'm running from Mono.fromRunnable")));
        subscribe("Mono.fromSupplier", Mono.fromSupplier(() -> "I'm from Mono.fromSupplier"));
        subscribe("Mono.fromFuture", Mono.fromFuture(CompletableFuture.completedFuture("I'm from Mono.fromFuture")));

        //By Publisher
        subscribe("Mono.from", Mono.from(new Publisher<String>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long l) {
                        subscriber.onNext("I'm from Mono.from");
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        }));


        //By aggregate other Mono
        subscribe("Mono.defer", Mono.defer(() -> Mono.delay(Duration.ofSeconds(20))));


        //End
        Thread.currentThread().join(25000, 0);
    }

    private static <T> void subscribe(String name, Mono<T> mono) {
        mono.subscribe(x -> {
            System.out.println(LocalDateTime.now() + "::" + name + "::onNext: " + x + ".");
        }, e -> {
            System.out.println(LocalDateTime.now() + "::" + name + "::onError: " + e + ".");
        }, () -> {
            System.out.println(LocalDateTime.now() + "::" + name + "::onComplete.");
        });
    }
}


