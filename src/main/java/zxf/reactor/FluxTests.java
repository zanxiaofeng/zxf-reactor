package zxf.reactor;

import reactor.core.publisher.Flux;

public class FluxTests {
    public static void main(String[] args) throws InterruptedException {
        flux_case_1();
    }

    public static void flux_case_1() throws InterruptedException {
        Flux<Integer> flux = Flux.just(1, 2, 3, 4);
        flux.map(x -> x * x).subscribe(x -> System.out.println("*" + x));
        Thread.sleep(4000);
        flux.map(x -> x + x).subscribe(x -> System.out.println("+" + x));
    }
}
