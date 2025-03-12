# Student Test Service / Öğrenci Test Servisi

A RESTful service that allows students to take tests, answer questions, and view their performance. Built with Spring Boot and Java 21.

Öğrencilerin test çözebileceği, sorulara cevap verebileceği ve performanslarını görüntüleyebileceği RESTful bir servis. Spring Boot ve Java 21 ile geliştirilmiştir.

## Features / Özellikler

- Student management (CRUD operations)
- Test management with questions and options
- Test participation tracking
- Answer submission and evaluation
- Performance analytics
- RESTful API with proper documentation
- Caching for improved performance
- Comprehensive validation
- Unit tests
- Pagination for list endpoints
- JWT Authentication and Authorization

---

- Öğrenci yönetimi (CRUD işlemleri)
- Sorular ve seçeneklerle test yönetimi
- Test katılım takibi
- Cevap gönderme ve değerlendirme
- Performans analitiği
- Dokümantasyonlu RESTful API
- Performans iyileştirmesi için önbellekleme
- Kapsamlı doğrulama
- Birim testleri
- Liste uç noktaları için sayfalama
- JWT Kimlik Doğrulama ve Yetkilendirme

## Technologies / Teknolojiler

- Java 21
- Spring Boot 3.2.2
- Spring Data JPA
- Spring Validation
- Spring Cache
- Spring Security
- JWT (JSON Web Token)
- Swagger/OpenAPI
- H2 In-memory Database
- JUnit 5 & Mockito
- Maven

## Architecture / Mimari

The project follows a layered architecture:

Proje, katmanlı bir mimari izler:

### Layers / Katmanlar

1. **Controller Layer / Kontrolcü Katmanı**
   - Handles HTTP requests and responses
   - Input validation
   - HTTP'den gelen istekleri ve yanıtları işler
   - Girdi doğrulaması

2. **Service Layer / Servis Katmanı**
   - Business logic
   - Transaction management
   - Caching
   - İş mantığı
   - İşlem yönetimi
   - Önbellekleme

3. **Repository Layer / Depo Katmanı**
   - Data access
   - Veri erişimi

4. **Model Layer / Model Katmanı**
   - Entity definitions
   - Relationships
   - Varlık tanımları
   - İlişkiler

5. **Security Layer / Güvenlik Katmanı**
   - Authentication
   - Authorization
   - JWT handling
   - Kimlik doğrulama
   - Yetkilendirme
   - JWT işleme

### Database Schema / Veritabanı Şeması

- **students**: Student information (name, surname, student number)
- **tests**: Test information (name, description, duration)
- **questions**: Test questions (content, type, points)
- **options**: Question options (content, correctness)
- **test_participations**: Student test participations (start time, end time, score)
- **answers**: Student answers to questions (selected option)
- **users**: User information for authentication
- **roles**: User roles for authorization
- **user_roles**: Many-to-many relationship between users and roles

---

- **students**: Öğrenci bilgileri (ad, soyad, öğrenci numarası)
- **tests**: Test bilgileri (ad, açıklama, süre)
- **questions**: Test soruları (içerik, tür, puan)
- **options**: Soru seçenekleri (içerik, doğruluk)
- **test_participations**: Öğrenci test katılımları (başlangıç zamanı, bitiş zamanı, puan)
- **answers**: Öğrencilerin sorulara verdiği cevaplar (seçilen seçenek)
- **users**: Kimlik doğrulama için kullanıcı bilgileri
- **roles**: Yetkilendirme için kullanıcı rolleri
- **user_roles**: Kullanıcılar ve roller arasındaki çoka-çok ilişki

## API Endpoints / API Uç Noktaları

### Authentication / Kimlik Doğrulama

- `POST /api/auth/login`: Login and get JWT token / Giriş yapın ve JWT token alın
- `POST /api/auth/register`: Register a new user / Yeni kullanıcı kaydı oluşturun

### Student Operations / Öğrenci İşlemleri

- `GET /api/students`: List all students (paginated) / Tüm öğrencileri listeler (sayfalanmış)
- `GET /api/students/all`: List all students without pagination / Tüm öğrencileri sayfalanmadan listeler
- `GET /api/students/{id}`: Get student by ID / ID'ye göre öğrenci getirir
- `GET /api/students/number/{studentNumber}`: Get student by student number / Öğrenci numarasına göre öğrenci getirir
- `POST /api/students`: Create new student / Yeni öğrenci oluşturur
- `PUT /api/students/{id}`: Update student / Öğrenci bilgilerini günceller
- `DELETE /api/students/{id}`: Delete student / Öğrenciyi siler

### Test Operations / Test İşlemleri

- `GET /api/tests`: List all tests (paginated) / Tüm testleri listeler (sayfalanmış)
- `GET /api/tests/all`: List all tests without pagination / Tüm testleri sayfalanmadan listeler
- `GET /api/tests/{id}`: Get test by ID / ID'ye göre test getirir
- `POST /api/tests`: Create new test / Yeni test oluşturur
- `PUT /api/tests/{id}`: Update test / Test bilgilerini günceller
- `DELETE /api/tests/{id}`: Delete test / Testi siler

### Test Participation Operations / Test Katılım İşlemleri

