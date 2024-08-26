public class MainSadiThread1 {

    public static void main(String[] args){

        // Producer vs. Consumer örneği üzerinden gidilecek
        Data data = new Data();
        data.value = 0;
        // Bu üretici tüketici örneğinde biri ürünü üretirken diğeri tüketecektir.
        // Bunu tasarlarken üretilmeden tüketilmemesini sağlamamız gerekir. Yani value 0(sıfır)
        // altına düşmemeli ve 2 olmamalı.
        // Bir değişkeni değiştirmede, yani artırma ve eksiltmede "RACE CONDITION" denilen bir durum vardır.
        // Birden fazla işlem gerektirir. Bu işlem araya girilebilen şekildedir ve atomic değildir.
        // Bu yüzden aşağıdaki Producer ve Consumer classlarında farzedelim ki peş peşe for loop ile
        // 10 işlem yaptırmaya kalksak normal şartlarda değer value 0(sıfır) olmalıydı.
        Producer producer = new Producer(data);
        Consumer consumer = new Consumer(data);
        producer.start(); // producer direk extend ile Thread class'ından inherit miras alındığı için start()
        // metodunu çalıştırabilirken, consumer için aynı şeyi söyleyemeyiz. Dolayısıyla bunu önce bir Thread'e
        // atmamız gerekecektir.
        //consumer.start();
        Thread thread = new Thread(consumer); // Bu sayede bizim consumer Thread'e verilmiş olur.
        thread.start();

        // Thread'ler shared variable bir değişkeni paylaşıyor olmasıdır.
    }
}

/** **** RACE CONDITION 1/3 *****
 * Bir değişkeni artırttığımız zaman, bu işlemin gerçekleştiği zaman aralığında bir değişken azaltma veya herhangi
 * başka işlemde girebilir. Bu büyük bir sorundur. Bundan nasıl kurtarabiliriz? MutualExclusion nasıl sağlayabiliriz?
 * Yani ben çalışırken kimse araya girmesin. Yaptığım işlemin process'in sonuna kadar doğru çalıştığını bileyim,
 * derdimiz budur. Bunun için işlemimizi process'i syncronized komutunu yapmalıyız. Böylelikle process'in o işlemin
 * olduğu critical section da başka hiçbir işlem almasın demektir.
 * Örneğin; counter = 5 iken bunu bir artırmak istersek;
 * register1 = counter;              register2 = counter;
 * register1 = register1 + 1;        register2 = register2 - 1;
 * counter = register1;              counter = register2;
 *
 * Arka planda çalışırken sırasıyla varsayalım böyle processler gerçekleşti.
 * register1 = counter;         ---> register1 = 5
 * register1 = register1 + 1;   ---> register1 = 6
 * register2 = counter;         ---> register2 = 5
 * register2 = register2 - 1;   ---> register2 = 4
 * counter = register1;         ---> counter = 6
 * counter = register2;         ---> counter = 4
 *
 * */

/* Java'da Thread create oluşturmanın iki farklı yöntemi vardır.
 * Aslında birbirlerine benzerlerdir. İlki Thread extend etmektir.
 * Extend ettiğimiz classtan üretilen bütün objectler direk Thread olarak üretilir.
 * */
class Producer extends Thread{ // tercih edilmeyen Thread yaklaşımı ve bu da doğrudur.
    // Neden tercih edilmez? Çünkü, oop tasarımda inherit ilişkisinde kullanılır extend kavramı.
    // Bir objeden kalıtım olarak almamız gereken bir bilgi varsa onu extend etmek yerine sadece
    // o objeyi almak daha mantıklıdır. Çünkü Java'da tek bir class inherit edilebilir ve multiple
    // inheritance yok. Bu yüzden bir tane inherit hakkımızı Thread'de kullanırsak daha hiçbir objeyi
    // miras yoluyla inherit edemez oluruz.

    final Data data;

    public Producer(Data data){
        this.data = data;
    }

    public void run(){

        for(int i = 0; i < 10; i++)
            System.out.println("Producer " + ++data.value + " ürün üretti.");
        System.out.println("\n\n");
        synchronized (data){
            for(int i = 0; i < 10; i++)
                System.out.println("Synchronized içerisinde Producer " + ++data.value + " ürün üretti.");
        }
    }
}
/* İkincisi ise Runnable implement etmektir.
* Herhangi birşey Thread olacaksa Runnable olmalıdır.
* Bu Runnable hazır kütüphane interface'i bize abstract olarak run()
* metotu tanımladığından kullanmamızı istiyor.
* Bu run() metotu Thread çalışması için gereklidir.
* */
class Consumer implements Runnable{ // tercih edilen Thread yaklaşımı ki, bu class'tan
    // tanımlanan objeye Thread thread = new Thread(<objectname>) şeklinde Thread çağırırız.

    final Data data;

    public Consumer(Data data){
        this.data = data;
    }

    @Override
    public void run() {

        for(int i = 0; i < 10; i++)
            System.out.println("Consumer " + --data.value + " ürün tüketti.");
        System.out.println("\n\n");
        synchronized (data){
            for(int i = 0; i < 10; i++)
                System.out.println("Synchronized içerisinde Consumer " + --data.value + " ürün tüketti.");
        }
    }
}

class Data{

    int value;
}
