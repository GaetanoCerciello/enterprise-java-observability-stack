# Enterprise Java Observability Stack - Guida di Compilazione e Deployment

## 📋 Prerequisiti

### Sistema Operativo: Rocky Linux
```bash
# Verificare la versione di Rocky Linux
cat /etc/os-release

# Aggiornare i pacchetti di sistema
sudo dnf update -y
```

### Requisiti Software

1. **Java 17 JDK**
2. **Maven 3.8.1+**
3. **Git** (opzionale, per versionamento)

## 🚀 Step-by-Step Installation su Rocky Linux

### 1. Installare Java 17 JDK

```bash
# Se non è già installato
sudo dnf install java-17-openjdk java-17-openjdk-devel -y

# Verificare l'installazione
java -version
javac -version

# Output atteso:
# openjdk version "17.0.x" ...
```

### 2. Installare Apache Maven

```bash
# Installare Maven
sudo dnf install maven -y

# Verificare l'installazione
mvn -version

# Output atteso:
# Apache Maven 3.8.1 (or higher)
# Java version: 17.0.x ...
```

### 3. Clonare o copiare il progetto

```bash
# Se si usa Git
git clone <repository-url> /path/to/enterprise-java-observability-stack

# Oppure copiare i file manualmente
# cd /tmp/enterprise-java-observability-stack (location di default)
```

## 🔨 Compilazione del Progetto

### Step 1: Pulire il progetto (opzionale ma consigliato)

```bash
cd /path/to/enterprise-java-observability-stack
mvn clean
```

### Step 2: Compilare e costruire il JAR

```bash
# Build completo con test
mvn clean package

# Build senza eseguire i test (più veloce)
mvn clean package -DskipTests
```

### Step 3: Output atteso

Il processo genererà un file JAR nella directory `target/`:

```bash
target/enterprise-java-observability-stack-1.0.0.jar
```

## 📦 Deployment su Rocky Linux

### Opzione 1: Esecuzione diretta

```bash
# Navigare nella directory del progetto
cd /path/to/enterprise-java-observability-stack

# Eseguire il JAR
java -jar target/enterprise-java-observability-stack-1.0.0.jar

# Output atteso:
# =========================================
# Enterprise Java Observability Stack
# Running on Java 17 with Spring Boot 3
# Prometheus metrics available at: /actuator/prometheus
# Health check at: /actuator/health
# =========================================
```

### Opzione 2: Copiare il JAR su Rocky Linux

```bash
# Copiare il JAR a una locazione permanente
sudo mkdir -p /opt/applications
sudo cp target/enterprise-java-observability-stack-1.0.0.jar /opt/applications/

# Renderlo eseguibile
sudo chmod +x /opt/applications/enterprise-java-observability-stack-1.0.0.jar

# Eseguire l'applicazione
java -jar /opt/applications/enterprise-java-observability-stack-1.0.0.jar
```

### Opzione 3: Creare un servizio systemd

```bash
# Creare il file di servizio
sudo bash -c 'cat > /etc/systemd/system/observability-app.service << EOF
[Unit]
Description=Enterprise Java Observability Stack
After=network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/applications
ExecStart=/usr/bin/java -jar /opt/applications/enterprise-java-observability-stack-1.0.0.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
EOF'

# Ricaricare systemd
sudo systemctl daemon-reload

# Abilitare il servizio all'avvio
sudo systemctl enable observability-app.service

# Avviare il servizio
sudo systemctl start observability-app.service

# Verificare lo stato
sudo systemctl status observability-app.service

# Visualizzare i log
journalctl -u observability-app.service -f
```

## 🌐 Accesso all'Applicazione

Una volta in esecuzione, l'applicazione sarà disponibile a:

```
http://localhost:8080
```

### Endpoint disponibili:

| Endpoint | Metodo | Descrizione |
|----------|--------|-------------|
| `/api/users` | GET | Recupera tutti gli utenti |
| `/api/users/{id}` | GET | Recupera un utente specifico |
| `/api/users` | POST | Crea un nuovo utente |
| `/api/users/{id}` | PUT | Aggiorna un utente |
| `/api/users/{id}` | DELETE | Elimina un utente |
| `/api/users/health-check` | POST | Health check dell'applicazione |
| `/actuator/health` | GET | Health status Spring Boot |
| `/actuator/prometheus` | GET | Metriche Prometheus (uso con Grafana) |
| `/actuator/metrics` | GET | Elenco di tutte le metriche |

## 📊 Integrazione con Prometheus e Grafana

### 1. Configurare Prometheus

