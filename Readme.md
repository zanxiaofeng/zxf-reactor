# Core Classes
## org.reactivestreams:reactive-streams
- org.reactivestreams.Publisher
- org.reactivestreams.Subscriber
- org.reactivestreams.Subscription
- org.reactivestreams.Processor
- org.reactivestreams.FlowAdapters[]
## io.projectreactor:reactor-core
- reactor.core.CorePublisher
- reactor.core.CoreSubscriber
- reactor.core.Disposable
- reactor.core.Scannable
- reactor.core.publisher.ContextHolder
- reactor.core.publisher.Flux
- reactor.core.publisher.FluxSink
- reactor.core.publisher.Mono
- reactor.core.publisher.MonoSink
- reactor.core.publisher.InnerConsumer
- reactor.core.publisher.InnerOperator
- reactor.core.publisher.InnerProducer
- reactor.core.publisher.InternalEmptySink
- reactor.core.publisher.InternalManySink
- reactor.core.publisher.InternalOneSink
- reactor.core.publisher.InternalFluxOperator
- reactor.core.publisher.InternalMonoOperator
- reactor.core.scheduler.Scheduler


# Use case
## Mono
### Mono 与 Value
- ```从 Value 到 Ｍono```
- ```从 Ｍono 到 Value```
### Ｍono 与 Ｍono
- ```从 Ｍono<T> 到 Mono<E>```
- ```Ｍono<T> + Ｍono<E> => Mono<Z>```
### Mono 与 Function
- ```从 Function 到 Mono```
### Mono 与 Flux
- ```从 Ｍono 到 Ｆlux```
### Observability
- Mono<T> doOnNext(Consumer<? super T> onNext)
- Mono<T> doOnError(Consumer<? super Throwable> onError)
- Mono<T> doOnComplete(Runnable onComplete)
- Mono<T> doOn******(****)
### Trigger
- void subscribe(Subscriber<? super T> actual)
- Disposable subscribe(@Nullable Consumer<? super T> consumer, @Nullable Consumer<? super Throwable> errorConsumer, @Nullable Runnable completeConsumer)
- Disposable subscribe()
- T block()

## Flux
### Flux 与 Value
- ```从 Value 到 Ｆlux```
- ```从 Flux 到 Value```
### Flux 与 Flux
- ```从 Flux<T> 到 Ｆlux<E>```
- ```Flux<T> + Flux<T> => Flux<T>```
- ```Flux<T> + Flux<E> => Flux<T,E>```
### Flux 与 Function
- ```从 Function 到 Flux```
### Flux 与 Mono
- ```从 Flux 到 Ｍono```
###  Observability
- Flux<T> doOnNext(Consumer<? super T> onNext)
- Flux<T> doOnError(Consumer<? super Throwable> onError)
- Flux<T> doOnComplete(Runnable onComplete)
- Flux<T> doOn******(****)
### Trigger
- void subscribe(Subscriber<? super T> actual)
- Disposable subscribe(@Nullable Consumer<? super T> consumer, @Nullable Consumer<? super Throwable> errorConsumer, @Nullable Runnable completeConsumer)
- Disposable subscribe()
- T blockLast()
- T blockFirst()


# Core Class of Web Client
- org.springframework.http.client.reactive.ClientHttpConnector
- org.springframework.http.client.reactive.HttpComponentsClientHttpConnector
- org.springframework.http.client.reactive.JettyClientHttpConnector
- org.springframework.http.client.reactive.ReactorClientHttpConnector
- org.springframework.http.client.reactive.ReactorNetty2ClientHttpConnector
- org.springframework.http.client.reactive.JdkClientHttpConnector
- org.springframework.http.client.reactive.ClientHttpResponse
- org.springframework.http.client.reactive.ClientHttpRequest