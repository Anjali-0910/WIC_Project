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
    <text x="60" y="155" fill="#8b949e" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif" font-size="18" font-weight="400">Enterprise Load Testing, Dual-Path Telemetry & Metric Dashboards</text>
  </svg>

  <br/><br/>

  <!-- Status & Tech Badges -->
  <p align="center">
    <img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github-actions" alt="Build Status"/>
    <img src="https://img.shields.io/badge/Python-3.11-3776AB?style=for-the-badge&logo=python" alt="Python"/>
    <img src="https://img.shields.io/badge/Django-4.2+-092E20?style=for-the-badge&logo=django" alt="Django"/>
    <img src="https://img.shields.io/badge/k6-Load_Testing-7D64FF?style=for-the-badge&logo=k6" alt="k6"/>
    <img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
  </p>

  <p align="center">
    <b>A robust Python/Django web application engineered with a Dual-Path Analysis Engine for real and simulated cloud telemetry tracking.</b>
  </p>

</div>

---

## 📌 Overview

**Cloud Performance Platform** is a full-stack performance testing and monitoring ecosystem designed to evaluate cloud application microservices under heavy web traffic. It features a custom **Dual-Path Engine** capable of parsing official k6 load-test JSON reports or executing cryptographic hash-based fallback simulations for universal file formats.

---

## 🚀 Key Features

- ⚡ **Dual-Path Analysis Engine:**
  - **Path A (Strict JSON Telemetry):** Parses official k6 benchmark reports for response latencies, throughput, and error rates.
  - **Path B (Universal Fallback Scan):** Computes cryptographic file sizes and SHA-256 hashes to generate stable, deterministic simulation metrics for non-JSON files.
- 📊 **Dynamic Dark-Mode Dashboard:** Real-time visual representation featuring live metric cards, score progress bars, and automated health rating badges (*Excellent, Good, Needs Improvement, Poor*).
- 💾 **Client-Side History Tracking:** Uses browser `localStorage` to maintain a persistent audit trail of past upload scans across sessions without bloating backend storage.
- 🐳 **Cloud-Ready Containerization:** Fully packaged with `Docker` and `Docker Compose` for instant, isolated multi-platform deployment.

---

## 🛠️ Tech Stack

| Domain | Technologies |
| :--- | :--- |
| **Backend Core** | Python 3.11, Django 4.2+, Gunicorn |
| **Database & Storage** | SQLite (ORM) / Local File Storage |
| **Load Testing** | Grafana k6, JavaScript (ES6) |
| **Frontend UI** | HTML5, Modern CSS (Custom Properties), Vanilla JS (Fetch API) |
| **DevOps & Infra** | Docker, Docker Compose, Git |

---

## 📂 Project Architecture

```text
.
├── core/                        # Global Django settings, WSGI, & URL routing
├── performance_platform/        # App views, business logic, and UI templates
├── performance-reports/         # Stored performance logs & test files
├── Dockerfile                   # Python container configuration
├── docker-compose.yml           # Multi-container service orchestration
├── db.sqlite3                   # SQLite database instance
├── manage.py                    # Django management CLI utility
├── performance-test.js          # k6 load testing configuration script
└── requirements.txt             # Python package dependencies