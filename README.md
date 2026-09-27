# Cloud Performance Platform

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)
![k6](https://img.shields.io/badge/Load%20Testing-k6-7D64FF)
![License](https://img.shields.io/badge/License-MIT-blue)

A Spring Boot backend paired with a **k6 load-testing script** and a lightweight **dashboard UI**. Upload a k6 load-test report and instantly see extracted performance metrics plus an automatically calculated performance score.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Scoring Logic](#scoring-logic)
- [Notes](#notes)
- [Contributors](#contributors)

---

## Overview

This project answers a simple question: **is my API fast, reliable, and scalable enough?**

The pipeline works like this:

1. A Spring Boot REST API exposes a health check endpoint.
2. **k6** load-tests that endpoint and produces a summary report (`k6-summary.json`).
3. The report is uploaded to the backend via a REST endpoint.
4. The backend parses the report and calculates a performance score across response time, reliability, and throughput.
5. A static dashboard (served directly by Spring Boot — no separate frontend server needed) displays the metrics and score, live.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot (Web MVC) |
| JSON parsing | Jackson |
| Load testing | [k6](https://k6.io/) |
| Frontend | Plain HTML / CSS / JavaScript (no framework) |
| Build tool | Maven |

---

## Project Structure

```
.
├── pom.xml
├── performance-test.js                       # k6 load test script
├── src/main/java/cloud_performance_api/
│   ├── CloudPerformanceApiApplication.java
│   ├── controller/
│   │   ├── HealthController.java             # GET /api/health
│   │   ├── PerformanceController.java        # GET /api/performance/report, /score
│   │   └── PerformanceUploadController.java  # POST /api/performance/upload
│   ├── model/
│   │   ├── PerformanceReport.java
│   │   └── PerformanceScore.java
│   └── service/
│       └── PerformanceReportService.java     # parses k6 JSON, calculates score
└── src/main/resources/
    ├── application.properties
    └── static/index.html                     # dashboard UI
```

---

## Prerequisites

- **Java 17+**
- **Maven**
- **[k6](https://k6.io/docs/get-started/installation/)** installed and available on your `PATH`

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Anjali-0910/WIC_Project.git
cd WIC_Project
```

### 2. Run the backend

```bash
mvn clean spring-boot:run
```

Wait for `Started CloudPerformanceApiApplication` in the console. The app runs at **http://localhost:8080**.

### 3. Open the dashboard

Visit **http://localhost:8080** in your browser. The health indicator in the top-right should turn green (`API online`).

### 4. Run the k6 load test

In a **separate terminal** (leave the backend running):

```bash
k6 run --summary-export=k6-summary.json performance-test.js
```

This sends traffic to `/api/health` — 10 virtual users, for 30 seconds — and writes a summary report to `k6-summary.json`.

### 5. Upload the report

**Option A — via the dashboard:** drag the file into the upload box, or use the file picker.

**Option B — via terminal (recommended, most reliable):**

```bash
curl -F "file=@k6-summary.json" http://localhost:8080/api/performance/upload
```

Then refresh the dashboard, or click **Refresh report & score**, to see the results populate.

---

## API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/health` | Health check — returns `Application is healthy` |
| `POST` | `/api/performance/upload` | Upload a k6 summary JSON file |
| `GET` | `/api/performance/report` | Returns parsed metrics from the last uploaded report |
| `GET` | `/api/performance/score` | Returns a calculated performance score and rating |

---

## Scoring Logic

The overall score is an average of three sub-scores, each starting at 100 and penalized based on thresholds:

| Sub-score | Penalized when... |
|---|---|
| **Response time** | p95 response time exceeds 100ms / 200ms / 500ms |
| **Reliability** | Error rate exceeds 1% / 5% |
| **Throughput** | Requests/sec falls below 10 / 5 |

**Overall rating:**

| Score range | Rating |
|---|---|
| ≥ 90 | Excellent |
| ≥ 75 | Good |
| ≥ 50 | Needs Improvement |
| < 50 | Poor |

---

## Notes

- `k6-summary.json` and the Maven `target/` build folder are intentionally excluded from version control (see `.gitignore`) — regenerate them locally as needed.
- The dashboard is a single static HTML file with no build step. Edit `src/main/resources/static/index.html` directly and refresh the browser — no restart required.

---

## Contributors

- **Anjali** — [@Anjali-0910](https://github.com/Anjali-0910)