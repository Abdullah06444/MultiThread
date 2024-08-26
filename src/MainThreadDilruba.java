import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class MainThreadDilruba {

    public static void main(String[] args) {

        //concurrency paralel işlemleri yapmak için Thread tanımlarız.
        //Runnable bir bakıma run edilecek program parçacığı demektir.
        Runnable runnable = () -> System.out.println("Ben bir runnable programıyım.");
        Thread thread = new Thread(runnable);
        thread.start();

        //Paralel çalışma örneği için bir tane Thread yeterli değildir, yani bir tane gişe işlem yapmamalıdır.
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //10 tane Thread paralelde açtık.


        // Runnable birşey return etmiyor, void metot gibi düşünebilir bu interface arayüzünü.
        List<Runnable> runnableList = Arrays.asList(

                () -> System.out.println("Runnable 1"),
                () -> System.out.println("Runnable 2"),
                () -> System.out.println("Runnable 3"),
                () -> System.out.println("Runnable 4"),
                () -> System.out.println("Runnable 5"),
                () -> System.out.println("Runnable 6"),
                () -> System.out.println("Runnable 7"),
                () -> System.out.println("Runnable 8")
        );

        // executorService sadece Callable için çağrılır. Runnable için ise IntStream kullanmalı.
        IntStream.range(0,8).forEach(i -> executorService.submit(runnableList.get(i)));

        // tek tek yazmak yerine çok büyük
        IntStream.range(0,100).forEach(i -> executorService.submit(
                () -> System.out.println("Task " + i + " executed by " + Thread.currentThread().getName()) // Yazdığımız 10 tane Thread
                // hangisi bu metotu çalıştıracak, currentThread().getName() ile erişebiliriz.
        ));



        // Callable ise birşey return etmek zorunda, bu interface arayüzü.
        List<Callable<Integer>> callableList = Arrays.asList(

                () -> { int a = 1; System.out.println("Callable 1"); return a; },
                () -> { int a = 2; System.out.println("Callable 2"); return a; },
                () -> { int a = 3; System.out.println("Callable 3"); return a; },
                () -> { int a = 4; System.out.println("Callable 4"); return a; }
        );

        // Bu metot, buna herhangi bir List elemanı veridğimizde onun içerisindeki her bir
        // metotu bir Thread atıp paralelde çalıştıracak.
        try {
            executorService.invokeAll(callableList); // paralelde çalışacağı için hata durumu vardır. throw InterruptedException fırlatır.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // try-catch metodunda ya da throw diyerek bunu gidermeliyiz.

        // List<Future<returnType>> x, şeklinde ifade biçimiyle return ettiğimiz değerleri sonuçları aslında biz bir List'e atarız.
        List<Future<Integer>> futureList = null;
        try {
            futureList = executorService.invokeAll(callableList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        futureList.forEach(f -> {
            try {
                System.out.println("Result " + f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });



        //Paralel çalışma örneği için bir tane Thread yeterli değildir, yani bir tane gişe işlem yapmamalıdır.
        ExecutorService executorService2 = Executors.newCachedThreadPool();
        // Yukarı satırdaki gibi 10 tane Thread paralel manuel açmak yerine bunu Java8 kararına bırakıp
        // en optimum seviyede thread sayısını açmasını isteyebiliriz newCachedThreadPool() ile

        // Runnable birşey return etmiyor, void metot gibi düşünebilir bu interface arayüzünü.
        List<Runnable> runnableList2 = Arrays.asList(

                () -> System.out.println("Runnable 11"),
                () -> System.out.println("Runnable 12"),
                () -> System.out.println("Runnable 13"),
                () -> System.out.println("Runnable 14"),
                () -> System.out.println("Runnable 15"),
                () -> System.out.println("Runnable 16"),
                () -> System.out.println("Runnable 17"),
                () -> System.out.println("Runnable 18")
        );

        // executorService sadece Callable için çağrılır. Runnable için ise IntStream kullanırız.
        IntStream.range(0,8).forEach(i -> executorService2.submit(runnableList2.get(i)));

        // tek tek yazmak yerine çok büyük
        IntStream.range(0,100).forEach(i -> executorService2.submit(
                () -> System.out.println("Task " + i + " executed by " + Thread.currentThread().getName()) // Yazdığımız 10 tane Thread
                // hangisi bu metotu çalıştıracak, currentThread().getName() ile erişebiliriz.
        ));


    }
}
