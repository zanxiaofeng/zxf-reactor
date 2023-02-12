package zxf.reactor.core;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public class CoreTests {
    public static void main(String[] args) {
        Publisher<Integer> publisher = new Publisher<Integer>() {
            private Integer seed = 0;

            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                this.seed += 1;
                subscriber.onSubscribe(new Subscription() {
                    Integer current = seed;

                    @Override
                    public void request(long l) {
                        subscriber.onNext(current);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(1000);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext::" + integer + ".");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError::" + throwable + ".");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete.");
            }
        };

        publisher.subscribe(subscriber);
        publisher.subscribe(subscriber);
        publisher.subscribe(subscriber);
        publisher.subscribe(subscriber);
        publisher.subscribe(subscriber);
    }
}
