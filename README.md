# Yapay Zeka Destekli Seyahat Rehberi Mobil Uygulaması


Bu proje, Sakarya Üniversitesi bitirme ödevi kapsamında hazırladığım yapay zeka destekli seyahat rehberi mobil uygulamasıdır. Bu projede, yapay zekanın mobil uygulamalara entegrasyon sürecini ele aldım. Özellikle, Google Gemini API'sini kullanarak yapay zekanın mobil uygulamalara nasıl entegre edilebileceğini inceledim ve uyguladım. Bu entegrasyon sayesinde kullanıcılar, seyahat planlamalarında daha akıllı ve kişiselleştirilmiş öneriler alabilecekler. Projem, seyahat rehberliği alanında yapay zeka teknolojilerinin pratik kullanımını gösteren bir örnek niteliğindedir.

## Giriş
Günümüzde seyahat etme alışkanlıklarındaki değişimlerle paralel olarak geliştirilen "Travel Guide" adlı mobil uygulama, kullanıcıların seyahat deneyimlerini kolaylaştırmayı hedefliyor. Kullanıcı dostu arayüzü ve kapsamlı özellikleriyle öne çıkan bu uygulama, seyahat tutkunlarının ihtiyaçlarını karşılamak ve seyahat etmeyi daha keyifli hale getirmek için tasarlanmıştır. Uygulama, kullanıcıların gezdikleri yerler hakkında deneyimlerini paylaşmalarına olanak tanırken, yapay zeka destekli ülke tanıtım sayfası ve Chatbot gibi özelliklerle seyahat planlamasını kolaylaştırıyor.
## Kullanılan Yöntem ve Yaklaşım
Bu proje, yazılım geliştirmenin etkinliğini artırmak ve kod organizasyonunu daha modüler hale getirmek adına MVVM (Model-View-ViewModel) mimarisini temel almaktadır. MVVM mimarisi, her bir kullanıcı arayüzü bileşeninin (view) iş mantığından ayrılarak daha düzenli ve modüler bir kod yapısı sağlar. Bu sayede, proje boyunca kullanıcı arayüzü ve iş mantığı arasındaki ilişki daha açık bir şekilde tanımlanmıştır.
MVVM mimarisinin yanı sıra, proje düzeni temiz kod ve sürdürülebilirlik prensiplerine dayanmaktadır. Kodlar, amaçlarına göre farklı klasörlerde organize edilmiş ve her bir bölümün sorumlulukları net bir şekilde belirlenmiştir. Bu yapı, kodun daha okunabilir ve bakımı yapılabilir olmasını sağlar. Proje boyunca kullanılan katmanlar arasında Model, ViewModel ve View katmanları bulunmaktadır. Model katmanında uygulama veri modelleri yer alır ve her model, belirli bir veri tipini temsil eder. ViewModel katmanında, kullanıcı arayüzü bileşenlerini temsil eden view model sınıfları bulunur. Bu sınıflar, veri işleme işlemlerini gerçekleştirir ve gerekli verileri kullanıcı arayüzüne sağlar. View katmanında ise kullanıcı arayüzünün görüntülendiği bileşenler yer alır; Activity'ler, Fragment'ler ve XML dosyaları bu katmanda bulunur.

Ayrıca projede, veri işleme işlemleri için Repository deseni kullanılmıştır. Repository sınıfları, veri kaynaklarına erişim sağlar ve iş mantığını içerir. Bu sayede, veri kaynaklarıyla (örneğin Firebase veritabanı, API çağrıları) iletişim kurulur ve veri işleme işlemleri gerçekleştirilir. Projede ayrıca, harici servislerle iletişim kurulan bir Service katmanı ve yardımcı işlevler ve araçların yer aldığı bir Utils katmanı bulunmaktadır. Bu yapılar, projenin genel yapısını belirleyen temel unsurlar olup kodun düzenli, okunabilir ve bakımı yapılabilir olmasını sağlamaktadır.
## Kullanılan Teknolojiler
Projede, mobil uygulama geliştirmek için Android işletim sistemi tercih edilmiştir. Android Studio, resmi bir entegre geliştirme ortamı olarak kullanılmış ve Java ile Kotlin programlama dillerini desteklemiştir. Yazılım mimarisi olarak MVVM (Model-View-ViewModel) benimsenmiş ve yazılım dili olarak Kotlin tercih edilmiştir. Veritabanı yönetimi için Firebase kullanılmıştır. Firebase, kullanıcı kimlik doğrulama işlemleri için Firebase Authentication, veri depolama için Realtime Database ve büyük veriler için Firebase Storage hizmetlerini sağlamıştır. Projede API kullanımı yapılmış olup, ülke bilgileri için RestCountries API, yapay zeka entegrasyonu için ise Google Gemini API kullanılmıştır. API çağrıları için Retrofit ve JSON Converter kütüphaneleri tercih edilmiştir. Proje sürecinde kod versiyon kontrolü ve işbirliği için Git ve GitHub kullanılmıştır. Ayrıca, kullanıcı tabanının geniş ve küresel olması göz önüne alınarak çoklu dil desteği projenin önemli bir özelliği olarak ele alınmıştır.

