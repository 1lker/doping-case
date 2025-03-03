```
# Öğrenci Test Servisi

Bu proje, öğrencilerin test çözebileceği bir REST API servisidir. Spring Boot kullanılarak Java 21 ile geliştirilmiştir.

## Proje Özellikleri

- Öğrenci ve test veritabanı tabloları
- Öğrencilerin test katılımları ve cevapları
- Test performans takibi
- RESTful API endpoints
- Validasyonlar
- Caching
- Swagger Dokümantasyonu
- Unit Testler
- H2 in-memory veritabanı

## Teknolojiler

- Java 21
- Spring Boot 3.2.2
- Spring Data JPA
- Spring Validation
- Spring Cache
- Swagger/OpenAPI
- H2 Database
- JUnit 5
- Lombok

## Veritabanı Şeması

- `students`: Öğrenci bilgileri
- `tests`: Test bilgileri
- `questions`: Test soruları
- `options`: Soru seçenekleri
- `test_participations`: Öğrenci test katılımları
- `answers`: Öğrenci cevapları

## API Endpoints

### Öğrenci İşlemleri

- `GET /api/students`: Tüm öğrencileri listeler
- `GET /api/students/{id}`: ID'ye göre öğrenci getirir
- `GET /api/students/number/{studentNumber}`: Öğrenci numarasına göre öğrenci getirir
- `POST /api/students`: Yeni öğrenci oluşturur
- `PUT /api/students/{id}`: Öğrenci bilgilerini günceller
- `DELETE /api/students/{id}`: Öğrenciyi siler

### Test İşlemleri

- `GET /api/tests`: Tüm testleri listeler
- `GET /api/tests/{id}`: ID'ye göre test getirir
- `POST /api/tests`: Yeni test oluşturur
- `PUT /api/tests/{id}`: Test bilgilerini günceller
- `DELETE /api/tests/{id}`: Testi siler

### Test Katılım İşlemleri

- `GET /api/participations`: Tüm test katılımlarını listeler
- `GET /api/participations/{id}`: ID'ye göre test katılımı getirir
- `GET /api/participations/student/{studentId}`: Öğrenci ID'sine göre test katılımlarını listeler
- `GET /api/participations/test/{testId}`: Test ID'sine göre test katılımlarını listeler
- `POST /api/participations/start`: Test katılımı başlatır
- `POST /api/participations/answer`: Test sorusuna cevap gönderir
- `POST /api/participations/{participationId}/finish`: Test katılımını tamamlar

## Kurulum ve Çalıştırma

1. Java 21 yüklü olduğundan emin olun
2. Projeyi klonlayın
3. Proje dizininde: `./mvnw spring-boot:run`
4. API dokümantasyonu: `http://localhost:8080/swagger-ui.html`
5. H2 Console: `http://localhost:8080/h2-console`

## Örnek Kullanım

### Öğrenci Oluşturma

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ali","lastName":"Yılmaz","studentNumber":"123456"}'
```

### Test Oluşturma

```bash
curl -X POST http://localhost:8080/api/tests \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Matematik Testi",
    "description": "Temel matematik soruları",
    "durationMinutes": 60,
    "questions": [
      {
        "content": "2 + 2 = ?",
        "questionType": "MULTIPLE_CHOICE",
        "points": 10,
        "options": [
          {"content": "3", "isCorrect": false},
          {"content": "4", "isCorrect": true},
          {"content": "5", "isCorrect": false}
        ]
      }
    ]
  }'
```

### Test Katılımı Başlatma

```bash
curl -X POST http://localhost:8080/api/participations/start \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"testId":1}'
```

### Soruya Cevap Verme

```bash
curl -X POST http://localhost:8080/api/participations/answer \
  -H "Content-Type: application/json" \
  -d '{"participationId":1,"questionId":1,"selectedOptionId":2}'
```

### Testi Tamamlama

```bash
curl -X POST http://localhost:8080/api/participations/1/finish
```
```