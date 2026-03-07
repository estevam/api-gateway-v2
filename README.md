# 🚀 Spring Cloud Gateway – REST, gRPC & Web Router

A unified API Gateway built with **Spring Cloud Gateway** that supports routing for:

* ✅ REST APIs (HTTP/1.1)
* ✅ gRPC Services (HTTP/2 + TLS)
* ✅ Web Applications (React / SPA)
* ✅ Custom Filters
* ✅ SSL / mTLS Support
* ✅ Load Balancing Ready

---

## 🏗 Architecture Overview

```
                 ┌─────────────────────┐
                 │     React App       │
                 │   (Frontend SPA)    │
                 └──────────┬──────────┘
                            │
                            ▼
                 ┌─────────────────────┐
                 │  Spring Cloud       │
                 │      Gateway        │
                 │  (HTTP1 + HTTP2)    │
                 └───────┬───────┬─────┘
                         │       │
                         ▼       ▼
                ┌──────────┐   ┌──────────┐
                │  REST    │   │  gRPC    │
                │ Service  │   │ Service  │
                └──────────┘   └──────────┘
```

---

# ⚙️ Technologies Used

* ☕ Java 17+
* 🌱 Spring Boot
* 🌐 Spring Cloud Gateway
* 🔄 Reactor Netty
* 📡 gRPC (HTTP/2)
* 🔐 TLS / SSL (PKCS12 / JKS / PEM)
* ⚡ Gradle

---

# 📦 Features

### 1️⃣ REST Routing (HTTP/1.1)

* Path-based routing
* Load balancing support
* Custom filters
* Header manipulation
* Authentication ready

Example:

```http
GET /api/users
→ Forward to http://localhost:8081
```

---

### 2️⃣ gRPC Routing (HTTP/2 + TLS)

* HTTP/2 support
* TLS secured channel
* Custom `ServerWebExchange` URI rewriting
* Supports internal PKCS12 bundle
* Compatible with `spring-grpc` or Netty server

Example:

```proto
service UserService {
  rpc GetUser(UserRequest) returns (UserResponse);
}
```

Gateway forwards:

```
https://gateway:9090/grpc/UserService
→ https://localhost:50051
```

---

### 3️⃣ Web / React App Forwarding

* SPA routing support
* Static content proxy
* Supports HTTPS certificates
* Works with custom SSL setup

Example:

```
https://gateway:9090/app
→ https://localhost:3000
```

---

# 🔐 SSL Configuration

Supports:

* PKCS12 (`.p12`)
* JKS
* PEM (`.crt` + `.key`)
* Self-signed certificates
* mTLS (optional)

Example `application.properties`:

```properties
server.port=9090
server.ssl.enabled=true
server.ssl.key-store=classpath:gateway.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.http2.enabled=true
```

---

# 📂 Project Structure

```
spring-cloud-gateway/
│
├── src/main/java/
│   ├── config/
│   ├── filter/
│   ├── route/
│   └── GatewayApplication.java
│
├── src/main/resources/
│   ├── application.properties
│   └── ssl/
│
└── build.gradle
```

---

# 🛠 Custom Filters Example

```java
@Component
public class GrpcRoutingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        URI newUri = URI.create("https://localhost:50051");
        exchange.getAttributes()
                .put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, newUri);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

---

# ▶️ Running the Project

### 1️⃣ Build

```bash
./gradlew clean build
```

### 2️⃣ Run

```bash
./gradlew bootRun
```

or

```bash
java -jar build/libs/gateway.jar
```

---

# 🧪 Testing

### Test REST

```bash
curl https://localhost:9090/api/users
```

### Test gRPC

```bash
grpcurl -insecure localhost:9090 list
```

### Test Web

Open:

```
https://localhost:9090/app
```

---

# 🐛 Common Issues

### ❌ `SSLHandshakeException: certificate_unknown`

Cause:

* Certificate not trusted
* Missing truststore
* CN mismatch

Fix:

* Import certificate into truststore
* Verify hostname
* Enable HTTP/2

---

### ❌ `Unexpected request [PRI * HTTP/2.0]`

Cause:

* HTTP/2 request sent to HTTP/1.1 endpoint

Fix:

```properties
server.http2.enabled=true
```

---

# 🔮 Future Improvements

* OAuth2 / JWT Authentication
* Service Discovery (Eureka / Consul)
* Rate Limiting
* Circuit Breaker
* Observability (Micrometer + Prometheus)

---

# 🤝 Contributing

Pull requests are welcome!

---

# 📄 License

MIT License

---

---

If you want, I can also generate:

* 🔹 A version tailored specifically for **gRPC + TLS production**
* 🔹 A version with **Docker + Docker Compose**
* 🔹 A version including **Eureka Service Discovery**
* 🔹 A more “enterprise-grade” README for GitHub portfolio**

Just tell me 👍
