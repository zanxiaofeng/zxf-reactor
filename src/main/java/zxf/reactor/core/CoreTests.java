package zxf.reactor.core;

import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class CoreTests {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {
        MyPublisher<Integer> myPublisher = new MyPublisher(atomicInteger::incrementAndGet);

        MyFilterProcessor<Integer> myFilterProcessor = new MyFilterProcessor<>(myPublisher, x -> x % 2 == 0);

        MyMapProcessor<Integer, String> myMapProcessor = new MyMapProcessor<>(myFilterProcessor, x -> {
            return "**" + x + "**";
        });

        MySubscriber mySubscriber = new MySubscriber(value -> {
            System.out.println(Thread.currentThread() + "  onNext::" + value + ".");
        });
        myMapProcessor.subscribe(mySubscriber);
    }

    public static class MyPublisher<T> implements Publisher<T> {
        private Supplier<T> supplier;

        public MyPublisher(Supplier<T> supplier) {
            System.out.println(Thread.currentThread() + "  MyPublisher::cotr");
            this.supplier = supplier;
        }

        @Override
        public void subscribe(Subscriber<? super T> subscriber) {
            System.out.println(Thread.currentThread() + "  MyPublisher::subscribe");
            subscriber.onSubscribe(new MySubscription(supplier, subscriber));
        }
    }

    public static class MySubscription<T> implements Subscription {
        private Supplier<T> supplier;
        ;
        private Subscriber<? super T> subscriber;

        public MySubscription(Supplier<T> supplier, Subscriber<? super T> subscriber) {
            System.out.println(Thread.currentThread() + "  MySubscription::cotr");
            this.supplier = supplier;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long count) {
            System.out.println(Thread.currentThread() + "  MySubscription::request." + count);

            for (int i = 0; i < count; i++) {
                subscriber.onNext(supplier.get());
            }

            subscriber.onComplete();
        }

        @Override
        public void cancel() {
            System.out.println(Thread.currentThread() + "  MySubscription::cancel");
            subscriber.onComplete();
        }
    }

    public static class MyMapProcessor<T, R> implements Processor<T, R> {
        private Publisher<T> source;
        private Function<T, R> mapper;
        private Subscriber<? super R> actual;

        public MyMapProcessor(Publisher<T> source, Function<T, R> mapper) {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::cotr");
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public void subscribe(Subscriber<? super R> actual) {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::subscribe");
            this.actual = actual;
            source.subscribe(this);
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::onSubscribe");
            actual.onSubscribe(subscription);
        }

        @Override
        public void onNext(T value) {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::onNext." + value);
            actual.onNext(mapper.apply(value));
        }


        @Override
        public void onError(Throwable error) {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::onError." + error);
            actual.onError(error);
        }

        @Override
        public void onComplete() {
            System.out.println(Thread.currentThread() + "  MyMapProcessor::onComplete");
            actual.onComplete();
        }
    }

    public static class MyFilterProcessor<T> implements Processor<T, T> {
        private Publisher<T> source;
        private Predicate<T> predicate;
        private Subscriber<? super T> actual;

        public MyFilterProcessor(Publisher<T> source, Predicate<T> predicate) {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::cotr");
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public void subscribe(Subscriber<? super T> actual) {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::subscribe");
            this.actual = actual;
            source.subscribe(this);
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::onSubscribe");
            actual.onSubscribe(subscription);
        }

        @Override
        public void onNext(T value) {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::onNext." + value);
            if (predicate.test(value)) {
                actual.onNext(value);
            }
        }


        @Override
        public void onError(Throwable error) {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::onError." + error);
            actual.onError(error);
        }

        @Override
        public void onComplete() {
            System.out.println(Thread.currentThread() + "  MyFilterProcessor::onComplete");
            actual.onComplete();
        }
    }

    public static class MySubscriber<T> implements Subscriber<T> {
        private Consumer<T> consumer;

        public MySubscriber(Consumer<T> consumer) {
            System.out.println(Thread.currentThread() + "  MySubscriber::cotr");
            this.consumer = consumer;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println(Thread.currentThread() + "  MySubscriber::onSubscribe");
            subscription.request(100);
        }

        @Override
        public void onNext(T value) {
            System.out.println(Thread.currentThread() + "  MySubscriber::onNext." + value);
            this.consumer.accept(value);
            //注意：在此处Call subscription.request(100) 会导致循环调用
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println(Thread.currentThread() + "  MySubscriber::onError::" + throwable + ".");
        }

        @Override
        public void onComplete() {
            System.out.println(Thread.currentThread() + "  MySubscriber::onComplete.");
        }
    }
}
