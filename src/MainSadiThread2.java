public class MainSadiThread2 {

    public static void main(String[] args){

        // Producer vs. Consumer örneği üzerinden gidilecek
        Data2 data = new Data2();
        data.value = 0;
        data.flag = false;
        // Bu üretici tüketici örneğinde biri ürünü üretirken diğeri tüketecektir.
        // Bunu tasarlarken üretilmeden tüketilmemesini sağlamamız gerekir. Yani value 0(sıfır)
        // altına düşmemeli ve 2 olmamalı.
        Producer2 producer = new Producer2(data);
        Consumer2 consumer = new Consumer2(data);
        producer.start();
        //consumer.start(); //error line
        Thread thread = new Thread(consumer);
        thread.start();
    }
}

/** **** RACE CONDITION 2/3 *****
 * Bir diğer sorunumuzda SIMPLE PROGRESS sırayla üretmek. Yani önce üretici üretmesi sonra tüketici tüketmesi gerekir.
 * Bunu iki değişken için bir binary değişkenle flag gibi birisi beklerken diğeri beklemesin şeklinde.
 *
 *
 *
 *  **** RACE CONDITION 3/3 *****
 * BOUNDED WAITING - https://youtu.be/R_EgvEOhV9U?t=785 Şadi hocam kral
 * Bize flexibility sağlar, esneklik. Yani biz aynı anda 5 tane ürünü banda koyabiliyorsak Producer çalıştıktan hemen
 * sonra Consumer çalışmak zorunda değil. Producer hâlâ çalışmaya devam edebilir. O bant dolana kadar çalışmaya devam
 * eder. Sonra diğer Consumer başlar. Dolayısıyla Bounded Waiting aslında bir süre Consumer çalışmasa da bu esnekliği
 * sağlayıp programın devam etmesini sağlamak.
 * */

class Producer2 extends Thread{

    final Data2 data;

    public Producer2(Data2 data){
        this.data = data;
    }

    public void run(){

        synchronized (data){
            if(!data.flag)
                for(int i = 0; i < 10; i++)
                    System.out.println("Synchronized içerisinde Producer " + ++data.value + " ürün üretti.");
            data.flag = true;
        }
    }
}

class Consumer2 implements Runnable{

    final Data2 data;

    public Consumer2(Data2 data){
        this.data = data;
    }

    @Override
    public void run() {

        synchronized (data){
            if(data.flag)
                for(int i = 0; i < 10; i++)
                    System.out.println("Synchronized içerisinde Consumer " + --data.value + " ürün tüketti.");
            data.flag = false;
        }
    }
}

class Data2{

    int value;
    boolean flag;
}
