# Enterprise Java Observability Stack

**Versione:** 1.0.0  
**Java Version:** 17  
**Spring Boot Version:** 3.2.0  
**Author:** Gaetano Cerciello - IT Support Specialist & Java Developer

## 📌 Descrizione Progetto

Applicazione Spring Boot 3 dimostrativa per showcase di competenze ibride (Development + Ops/Support L2).

L'app integra:
- ✅ **Spring Boot 3** con Java 17
- ✅ **Apache Camel** per simulazione flussi di dati
- ✅ **Prometheus + Micrometer** per observability
- ✅ **Spring Boot Actuator** per monitoring
- ✅ **REST API** con operazioni CRUD simulate
- ✅ **SHA-256 Security Utility** per data masking
- ✅ **Unit Test** con JUnit 5 e Mockito

## 🎯 Obiettivi del Progetto

Dimostrare su Rocky Linux:
1. Competenze di **Java Development** avanzato
2. Competenze di **DevOps/Support L2** (deployment, monitoring)
3. Progettazione di architetture **osservabili** e **scalabili**
4. Integrazione di **Prometheus + Grafana** per monitoring

## 🚀 Quick Start

### 1. Compilare il progetto

```bash
cd /path/to/enterprise-java-observability-stack
mvn clean package
```

### 2. Eseguire l'applicazione

```bash
java -jar target/enterprise-java-observability-stack-1.0.0.jar
```

### 3. Accedere agli endpoint

```bash
# Health check
curl http://localhost:8080/actuator/health

# Prometheus metrics
curl http://localhost:8080/actuator/prometheus

# Creare utente
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"123","role":"USER"}'

# Recuperare utenti
curl http://localhost:8080/api/users
```

## 📂 Struttura del Progetto

```
src/main/java/com/observability/
├── app/
│   └── Application.java              # Entry point Spring Boot
├── camel/
│   └── CamelRoutes.java              # Rotte Camel (simulazione dati)
├── controller/
│   └── UserController.java           # REST API (CRUD)
├── model/
│   └── User.java                     # Entità User
└── util/
    └── SecurityUtils.java            # SHA-256 hashing

src/test/java/com/observability/
├── UserControllerTest.java           # Test controller (10+ test cases)
├── SecurityUtilsTest.java            # Test security (8+ test cases)
└── CamelRoutesTest.java              # Test Camel (6+ test cases)

src/main/resources/
└── application.properties            # Configurazione Prometheus/Actuator
```

## 🔧 Componenti Principali

### Application.java
- Entry point dell'applicazione Spring Boot 3
- Configurazione del component scanning
- Messaggio di startup con endpoint key

### CamelRoutes.java
- 4 rotte Apache Camel
- Simulazione di flussi dati realista
- Integrazione con REST controller

### UserController.java
- Endpoint REST CRUD complete
- Integrazione con Apache Camel
- Misurazione tempo di risposta
- SHA-256 hashing integrato

### SecurityUtils.java
- Hashing SHA-256 di stringhe
- Data masking demo ("VALENTINANAPPI")
- Verifica password

### application.properties
- Prometheus metrics enablement
- Spring Boot Actuator configuration
- Camel configuration
- Logging configuration

## 🧪 Test Coverage

### UserControllerTest (9 test cases)
- Create user
- Get all users
- Get user by ID (success + not found)
- Update user (success + not found)
- Delete user (success + not found)
- Health check

### SecurityUtilsTest (9 test cases)
- SHA-256 hashing
- Hash consistency
- Different inputs
- Empty string handling
- Password verification (success + failure)
- Data masking demo
- Case sensitivity
- Long strings
- Special characters

### CamelRoutesTest (6 test cases)
- Routes configuration
- Process data route
- CRUD operation route
- Response handler route
- Camel context state

## 📊 Metriche Prometheus Disponibili

L'applicazione espone automaticamente:

```
# JVM Metrics
jvm_memory_used_bytes
jvm_threads_current
jvm_gc_memory_allocated_bytes

# HTTP Metrics
http_server_requests_seconds
http_requests_total

# Tomcat Metrics
tomcat_threads_current
tomcat_sessions_active_current

# Custom Metrics (da aggiungere)
users_created_total
users_deleted_total
crud_operation_duration_seconds
```

