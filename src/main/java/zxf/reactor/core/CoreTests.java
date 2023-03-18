package zxf.reactor.core;

import org.reactivestreams.Processor;
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
                        subscriber.onComplete();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Processor<Integer, String> processor = new Processor<Integer, String>() {
            private Subscription subscription;
            private Subscriber<? super String> subscriber;

            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                this.subscriber = subscriber;
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long l) {
                        subscription.request(l);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
            }

            @Override
            public void onNext(Integer integer) {
                subscriber.onNext(integer + "-" + integer);
            }

            @Override
            public void onError(Throwable error) {
                subscriber.onError(error);
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(1000);
            }

            @Override
            public void onNext(String next) {
                System.out.println("onNext::" + next + ".");
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

        publisher.subscribe(processor);
        processor.subscribe(subscriber);
    }
}