Aggiungere al file `prometheus.yml`:

```yaml
scrape_configs:
  - job_name: 'observability-app'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 15s
```

### 2. Lanciare Prometheus (su Rocky Linux)

```bash
# Scaricare Prometheus (se non già disponibile)
cd /opt
sudo wget https://github.com/prometheus/prometheus/releases/download/v2.45.0/prometheus-2.45.0.linux-amd64.tar.gz
sudo tar xzf prometheus-2.45.0.linux-amd64.tar.gz

# Avviare Prometheus
cd prometheus-2.45.0.linux-amd64
./prometheus --config.file=prometheus.yml

# Sarà disponibile a: http://localhost:9090
```

### 3. Lanciare Grafana

```bash
# Installare Grafana
sudo dnf install grafana -y

# Avviare il servizio
sudo systemctl start grafana-server
sudo systemctl enable grafana-server

# Accedere a: http://localhost:3000
# Default credentials: admin / admin
```

## 🧪 Esecuzione dei Test

```bash
# Eseguire tutti i test
mvn test

# Eseguire un test specifico
mvn test -Dtest=UserControllerTest

# Eseguire con report di copertura
mvn clean test jacoco:report
```

## 📝 Troubleshooting

### Problema: "Maven command not found"

```bash
# Verificare il PATH
echo $PATH

# Reinstallare Maven
sudo dnf remove maven
sudo dnf install maven -y
```

### Problema: "Java command not found"

```bash
# Verificare Java
which java

# Reinstallare Java 17
sudo dnf install java-17-openjdk -y
```

### Problema: Porta 8080 già in uso

```bash
# Cambiare il port in application.properties
# server.port=8081

# Oppure terminare il processo usando la porta
sudo lsof -i :8080
sudo kill -9 <PID>
```

### Problema: Impostazioni di memoria

```bash
# Aumentare la memoria heap (se necessario)
java -Xmx512m -Xms256m -jar target/enterprise-java-observability-stack-1.0.0.jar
```

## 📚 File Struttura

```
enterprise-java-observability-stack/
├── pom.xml                                 # Configurazione Maven
├── src/
│   ├── main/
│   │   ├── java/com/observability/
│   │   │   ├── app/
│   │   │   │   └── Application.java       # Entry point Spring Boot
│   │   │   ├── camel/
│   │   │   │   └── CamelRoutes.java       # Rotte Apache Camel
│   │   │   ├── controller/
│   │   │   │   └── UserController.java    # REST API controller
│   │   │   ├── model/
│   │   │   │   └── User.java              # Entità User
│   │   │   └── util/
│   │   │       └── SecurityUtils.java     # Utility SHA-256 hashing
│   │   └── resources/
│   │       └── application.properties     # Configurazione Spring Boot
│   └── test/
│       └── java/com/observability/
│           ├── UserControllerTest.java    # Test controller (JUnit 5 + Mockito)
│           ├── SecurityUtilsTest.java     # Test security utils
│           └── CamelRoutesTest.java       # Test Camel routes
└── BUILD_GUIDE.md                         # Questo file
```

## ✅ Validazione Finale

Dopo aver compilato e deployato, verificare:

```bash
# 1. Verificare che l'app sia in ascolto
curl -s http://localhost:8080/actuator/health | json_pp

# 2. Verificare le metriche Prometheus
curl -s http://localhost:8080/actuator/prometheus | head -20

# 3. Creare un utente di test
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "secure123",
    "role": "USER",
    "active": true
  }'

# 4. Verificare la lista degli utenti
curl http://localhost:8080/api/users | json_pp
```

## 🎯 Prossimi Passi

1. **Configurare Prometheus** per raccogliere metriche
2. **Configurare Grafana** per visualizzare i dati
3. **Creare dashboard Grafana** per monitorare:
   - Response times
   - Error rates
   - Throughput
   - JVM metrics
4. **Setup CI/CD** con GitHub Actions o GitLab CI
5. **Containerizzazione** con Docker per Rocky Linux

## 📞 Supporto

Per problemi o domande durante la compilazione e il deployment:

1. Verificare i log dell'applicazione
2. Consultare la documentazione di [Spring Boot](https://spring.io/projects/spring-boot)
3. Consultare la documentazione di [Apache Camel](https://camel.apache.org)
4. Consultare la documentazione di [Prometheus](https://prometheus.io/docs)

---

**Data creazione:** 23 Marzo 2026
**Versione:** 1.0.0
**Autore:** Gaetano Cerciello - IT Support Specialist & Java Developer