## 🔗 Endpoint Disponibili

| Endpoint | Metodo | Descrizione |
|----------|--------|-------------|
| `/api/users` | GET | Recupera tutti gli utenti |
| `/api/users/{id}` | GET | Recupera utente specifico |
| `/api/users` | POST | Crea nuovo utente |
| `/api/users/{id}` | PUT | Aggiorna utente |
| `/api/users/{id}` | DELETE | Elimina utente |
| `/api/users/health-check` | POST | Health check |
| `/actuator/health` | GET | Spring Boot health |
| `/actuator/prometheus` | GET | Prometheus metrics |
| `/actuator/metrics` | GET | Lista metriche |
| `/actuator/info` | GET | Info applicazione |

## 🐧 Rocky Linux Deployment

Vedere **BUILD_GUIDE.md** per:
- Installazione Java 17 e Maven su Rocky Linux
- Compilazione del JAR
- Deployment e systemd service
- Integrazione Prometheus + Grafana

## 💡 Punti Chiave per LinkedIn Recruiter

✨ **Technical Stack Dimostra:**
1. **Modern Java**: Spring Boot 3 con Java 17 LTS
2. **Enterprise Integration**: Apache Camel (message routing)
3. **Observability**: Prometheus + Micrometer (monitoring-ready)
4. **Security**: SHA-256 hashing, data masking
5. **Testing**: JUnit 5 + Mockito (comprehensive test suite)
6. **DevOps**: Rocky Linux deployment, systemd integration
7. **REST API**: CRUD operations con error handling
8. **Code Quality**: Lombok, logging, best practices

🎯 **Showcasing:**
- Competenze ibride Dev + Ops
- Architettura scalabile e osservabile
- Deployment production-ready su Linux
- Professional code structure e testing

## 📦 Dipendenze Key

```xml
- spring-boot-starter-web (REST API)
- spring-boot-starter-actuator (Monitoring)
- micrometer-registry-prometheus (Metrics export)
- camel-spring-boot-starter (Message routing)
- lombok (Reduce boilerplate)
- junit-jupiter (JUnit 5)
- mockito-core (Mocking framework)
```

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│     REST Client / Prometheus Scraper    │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│      Spring Boot 3 Application          │
│  - Spring MVC (REST Controller)         │
│  - Spring Actuator (Monitoring)         │
└──┬───────────────────────────┬──────────┘
   │                           │
   ▼                           ▼
┌──────────────────┐  ┌──────────────────┐
│ Apache Camel     │  │ Micrometer       │
│ (Data Routes)    │  │ Prometheus       │
└──────────────────┘  │ (Metrics Export) │
                      └──────────────────┘
```

## 📚 Documentation

- **BUILD_GUIDE.md**: Step-by-step compilation e deployment
- **README.md**: Questo file
- **Inline code comments**: Documentazione nel codice

## ✅ Checklist di Deployment

- [ ] Java 17 JDK installato su Rocky Linux
- [ ] Maven 3.8.1+ installato
- [ ] Progetto clonato/copiato
- [ ] `mvn clean package` eseguito con successo
- [ ] JAR generato in `target/`
- [ ] Applicazione avviata su porta 8080
- [ ] Health check raggiungibile
- [ ] Prometheus metrics disponibili
- [ ] Almeno 1 utente creato via REST API
- [ ] Test eseguiti con successo

## 🎓 Learnings Esposti

Questo progetto dimostra:

1. **Advanced Java**: Java 17 features, Spring Boot 3 patterns
2. **Enterprise messaging**: Apache Camel routing
3. **Cloud-native**: 12-factor app principles
4. **Observability**: Prometheus, metrics-driven development
5. **Testing**: Unit + integration tests
6. **DevOps**: Linux deployment, systemd, monitoring stack
7. **Security**: Password hashing, data masking practices
8. **API Design**: RESTful principles, error handling

---

**Pronto per il deployment su Rocky Linux!** 🚀

**Contatto profesionale**: Gaetano Cerciello - IT Support Specialist & Java Developer
