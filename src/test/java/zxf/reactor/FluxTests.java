package zxf.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;

public class FluxTests {
    Flux<Integer> evenNumbers = Flux.range(2, 2).filter(x -> x % 2 == 0);
    Flux<Integer> oddNumbers = Flux.range(1, 5).filter(x -> x % 2 == 1);

    @Test
    public void givenFluxes_whenConcatIsInvoked_thenConcat() {
        Flux<Integer> fluxOfIntegers = Flux.concat(
                evenNumbers,
                oddNumbers);

        StepVerifier.create(fluxOfIntegers)
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .expectComplete()
                .verify();
    }

    @Test
    public void givenFluxes_whenConcatWithIsInvoked_thenConcatWith() {
        Flux<Integer> fluxOfIntegers = evenNumbers.concatWith(oddNumbers);

        // same stepVerifier as in the concat example above
    }

    @Test
    public void givenFluxes_whenMergeIsInvoked_thenMerge() {
        Flux<Integer> fluxOfIntegers = Flux.merge(
                evenNumbers,
                oddNumbers);

        StepVerifier.create(fluxOfIntegers)
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .expectComplete()
                .verify();
    }

    @Test
    public void givenFluxes_whenCombineLatestIsInvoked_thenCombineLatest() {
        Flux<Integer> fluxOfIntegers = Flux.combineLatest(
                evenNumbers,
                oddNumbers,
                (a, b) -> a + b);

        StepVerifier.create(fluxOfIntegers)
                .expectNext(5) // 4 + 1
                .expectNext(7) // 4 + 3
                .expectNext(9) // 4 + 5
                .expectComplete()
                .verify();
    }

//    @Test
//    public void givenFluxes_whenMergeWithDelayedElementsIsInvoked_thenMergeWithDelayedElements() {
//        Flux<Integer> fluxOfIntegers = Flux.merge(
//                evenNumbers.delayElements(Duration.ofMillis(500L)),
//                oddNumbers.delayElements(Duration.ofMillis(300L)));
//
//        StepVerifier.create(fluxOfIntegers)
//                .expectNext(1)
//                .expectNext(2)
//                .expectNext(3)
//                .expectNext(5)
//                .expectNext(4)
//                .expectComplete()
//                .verify();
//    }


    @Test
    public void testMergeSequential() {
        Flux<Integer> fluxOfIntegers = Flux.mergeSequential(
                evenNumbers,
                oddNumbers);

        StepVerifier.create(fluxOfIntegers)
                .expectNext(2)
                .expectNext(4)
                .expectNext(1)
                .expectNext(3)
                .expectNext(5)
                .expectComplete()
                .verify();
    }


    @Test
    public void testBuffer() {
        Flux.range(1, 5)
                .buffer()
                .doOnNext(System.out::println)
                .subscribe();
    }

    @Test
    public void testCollect() {
        Flux<Integer> flux = Flux.range(1, 5)
                .collect(Collectors.toList())
                .doOnNext(System.out::println)
                .map(x -> x.stream().mapToInt(Integer::intValue).sum())
                .flux();

        flux.doOnNext(System.out::println)
                .subscribe();
    }
}