## Uygulama Arayüzü
Uygulama tasarımı, kullanım kolaylığı ve işlevselliği ön planda tutularak dikkatle oluşturulmuştur. Kullanıcı deneyimini optimize etmek amacıyla, gerekli tüm ekranlar birbiriyle bağlantılı bir şekilde tasarlanmıştır. Kullanıcı, uygulama içerisinde rahatça gezinebileceği giriş yap, kayıt ol, şifremi unuttum, anasayfa, keşfet, gönderi detay, gönderi ekleme, gönderi düzenleme, ülkeler, ChatBot, harita, profil, profil düzenleme ve ayarlar gibi ekranlarla karşılaşır. Bu sayede, kullanıcılar ihtiyaç duydukları bilgilere ve işlevlere hızlı ve etkili bir şekilde ulaşabilirler. Ayrıca, kullanım kolaylığını artırmak için uygulamada drawer menu ve bottom menu de bulunmaktadır. Bu menüler sayesinde kullanıcılar, uygulama içinde hızlı ve kolay bir şekilde gezinebilirler.
Uygulamanın ‘Kayıt Ol’ sayfası, yeni kullanıcıların ad, soyad, e-posta adresi gibi temel bilgilerini girerek üye olmalarını sağlar. ‘Giriş Yap’ ekranı, kayıtlı kullanıcıların kullanıcı adı ve şifreleri ile uygulamaya erişmelerine olanak tanır. Kullanıcıların şifrelerini unutmaları durumunda ise ‘Şifremi Unuttum’ sayfası üzerinden e-posta adreslerine şifre sıfırlama bağlantısı gönderilir ve kolayca yeni şifre oluşturulabilir.
Uygulama tasarımı, kullanıcı dostu ve işlevsel özelliklerle zenginleştirilmiştir, böylece kullanıcılar ihtiyaç duydukları bilgilere ve işlevlere kolayca erişebilirler. Anasayfa'dan başlayarak, kullanıcılar profil bilgilerini görebilir, şehir fotoğrafları sunan bir slider aracılığıyla görsel bir tur yapabilir ve en son eklenen gönderilere hızlıca göz atabilirler. Keşfet sayfası, çeşitli gönderileri kategorilere göre filtreleme imkanı sunarken, Gönderi Detay sayfası her gönderinin ayrıntılarını derinlemesine inceleme fırsatı verir. Kullanıcılar, Gönderi Ekleme ve Gönderi Düzenleme ekranlarında yeni içerikler oluşturabilir veya mevcut içerikleri güncelleyebilirler. Ülkeler sayfası, RestCountries API ile zenginleştirilmiş detaylı ülke bilgileri sunar. ChatBot sayfasında kullanıcıların soruları hızlıca yanıtlanırken, Harita sayfası üzerinde Google Maps ve Places API entegrasyonu sayesinde kullanıcılar diledikleri lokasyonları keşfedebilir. Profil ve Profil Düzenleme sayfaları kişisel bilgilerin yönetimini kolaylaştırırken, Ayarlar sayfası üzerinden dil seçimi, şifre değişiklikleri gibi temel ayarlar yapılabilmektedir. Drawer Menu ve Bottom Navigation Bar ise uygulama içi navigasyonu basit ve etkili kılar. Bu kapsamlı ve entegre arayüz tasarımı, kullanıcıların uygulama içinde verimli ve keyifli vakit geçirmelerini sağlar.

## Sonuçlar 
TravelGuide, seyahat alışkanlıklarınıza uyum sağlayarak deneyimlerinizi kolaylaştıran bir uygulamadır. Harita entegrasyonları ve yapay zeka destekli bir chatbot gibi özellikler sunar. Kullanıcılar, deneyimlerini paylaşabilecekleri özel bir sayfadan faydalanabilir, bu da diğer kullanıcıların daha bilinçli seyahat planları yapmalarına yardımcı olur. Başlangıçta hedeflenen özellikler başarıyla entegre edilmiş olsa da, TravelGuide sürekli olarak geliştirilmekte ve yeni özellikler eklenmektedir. Bu da uygulamanın potansiyelini artırarak kullanıcı deneyimini iyileştirir.