- `GET /api/participations`: List all participations (paginated) / Tüm katılımları listeler (sayfalanmış)
- `GET /api/participations/all`: List all participations without pagination / Tüm katılımları sayfalanmadan listeler
- `GET /api/participations/{id}`: Get participation by ID / ID'ye göre katılım getirir
- `GET /api/participations/student/{studentId}`: List participations by student ID / Öğrenci ID'sine göre katılımları listeler
- `GET /api/participations/test/{testId}`: List participations by test ID / Test ID'sine göre katılımları listeler
- `POST /api/participations/start`: Start a test / Test katılımı başlatır
- `POST /api/participations/answer`: Submit an answer / Cevap gönderir
- `POST /api/participations/{participationId}/finish`: Finish a test / Test katılımını tamamlar

## Setup and Running / Kurulum ve Çalıştırma

### Prerequisites / Gereksinimler

- Java 21 or higher / Java 21 veya üstü
- Maven 3.8+ / Maven 3.8+

### Running Locally / Yerel Olarak Çalıştırma

1. Clone the repository / Depoyu klonlayın
   ```bash
   git clone https://github.com/yourusername/student-test-service.git
   cd student-test-service
   ```

2. Build the project / Projeyi derleyin
   ```bash
   ./mvnw clean install
   ```

3. Run the application / Uygulamayı çalıştırın
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access the application / Uygulamaya erişin
   - API: `http://localhost:8080/api/`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

### H2 Database Connection / H2 Veritabanı Bağlantısı

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Authentication / Kimlik Doğrulama

The application uses JWT (JSON Web Token) for authentication. To access protected endpoints, you need to:

Uygulama, kimlik doğrulama için JWT (JSON Web Token) kullanır. Korumalı uç noktalara erişmek için:

1. Register a user / Bir kullanıcı kaydı oluşturun
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{
       "username": "student1",
       "email": "student1@example.com",
       "password": "password123",
       "studentId": 1
     }'
   ```

2. Login to get a token / Token almak için giriş yapın
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "username": "student1",
       "password": "password123"
     }'
   ```

3. Use the token in subsequent requests / Sonraki isteklerde token'ı kullanın
   ```bash
   curl -X GET http://localhost:8080/api/students \
     -H "Authorization: Bearer YOUR_JWT_TOKEN"
   ```

## Caching Strategy / Önbellekleme Stratejisi

The application uses Spring's caching mechanism to improve performance:

Uygulama, performansı artırmak için Spring'in önbellekleme mekanizmasını kullanır:

- `students`: Caches the list of all students / Tüm öğrencilerin listesini önbelleğe alır
- `student`: Caches individual students by ID / Öğrencileri ID'ye göre önbelleğe alır
- `studentByNumber`: Caches students by student number / Öğrencileri öğrenci numarasına göre önbelleğe alır
- `tests`: Caches the list of all tests / Tüm testlerin listesini önbelleğe alır
- `test`: Caches individual tests by ID / Testleri ID'ye göre önbelleğe alır
- `participations`: Caches the list of all participations / Tüm katılımların listesini önbelleğe alır
- `participation`: Caches individual participations by ID / Katılımları ID'ye göre önbelleğe alır
- `participationsByStudent`: Caches participations by student ID / Katılımları öğrenci ID'sine göre önbelleğe alır
- `participationsByTest`: Caches participations by test ID / Katılımları test ID'sine göre önbelleğe alır
- `studentsPage`: Caches paginated student results / Sayfalanmış öğrenci sonuçlarını önbelleğe alır
- `testsPage`: Caches paginated test results / Sayfalanmış test sonuçlarını önbelleğe alır
- `participationsPage`: Caches paginated participation results / Sayfalanmış katılım sonuçlarını önbelleğe alır

## Testing / Test Etme

Run the tests with:

Testleri şu şekilde çalıştırın:

```bash
./mvnw test
```

## Project Evaluation / Proje Değerlendirmesi

### Strengths / Güçlü Yönler

- Well-structured layered architecture / İyi yapılandırılmış katmanlı mimari
- Comprehensive API with proper documentation / Uygun dokümantasyona sahip kapsamlı API
- Effective use of caching to improve performance / Performansı artırmak için etkili önbellekleme kullanımı
- Proper validation and error handling / Uygun doğrulama ve hata yönetimi
- Unit tests for controllers / Kontrolcüler için birim testler
- Pagination for list endpoints / Liste uç noktaları için sayfalama
- JWT Authentication and Authorization / JWT Kimlik Doğrulama ve Yetkilendirme

### Areas for Improvement / İyileştirme Alanları

- Add more comprehensive unit tests for services and repositories / Servisler ve depolar için daha kapsamlı birim testler ekleyin
- Implement integration tests / Entegrasyon testleri uygulayın
- Add more advanced search and filtering capabilities / Daha gelişmiş arama ve filtreleme özellikleri ekleyin
- Implement refresh token mechanism / Yenileme token mekanizması uygulayın
- Add password reset functionality / Şifre sıfırlama işlevselliği ekleyin

## License / Lisans

This project is licensed under the MIT License - see the LICENSE file for details.

Bu proje MIT Lisansı altında lisanslanmıştır - detaylar için LICENSE dosyasına bakın.
```