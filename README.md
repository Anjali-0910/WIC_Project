<div align="center">

  <!-- SVG Bounding Banner Header -->
  <svg width="100%" height="160" viewBox="0 0 1200 240" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="1200" height="240" rx="16" fill="#0d1117"/>
    <path d="M0 120 C 300 200, 600 40, 1200 120 L 1200 240 L 0 240 Z" fill="#161b22" opacity="0.6"/>
    <circle cx="200" cy="80" r="4" fill="#58a6ff"/>
    <circle cx="400" cy="160" r="6" fill="#bc8cff"/>
    <circle cx="800" cy="70" r="5" fill="#3fb950"/>
    <line x1="200" y1="80" x2="400" y2="160" stroke="#58a6ff" stroke-opacity="0.3" stroke-width="2"/>
    <line x1="400" y1="160" x2="800" y2="70" stroke="#bc8cff" stroke-opacity="0.3" stroke-width="2"/>
    <text x="60" y="110" fill="#f0f6fc" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif" font-size="42" font-weight="800" letter-spacing="1">CLOUD PERFORMANCE PLATFORM</text>
    <text x="60" y="155" fill="#8b949e" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif" font-size="18" font-weight="400">Enterprise Load Testing, Real-Time Analytics & Metric Dashboards</text>
  </svg>

  <br/><br/>

  <!-- Status & Tech Badges -->
  <p align="center">
    <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github-actions" alt="Build Status"/>
    <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java"/>
    <img src="https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=springboot" alt="Spring Boot"/>
    <img src="https://img.shields.io/badge/k6-Load_Testing-7D64FF?style=for-the-badge&logo=k6" alt="k6"/>
    <img src="https://img.shields.io/badge/Docker-Supported-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
  </p>

  <p align="center">
    <b>A high-throughput Spring Boot backend paired with k6 load-testing automation and a real-time analytics dashboard.</b>
  </p>

</div>

---

## 📌 Overview

**Cloud Performance Platform** is a full-stack performance testing and monitoring ecosystem designed to simulate high-concurrency traffic, evaluate API latency, and store historical performance telemetry[cite: 5].

It combines automated **k6 script execution** with a robust **Java Spring Boot core** to deliver multi-format file processing and metric persistence[cite: 5].

---

## 🚀 Key Features

- ⚡ **Automated Load Testing:** Embedded `k6` test script runner supporting configurable virtual users (VUs) and ramp-up stages[cite: 5].
- 📊 **Real-Time Dashboards:** Interactive visualization of request rates, latency distribution, p95/p99 response times, and failure rates.
- 📁 **Multi-Format Processing:** Ingestion and parsing support for JSON, CSV, and multipart file uploads[cite: 5].
- 💾 **Persistent Metric History:** SQLite database integration powered by JPA/Hibernate for tracking test trends over time[cite: 5].
- 🐳 **Containerized Setup:** Ready-to-use Docker and Docker Compose environment for single-command deployment[cite: 5].

---

## 🛠️ Tech Stack

| Domain | Technologies |
| :--- | :--- |
| **Backend Core** | Java 17, Spring Boot 3, Spring Data JPA |
| **Database** | SQLite, Hibernate ORM |
| **Load Testing** | Grafana k6, JavaScript (ES6) |
| **Frontend UI** | HTML5, CSS3, JavaScript (Dashboard View) |
| **DevOps & Infra** | Docker, Docker Compose, Git |

---

## 📂 Project Architecture

```text
.
├── core/                        # Spring Boot API & service layer
├── performance_platform/        # UI Dashboard files
├── performance-reports/         # Output logs & generated test reports
├── Dockerfile                   # Application container config
├── docker-compose.yml           # Multi-container orchestration
├── db.sqlite3                   # Local persistent storage
├── manage.py                    # Platform management runner
├── performance-test.js          # k6 load testing configuration
└── requirements.txt             # Python dependencies