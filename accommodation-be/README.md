# Tugas Individu Praktikum 9 - Deployment
**NPM:** 2306214510
**Nama:** [Nama Kamu]
**Kelas:** [Kelas Kamu]

---

## 📸 Screenshot Deployment

### Sidating BE1
[Screenshot request/response Bruno/Postman ke domain sidating BE1]

### Sidating BE2
[Screenshot request/response Bruno/Postman ke domain sidating BE2]

### Sidating FE
[Screenshot halaman FE yang sudah dapat diakses dengan URL deployment]

### TI Backend
[Screenshot request/response Bruno/Postman ke http://2306214510-be.hafizmuh.site - pastikan URL terlihat]

### TI Frontend (jika ada)
[Screenshot halaman FE yang sudah dapat diakses dengan URL deployment]

---

## 📊 Pipeline CI/CD Tugas Individu

```
┌─────────────────┐
│   DEVELOPMENT   │
│  (Push ke Git)  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   BUILD STAGE   │──────────────────────────────────────┐
│                 │                                       │
│ • Pull code     │  Image: eclipse-temurin:21-jdk       │
│ • ./gradlew     │  Output: app.jar + configs           │
│   clean build   │  Artifacts expire: 1 hour            │
│ • Copy jar      │                                       │
└────────┬────────┘                                       │
         │                                                │
         ▼                                                │
┌─────────────────┐                                       │
│ DOCKER-PUSH     │──────────────────────────────────────┤
│     STAGE       │                                       │
│                 │  Image: docker:24-dind                │
│ • Build Docker  │  Login to Docker Hub                 │
│   image         │  Tag: $CI_COMMIT_SHORT_SHA            │
│ • Push to       │  Push: accommodation:${SHA}           │
│   Docker Hub    │                                       │
└────────┬────────┘                                       │
         │                                                │
         ▼                                                │
┌─────────────────┐                                       │
│  DEPLOY STAGE   │──────────────────────────────────────┘
│                 │
│ • Generate      │  Image: alpine:latest
│   ConfigMap     │  SSH to EC2 via key
│ • Generate      │  Apply K8s manifests:
│   Secret        │    - config.yaml
│ • Update        │    - secret.yaml
│   deployment    │    - deployment.yaml
│ • SCP to EC2    │    - service.yaml
│ • kubectl apply │    - ingress.yaml
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  K3S CLUSTER    │
│   (EC2 Server)  │
│                 │
│ • Pull image    │
│ • Deploy pods   │
│ • Expose via    │
│   Ingress       │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   APPLICATION   │
│     RUNNING     │
│                 │
│ 2306214510-be.  │
│ hafizmuh.site   │
└─────────────────┘
```

### Deskripsi Singkat:
Pipeline ini terdiri dari 3 stage utama:
1. **Build**: Kompilasi aplikasi Spring Boot menggunakan Gradle, menghasilkan JAR file
2. **Docker-push**: Membuat Docker image dan push ke Docker Hub dengan tag berdasarkan commit SHA
3. **Deploy**: Generate Kubernetes manifests (ConfigMap & Secret), transfer ke EC2, dan apply menggunakan kubectl

---

## 🚀 Pipeline CI/CD Improvement

```
┌─────────────────┐
│   DEVELOPMENT   │
│  (Push ke Git)  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  LINT & FORMAT  │  ← NEW: Code quality check
│                 │     (Checkstyle, SpotBugs)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   BUILD STAGE   │
└────────┬────────┘
         │
         ├────────────────┐
         ▼                ▼
┌─────────────────┐  ┌─────────────────┐
│   UNIT TEST     │  │ SECURITY SCAN   │  ← NEW: Parallel testing
│   (JUnit)       │  │ (OWASP, Trivy)  │      & security check
└────────┬────────┘  └────────┬────────┘
         │                    │
         └──────────┬─────────┘
                    ▼
         ┌─────────────────┐
         │ CODE COVERAGE   │  ← NEW: JaCoCo report
         │ (Min 80%)       │      with threshold
         └────────┬────────┘
                  │
                  ▼
         ┌─────────────────┐
         │ DOCKER BUILD    │
         │ & SCAN          │  ← IMPROVED: Scan image
         └────────┬────────┘      for vulnerabilities
                  │
                  ▼
         ┌─────────────────┐
         │ STAGING DEPLOY  │  ← NEW: Deploy to staging
         │ (Auto)          │      environment first
         └────────┬────────┘
                  │
                  ▼
         ┌─────────────────┐
         │ INTEGRATION     │  ← NEW: E2E testing
         │ TEST            │      on staging
         └────────┬────────┘
                  │
                  ▼
         ┌─────────────────┐
         │ PRODUCTION      │  ← IMPROVED: Manual approval
         │ DEPLOY          │      + Blue-Green deployment
         │ (Manual trigger)│      + Rollback capability
         └────────┬────────┘
                  │
                  ▼
         ┌─────────────────┐
         │ MONITORING &    │  ← NEW: Health check
         │ NOTIFICATION    │      & Slack notification
         └─────────────────┘
```

### Improvement yang Ditambahkan:

1. **Lint & Format Stage**: Menambahkan checkstyle dan code formatting untuk menjaga kualitas kode
2. **Parallel Testing**: Unit test dan security scan berjalan paralel untuk menghemat waktu
3. **Security Scan**: Menggunakan OWASP Dependency Check dan Trivy untuk scan vulnerabilities
4. **Code Coverage**: Enforce minimum 80% coverage dengan JaCoCo
5. **Staging Environment**: Deploy ke staging terlebih dahulu sebelum production
6. **Integration Test**: E2E testing otomatis di staging environment
7. **Manual Approval**: Production deployment memerlukan approval manual
8. **Blue-Green Deployment**: Zero-downtime deployment strategy
9. **Monitoring & Notification**: Health check otomatis dan notifikasi ke Slack/Discord
10. **Rollback Capability**: Kemampuan rollback otomatis jika deployment gagal

### Manfaat Improvement:
- **Quality Assurance**: Code quality terjaga dengan linting dan testing yang komprehensif
- **Security**: Mendeteksi vulnerabilities lebih awal sebelum production
- **Reliability**: Staging environment mengurangi risiko error di production
- **Faster Recovery**: Rollback otomatis meminimalkan downtime
- **Better Visibility**: Monitoring dan notification meningkatkan awareness tim

---

## 🔧 Pertanyaan README

### 1. Mengapa EC2 Instance harus dikaitkan dengan Elastic IP?

**Jawaban:**

Elastic IP diperlukan karena AWS Academy Learner Lab memiliki karakteristik khusus dimana:
- Lab akan **mati otomatis setelah 4 jam** tidak ada aktivitas
- Setiap kali lab di-restart, **IP Public instance akan berubah**
- Pipeline deployment menggunakan IP yang di-hardcode di GitLab CI/CD variables

**Tanpa Elastic IP:**
- Setiap lab restart, kamu harus:
  1. Update variabel `EC2_HOST` di GitLab
  2. Update DNS record domain di spreadsheet
  3. Tunggu DNS propagation (bisa 5-15 menit)
  4. Re-run pipeline deployment

**Dengan Elastic IP:**
- IP tetap sama meskipun lab restart
- Pipeline langsung bisa jalan tanpa perlu update konfigurasi
- Domain selalu point ke IP yang sama
- Proses development lebih smooth

**Yang terjadi jika tidak mengaitkan Elastic IP:**
- Deployment pipeline akan **GAGAL** karena SSH tidak bisa connect ke IP lama
- Application tidak bisa diakses via domain karena DNS masih point ke IP lama
- Harus manual update konfigurasi setiap kali lab restart → **sangat tidak efisien**

---

### 2. Apa perbedaan utama Docker dan Kubernetes pada praktikum ini?

**Jawaban:**

| Aspek | Docker | Kubernetes (K3s) |
|-------|--------|------------------|
| **Fungsi** | **Containerization** - mengemas aplikasi beserta dependencies ke dalam image | **Orchestration** - mengelola, deploy, dan scale containers |
| **Scope** | Single container/application | Multiple containers/services |
| **Usage di Praktikum** | • Build image aplikasi<br>• Run database (docker-compose)<br>• Push image ke Docker Hub | • Deploy aplikasi dari image<br>• Manage pods & services<br>• Load balancing & routing (Ingress) |
| **Komponen** | • Dockerfile<br>• Docker images<br>• Docker containers | • Pods<br>• Deployments<br>• Services<br>• Ingress<br>• ConfigMaps<br>• Secrets |
| **Scaling** | Manual (jalankan lebih banyak container) | Otomatis (replicas di deployment.yaml) |
| **Networking** | Port mapping manual (-p 8080:8080) | Service ClusterIP + Ingress Controller |
| **Configuration** | Environment variables di docker run | ConfigMap & Secret (terpisah dari application) |
| **Health Management** | Restart policy (--restart=always) | Self-healing, liveness & readiness probes |
| **Contoh** | `docker run -p 8080:8080 myapp:latest` | `kubectl apply -f deployment.yaml` |

**Analogi:**
- **Docker** = Mobil (alat untuk membawa barang/aplikasi)
- **Kubernetes** = Sistem manajemen armada (mengelola banyak mobil, rute, driver, dll)

**Di praktikum ini:**
- Docker dipakai untuk **packaging** (buat image BE/FE)
- Kubernetes dipakai untuk **deployment & management** di server

---

### 3. Proses mana yang paling penting dalam pipeline dan mengapa?

**Jawaban:**

**Deploy Stage** adalah yang paling penting karena:

1. **Critical Point of Failure**
   - Jika build gagal → gampang debug di lokal
   - Jika docker-push gagal → tinggal re-run
   - Jika **deploy gagal** → aplikasi down, user tidak bisa akses

2. **Melibatkan Banyak Komponen**
   - SSH ke server production
   - Generate ConfigMap & Secret
   - Update Kubernetes manifests
   - Apply ke cluster yang sedang running
   - Rollout pods baru tanpa downtime

3. **Security Sensitive**
   - Menangani credentials (EC2_SSH_KEY)
   - Inject secrets ke cluster (DATABASE_PASSWORD, JWT_SECRET)
   - Akses ke production server

4. **Impact ke User**
   - Deploy yang salah bisa bikin aplikasi down
   - Bisa expose data sensitif jika secret salah handle
   - Menentukan apakah fitur baru sampai ke user atau tidak

**Namun, setiap stage tetap penting:**
- **Build**: Ensure code bisa di-compile
- **Docker-push**: Ensure image available untuk deploy
- **Deploy**: Ensure aplikasi running di production

**Best practice:** Tambahkan **smoke test** setelah deploy untuk memastikan aplikasi benar-benar jalan.

---

### 4. Penjelasan kegunaan 5 file konfigurasi Kubernetes

**Jawaban:**

#### 1. **deployment.yaml**
**Kegunaan:** Mendefinisikan **bagaimana aplikasi di-deploy dan dijalankan** di dalam cluster

**Isi:**
- **Replicas**: Berapa banyak instance aplikasi yang running (untuk load balancing)
- **Container spec**: Image Docker yang dipakai, port yang di-expose
- **Environment variables**: Database URL, credentials, JWT secret
- **Resources**: Limit CPU dan memory untuk setiap pod
- **Labels & selectors**: Untuk identifikasi pods

**Analogi:** Blueprint rumah yang menentukan berapa kamar, ukuran, material, dll.

```yaml
spec:
  replicas: 1  # Jalankan 1 instance
  containers:
    - image: accommodation:v1
      env:
        - name: DATABASE_URL
          valueFrom: configMapKeyRef
```

---

#### 2. **service.yaml**
**Kegunaan:** Membuat **internal networking** agar pods bisa diakses secara konsisten

**Isi:**
- **Type**: ClusterIP (internal), NodePort (external), LoadBalancer
- **Selector**: Pods mana yang di-expose oleh service ini
- **Ports**: Port mapping (port service → targetPort container)

**Kenapa perlu?**
- Pods bisa mati dan diganti dengan IP baru
- Service memberikan **stable IP address** untuk akses pods
- Load balancing otomatis jika ada multiple pods

**Analogi:** Resepsionis hotel yang tahu kamar mana yang tersedia (meskipun tamu keluar-masuk)

```yaml
spec:
  type: ClusterIP
  selector:
    app: accommodation-ti  # Route ke pods dengan label ini
  ports:
    - port: 80  # Port service
      targetPort: 8080  # Port container
```

---

#### 3. **ingress.yaml**
**Kegunaan:** **Routing HTTP/HTTPS dari internet** ke service di dalam cluster

**Isi:**
- **Host**: Domain yang dipakai (2306214510-be.hafizmuh.site)
- **Paths**: URL path routing (/, /api, dll)
- **Backend service**: Service mana yang handle request
- **TLS**: Certificate untuk HTTPS (optional)

**Kenapa perlu?**
- Service ClusterIP hanya bisa diakses dari dalam cluster
- Ingress expose service ke internet
- Routing berdasarkan domain/path

**Analogi:** Security gate yang periksa KTP sebelum masuk komplek

```yaml
spec:
  rules:
    - host: 2306214510-be.hafizmuh.site
      http:
        paths:
          - path: /
            backend:
              service:
                name: accommodation-ti-service
```

---

#### 4. **config.yaml (ConfigMap)**
**Kegunaan:** Menyimpan **konfigurasi non-sensitif** yang bisa berubah antar environment

**Isi:**
- Database URL
- Database username
- API endpoints
- Feature flags

**Kenapa terpisah dari deployment.yaml?**
- Bisa diubah tanpa rebuild image
- Bisa di-reuse oleh multiple deployments
- Centralized configuration management

**Analogi:** File .env untuk production

```yaml
data:
  DATABASE_URL_PROD: "jdbc:postgresql://ip:35002/db"
  DATABASE_USERNAME: "travelapap"
```

---

#### 5. **secret.yaml**
**Kegunaan:** Menyimpan **data sensitif yang ter-enkripsi** (passwords, keys, tokens)

**Isi:**
- Database password
- JWT secret key
- API keys
- CORS allowed origins

**Bedanya dengan ConfigMap:**
- Data di-encode base64 (atau encrypted di production)
- Lebih strict access control
- Tidak ter-log di kubectl describe

**Kenapa perlu?**
- Security: Credentials tidak hardcode di code/deployment
- Flexibility: Bisa diganti tanpa rebuild image
- Audit: Kubernetes track siapa yang akses secrets

**Analogi:** Brankas untuk simpan password/kunci

```yaml
stringData:  # Auto-encoded ke base64
  DATABASE_PASSWORD: "2306214510"
  JWT_SECRET_KEY: "supersecretkey12345"
```

---

### 5. Dimana sistem start on restart diterapkan?

**Jawaban:**

#### **A. Docker Database (docker-compose.yml)**
```yaml
services:
  accommodation-app-db:
    restart: always  # ← Restart policy
```

**Cara kerja:**
- Jika container crash → Docker auto-restart
- Jika server reboot → Docker daemon start container otomatis
- Diterapkan saat `docker compose up -d`

**Verifikasi:**
```bash
docker ps  # Cek kolom STATUS
# Up 2 days (healthy) atau "Restarting"
```

---

#### **B. Docker Daemon (systemd)**
```bash
sudo systemctl enable docker  # ← Enable auto-start
sudo systemctl start docker
```

**Cara kerja:**
- Saat server boot → systemd jalankan Docker daemon
- Daemon aktif → semua container dengan `restart: always` di-start

---

#### **C. K3s Service (systemd)**
```bash
curl -sfL https://get.k3s.io | sh -
# Otomatis membuat systemd service
```

File: `/etc/systemd/system/k3s.service`
```ini
[Install]
WantedBy=multi-user.target  # ← Auto-start on boot
```

**Cara kerja:**
- Saat server boot → systemd start K3s
- K3s aktif → semua pods di-restart

**Verifikasi:**
```bash
sudo systemctl status k3s
# Active: active (running)
```

---

#### **D. Kubernetes Deployment**
```yaml
# deployment.yaml
spec:
  replicas: 1  # ← Desired state
```

**Cara kerja:**
- K3s **terus memantau** jumlah pods yang running
- Jika pods crash/hilang → K3s auto-create pods baru
- Jika server reboot → K3s ensure pods running sesuai replicas

**Verifikasi:**
```bash
kubectl get pods
# Jika ada pod yang crash, lihat RESTARTS column
```

---

#### **E. Docker Container (manual set)**
```bash
docker update --restart=always <container-id>
```

**Ini yang dilakukan di praktikum untuk database:**
```bash
docker update --restart=always ubuntu-sidating-app-db-1
```

---

### **Summary Flow:**
```
Server Boot
    ↓
systemd starts Docker daemon
    ↓
Docker starts containers with restart: always
    ↓
systemd starts K3s
    ↓
K3s reads deployment.yaml & ensures pods running
    ↓
Application fully up
```

**Tanpa start on restart:**
- Setiap kali AWS Academy lab restart (setelah 4 jam idle)
- Kamu harus manual `docker compose up` dan `kubectl apply`
- Application down sampai kamu manual start → **bad user experience**

---

### 6. Keuntungan Kubernetes dibanding langsung run Docker image

**Jawaban:**

| Fitur | Docker Run | Kubernetes |
|-------|-----------|------------|
| **Deployment** | Manual: `docker run -d -p 8080:8080 myapp` | Deklaratif: `kubectl apply -f deployment.yaml` |
| **Scaling** | Manual jalankan multiple containers | Auto: ubah `replicas: 3` di YAML |
| **Load Balancing** | Perlu setup nginx/haproxy manual | Built-in via Service |
| **Auto-Healing** | Container crash → perlu manual restart | Self-healing: auto-restart crashed pods |
| **Rolling Update** | Stop old container → Start new (downtime!) | Zero-downtime rolling update |
| **Rollback** | Manual: simpan old image, stop, run | `kubectl rollout undo deployment` |
| **Configuration** | Hardcode env vars di docker run | ConfigMap & Secret (centralized) |
| **Service Discovery** | Manual track container IPs | DNS-based: `service-name.namespace` |
| **Health Checks** | Manual monitoring | Liveness & Readiness probes |
| **Resource Limits** | `--memory`, `--cpus` per container | Per deployment, auto-enforce |

---

#### **Contoh Skenario:**

**Scenario 1: Application Crash**
- **Docker**: Container mati → app down sampai manual restart
- **K8s**: Pod crash → auto-restart dalam seconds

**Scenario 2: Update Aplikasi**
- **Docker**:
  1. `docker stop myapp`
  2. `docker rm myapp`
  3. `docker pull myapp:v2`
  4. `docker run ...` ← **downtime 30-60 detik**
- **K8s**:
  1. `kubectl set image deployment/myapp myapp=myapp:v2`
  2. Auto rolling update: start v2 → check health → stop v1 ← **zero downtime**

**Scenario 3: Scale to 3 Instances**
- **Docker**:
  ```bash
  docker run -d -p 8081:8080 myapp  # Manual 3 kali
  docker run -d -p 8082:8080 myapp
  docker run -d -p 8083:8080 myapp
  # Perlu setup load balancer manual
  ```
- **K8s**:
  ```yaml
  spec:
    replicas: 3  # Ubah dari 1 ke 3
  ```
  `kubectl apply -f deployment.yaml` → auto-create 3 pods + load balance

**Scenario 4: Server Reboot**
- **Docker**: Perlu re-run semua `docker run` commands (atau docker-compose up)
- **K8s**: Auto-restart semua deployments sesuai desired state

---

### **Kesimpulan:**
Kubernetes memberikan **automation**, **reliability**, dan **scalability** yang tidak bisa dicapai dengan Docker run manual. Cocok untuk **production environment** dimana uptime dan automation sangat penting.

---

### 7. Perbedaan ClusterIP, NodePort, dan LoadBalancer

**Jawaban:**

#### **1. ClusterIP (Default)**
**Akses:** Hanya dari **dalam cluster**

**Use case:** Internal service communication (BE ↔ database, BE1 ↔ BE2)

**Cara kerja:**
- K8s assign IP internal (misal: 10.43.0.5)
- Service hanya bisa diakses dari pods lain di cluster
- **TIDAK bisa diakses dari internet**

**Example:**
```yaml
spec:
  type: ClusterIP
  ports:
    - port: 80
      targetPort: 8080
```

**Akses:**
```bash
# Dari dalam cluster:
curl http://accommodation-ti-service.default.svc.cluster.local

# Dari internet:
curl http://accommodation-ti-service  # ❌ TIDAK BISA
```

**Kenapa pakai di praktikum:**
- Application tidak perlu di-expose langsung ke internet
- Routing ke internet dihandle oleh **Ingress Controller** (Traefik)
- Lebih aman: layer tambahan antara internet dan application

---

#### **2. NodePort**
**Akses:** Dari **luar cluster** via `<NodeIP>:<NodePort>`

**Cara kerja:**
- K8s buka port di **semua node** (30000-32767)
- Request ke `NodeIP:30080` → route ke service

**Example:**
```yaml
spec:
  type: NodePort
  ports:
    - port: 80
      targetPort: 8080
      nodePort: 30080  # Optional, auto-assign jika tidak diset
```

**Akses:**
```bash
curl http://<EC2-IP>:30080
```

**Kekurangan:**
- Port number tidak standard (30000+)
- Perlu expose port di firewall untuk setiap service
- Tidak ada load balancing antar nodes

---

#### **3. LoadBalancer**
**Akses:** Dari **luar cluster** via **cloud load balancer** (AWS ELB, GCP LB)

**Cara kerja:**
- K8s request cloud provider create external load balancer
- Load balancer assign **public IP**
- Traffic → LB → NodePort → Service → Pods

**Example:**
```yaml
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8080
```

**Akses:**
```bash
curl http://<LoadBalancer-IP>
```

**Kekurangan:**
- **Tidak gratis**: setiap service = 1 load balancer ($$$)
- Over-kill untuk simple use case
- Tidak support di bare-metal/K3s (kecuali MetalLB)

---

### **Comparison Table:**

| Type | Access | IP/Port | Use Case | Cost |
|------|--------|---------|----------|------|
| **ClusterIP** | Internal only | 10.43.x.x:80 | BE ↔ DB, microservices | Free |
| **NodePort** | External | NodeIP:30000+ | Development, testing | Free |
| **LoadBalancer** | External | Public IP:80/443 | Production (cloud) | $$$ per service |
| **Ingress + ClusterIP** | External | Domain:80/443 | **Production (best practice)** | Free (1 LB untuk banyak service) |

---

### **Kenapa ClusterIP + Ingress di praktikum ini?**

**Arsitektur:**
```
Internet
    ↓
Ingress Controller (Traefik) ← 1 entry point
    ↓
Ingress rules (domain routing)
    ↓
Service ClusterIP (accommodation-ti-service)
    ↓
Pods (accommodation app)
```

**Keuntungan:**
1. **Cost-effective**: Hanya 1 load balancer (Traefik) untuk banyak services
2. **Flexible routing**: Bisa route berdasarkan domain/path
   ```
   be1.hafizmuh.site → sidating-be1-service
   be2.hafizmuh.site → sidating-be2-service
   fe.hafizmuh.site → sidating-fe-service
   ```
3. **TLS termination**: HTTPS handle di Ingress, backend tetap HTTP
4. **Security**: Services tidak exposed langsung, hanya via Ingress

**Alternatif (jika pakai NodePort):**
- Setiap service butuh port berbeda: BE1:30001, BE2:30002, FE:30003
- Domain harus include port: `http://be1.hafizmuh.site:30001` ← **jelek!**
- Perlu update security group untuk setiap port
- Tidak ada centralized TLS

**Alternatif (jika pakai LoadBalancer):**
- Di AWS Academy → tidak support (karena bukan AWS EKS)
- Jika support → butuh 5 load balancers untuk 5 services ($$$)

**Kesimpulan:** ClusterIP + Ingress adalah **best practice** untuk expose multiple HTTP services dengan cost-effective dan flexible routing.

---

### 8. Pelajaran terpenting CI/CD dan penerapannya

**Jawaban:**

#### **Pelajaran Terpenting:**

**1. Automation is Key**
- Manual deployment error-prone dan time-consuming
- CI/CD pipeline **eliminate human error**
- Push code → auto test, build, deploy → production
- Save waktu: deployment dari 30 menit manual → 5 menit otomatis

**2. Infrastructure as Code (IaC)**
- Semua konfigurasi di-version control (GitLab)
- Deployment reproducible: bisa recreate environment dengan `kubectl apply`
- Dokumentasi otomatis: YAML files adalah dokumentasi

**3. Separation of Concerns**
- **ConfigMap**: configuration (bisa diubah tanpa rebuild)
- **Secret**: credentials (secure & encrypted)
- **Deployment**: application logic
- **Service & Ingress**: networking
- Setiap komponen punya responsibility sendiri → easier to maintain

**4. Testing Before Production**
- Build stage catch compilation errors
- Docker stage catch containerization issues
- Deploy stage catch infrastructure issues
- Setiap stage adalah **quality gate**

**5. Rollback Capability**
- Git commit → immutable deployment
- Bisa rollback ke commit sebelumnya dengan `kubectl rollout undo`
- Docker image tagged dengan commit SHA → traceable

---

#### **Penerapan di Proyek Lain:**

**Proyek Tugas Kelompok (Team Project):**
```yaml
# .gitlab-ci.yml untuk project team
stages:
  - lint
  - test
  - build
  - deploy-staging
  - deploy-production

lint:
  script:
    - npm run lint  # Check code style

test:
  script:
    - npm test
    - coverage > 80% atau fail

deploy-staging:
  script:
    - Deploy ke staging.myproject.com
  only:
    - develop  # Auto deploy develop branch ke staging

deploy-production:
  script:
    - Deploy ke myproject.com
  only:
    - main
  when: manual  # Require manual approval untuk production
```

**Benefit:**
- Setiap commit di develop branch auto-deploy ke staging → QA bisa test cepat
- Production deployment require approval → prevent accidental deployment
- Team bisa fokus coding, deployment handled automatically

---

**Personal Projects (Portfolio):**
```yaml
# GitHub Actions untuk portfolio website
name: Deploy Portfolio
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build
        run: npm run build
      - name: Deploy to Vercel
        run: vercel --prod
```

**Benefit:**
- Push to GitHub → auto-deploy ke Vercel
- No need VPS/AWS management
- Portfolio selalu up-to-date

---

**Startup/Company Project:**
```yaml
stages:
  - test
  - security-scan  # OWASP dependency check
  - build
  - deploy-dev
  - integration-test
  - deploy-staging
  - load-test  # Performance testing
  - deploy-production  # Blue-green deployment

# Slack notification jika deployment gagal
after_script:
  - curl -X POST slack-webhook "Deployment failed!"
```

**Benefit:**
- Comprehensive testing sebelum production
- Security vulnerabilities detected early
- Team notified immediately jika ada issue
- Confident deploy karena sudah tested di staging

---

**Open Source Project:**
```yaml
# Automated testing untuk PR
on:
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - Checkout code
      - Run unit tests
      - Run integration tests
      - Comment test results on PR
```

**Benefit:**
- Contributor submit PR → auto-test
- Maintainer bisa review dengan confidence
- Quality terjaga meskipun banyak contributors

---

### **Konsep yang Bisa Diterapkan Dimana Saja:**

1. **Version Everything**
   - Code (Git)
   - Configuration (ConfigMap/Secret di Git)
   - Infrastructure (Terraform/K8s YAML)

2. **Automate Repetitive Tasks**
   - Deployment
   - Testing
   - Code review (linters)
   - Documentation generation

3. **Fail Fast**
   - Test early, fail early
   - Don't wait until production untuk found bugs

4. **Monitoring & Logging**
   - Know when something breaks
   - Debug issues faster dengan proper logs

5. **Rollback Plan**
   - Deployment bisa fail, harus ada plan B
   - Blue-green deployment, canary deployment

---

**Kesimpulan:**
CI/CD bukan cuma untuk big companies. **Any project** yang di-deploy ke server seharusnya pakai CI/CD. Manual deployment = technical debt. Automation = productivity + reliability + peace of mind.

---

## 📝 Catatan Tambahan

File-file yang sudah dibuat:
- ✅ `Dockerfile`
- ✅ `.gitlab-ci.yml`
- ✅ `k8s/deployment.yaml`
- ✅ `k8s/service.yaml`
- ✅ `k8s/ingress.yaml`
- ✅ `CorsConfig.java`
- ✅ `application.properties` (updated)

Next steps:
1. Setup database di EC2 (manual - lihat instruksi di atas)
2. Setup GitLab CI/CD variables
3. Push ke branch feat/praktikum-9
4. Monitor pipeline deployment
5. Test aplikasi & screenshot
6. Update README dengan screenshot

---

## 🔗 Links

- **Backend URL:** http://2306214510-be.hafizmuh.site
- **Frontend URL:** http://2306214510-fe.hafizmuh.site (jika ada)
- **Docker Hub:** https://hub.docker.com/r/<username>/accommodation-ti
- **GitLab Repository:** [Link ke repository GitLab]

---

**Last Updated:** [Tanggal]
