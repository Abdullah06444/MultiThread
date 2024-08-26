## RACE CONDITION 1/3
* Bir değişkeni artırttığımız zaman, bu işlemin gerçekleştiği zaman aralığında bir değişken azaltma veya herhangi
* başka işlemde girebilir. Bu büyük bir sorundur. Bundan nasıl kurtarabiliriz? MutualExclusion nasıl sağlayabiliriz?
* Yani ben çalışırken kimse araya girmesin. Yaptığım işlemin process'in sonuna kadar doğru çalıştığını bileyim,
* derdimiz budur. Bunun için işlemimizi process'i syncronized komutunu yapmalıyız. Böylelikle process'in o işlemin
* olduğu critical section da başka hiçbir işlem almasın demektir.
* Örneğin; counter = 5 iken bunu bir artırmak istersek;
* register1 = counter;              register2 = counter;
* register1 = register1 + 1;        register2 = register2 - 1;
* counter = register1;              counter = register2;

* Arka planda çalışırken sırasıyla varsayalım böyle processler gerçekleşti.
* register1 = counter;         ---> register1 = 5
* register1 = register1 + 1;   ---> register1 = 6
* register2 = counter;         ---> register2 = 5
* register2 = register2 - 1;   ---> register2 = 4
* counter = register1;         ---> counter = 6
* counter = register2;         ---> counter = 4

-----

## RACE CONDITION 2/3
* Bir diğer sorunumuzda SIMPLE PROGRESS sırayla üretmek. Yani önce üretici üretmesi sonra tüketici tüketmesi gerekir.
* Bunu iki değişken için bir binary değişkenle flag gibi birisi beklerken diğeri beklemesin şeklinde.

-----

## RACE CONDITION 3/3
* BOUNDED WAITING - https://youtu.be/R_EgvEOhV9U?t=785 Şadi hocam kral
* Bize flexibility sağlar, esneklik. Yani biz aynı anda 5 tane ürünü banda koyabiliyorsak Producer çalıştıktan hemen
* sonra Consumer çalışmak zorunda değil. Producer hâlâ çalışmaya devam edebilir. O bant dolana kadar çalışmaya devam
* eder. Sonra diğer Consumer başlar. Dolayısıyla Bounded Waiting aslında bir süre Consumer çalışmasa da bu esnekliği
* sağlayıp programın devam etmesini sağlamak.
