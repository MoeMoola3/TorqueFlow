<p align="center">
  <img width="430" alt="torqueflow-backend" src="https://github.com/user-attachments/assets/91f40780-ed5d-4fd5-a2c7-da4b581712f8" />
</p>

<h1 align="center">🛠️ TorqueFlow Backend</h1>

<p align="center">
  <b>Real-Time Automotive Telemetry API | Spring Boot + PostgreSQL + WebSockets + Docker</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-Backend-green?logo=springboot" />
  <img src="https://img.shields.io/badge/PostgreSQL-Database-blue?logo=postgresql" />
  <img src="https://img.shields.io/badge/WebSockets-Real--Time-orange?logo=websocket" />
  <img src="https://img.shields.io/badge/Container-Docker-blue?logo=docker" />
  <img src="https://img.shields.io/badge/Host-Raspberry%20Pi-red?logo=raspberrypi" />
  <img src="https://img.shields.io/badge/Proxy-Cloudflare-orange?logo=cloudflare" />
</p>

---

## 🧭 Overview

The **TorqueFlow Backend** powers real-time automotive telemetry streaming and data storage.  
It provides WebSocket live events + REST API logging backed by **Spring Boot**, **PostgreSQL**, and **Docker** — running on a Raspberry Pi with **Cloudflare secure tunnel access**.

Designed to simulate OBD-II style engine metrics and serve them to the [TorqueFlow Frontend](https://github.com/MoeMoola3/TorqueFlow-Frontend).

---

## 🚀 Features

✅ **Real-time WebSocket engine telemetry**  
✅ **REST API for OBD data logs**  
✅ **PostgreSQL storage**  
✅ **Pagination — latest 50 entries default**  
✅ **Engine modes simulation** (cold start, idle, cruise, acceleration, deceleration, high load)  
✅ **Docker + Raspberry Pi deployment**  
✅ **Secure Cloudflare tunneling**  
✅ **Auto-restarts + persistent DB volume**

---

## 🧩 Architecture
````
┌──────────────┐     WebSocket      ┌─────────────┐
│   Frontend   │ <----------------> │ Spring Boot │
│  (React UI)  │     Live Telemetry │  Backend    │
└──────┬───────┘                    └──────┬──────┘
       │                                   │
       │    REST API (GET)                 │
       ▼                                   │
┌──────────────┐                           │
│  PostgreSQL  │  <── Persist + Query  ────┘
└──────────────┘

Raspberry Pi Host + Docker + Cloudflare Secure Tunnel

````

---

## 🗂 File Structure

```
com.moola.obd.analyzer
├── config
|     └── WebSocketConfig
├── controller
|      ├── ObdDataController
|      └── WebSocketObdDataController
├── handler
|      └── ObdWebSocketHandler
├── model
|      ├── EngineMode
|      ├── ObdData
|      ├── Vin
|      └── WebSocketMessage
├── repository
|      ├── ObdDataRepository
|      └── VinRepository
├── service
      └── ObdAnalyzerAppliation
```
---

## 🧾 Log Screenshot

<img width="1845" height="698" alt="Screenshot 2025-11-05 031835" src="https://github.com/user-attachments/assets/c03afbed-5a94-41e9-b514-4247c70803d5" />


---

## 🌐 API + WebSocket Endpoints

### 📡 WebSocket
| Endpoint    | Description                  |
|-------------|------------------------------|
| `/ws`       | Live real-time data stream   |
| `/api/obd`  | Latest 50 records            |


---
