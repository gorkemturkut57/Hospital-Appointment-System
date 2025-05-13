# Hastane Randevu ve Hasta Takip Sistemi

Bu proje, hastaların randevu alabileceği ve doktorların bu randevuları yönetebildiği bir hasta takip sistemidir. Spring Boot tabanlı bir back-end uygulamasıdır.

## Özellikler

### Kullanıcı Rolleri

- **Hasta**:
  - Kayıt olma ve giriş yapma
  - Randevu oluşturma
  - Geçmiş randevuları görüntüleme
  - Doktor notlarını görüntüleme

- **Doktor**:
  - Giriş yapma
  - Kendisine gelen randevuları görüntüleme
  - Randevuları onaylama veya reddetme
  - Randevulara özel notlar ekleme

### Randevu Sistemi

- Randevu tarihi ve saati seçimi
- Aynı saatte çakışan randevuların engellenmesi
- Randevu durumları: Beklemede, Onaylandı, Reddedildi

### Hasta Takibi

- Doktorlar randevulara özel not ekleyebilir
- Hasta geçmiş randevularını ve doktor notlarını görebilir

## Teknolojik Altyapı

- **Framework**: Spring Boot
- **Güvenlik**: Spring Security
- **Veritabanı**: MySQL
- **ORM**: Spring Data JPA
- **View Template**: Thymeleaf
- **UI**: Bootstrap

## Kurulum ve Çalıştırma

### Gereksinimler

- JDK 17 veya üzeri
- Maven
- MySQL

### Adımlar

1. Projeyi klonlayın: `git clone [repo-url]`
2. MySQL'de bir veritabanı oluşturun
3. `src/main/resources/application.properties` dosyasındaki veritabanı bağlantı ayarlarını düzenleyin
4. Projeyi derleyin: `mvn clean install`
5. Uygulamayı çalıştırın: `mvn spring-boot:run` veya `java -jar target/backendvideo-0.0.1-SNAPSHOT.jar`
6. Tarayıcıda `http://localhost:8080` adresine gidin

## Projenin Yapısı

- **Controller**: Kullanıcı etkileşimlerini yöneten sınıflar
- **Model**: Veritabanı varlıklarını temsil eden sınıflar
- **Repository**: Veritabanı işlemleri için arayüzler
- **Service**: İş mantığını içeren sınıflar
- **Security**: Yetkilendirme ve kimlik doğrulama yapılandırması

## Katkıda Bulunma

1. Bu depoyu forklayın
2. Özellik dalı oluşturun (`git checkout -b feature/amazing-feature`)
3. Değişikliklerinizi commit edin (`git commit -m 'Add some amazing feature'`)
4. Dalınıza push yapın (`git push origin feature/amazing-feature`)
5. Bir Pull Request açın

## Lisans

Bu proje [lisans adı] altında lisanslanmıştır - ayrıntılar için LICENSE dosyasına bakın. 