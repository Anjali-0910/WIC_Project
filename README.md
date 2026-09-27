\# Cloud Performance Platform



A Spring Boot backend paired with a k6 load-testing script and a lightweight dashboard UI. The dashboard lets you upload a k6 load-test summary (JSON), then view the extracted performance metrics and an automatically calculated performance score.



\## What this project does



1\. A Spring Boot REST API exposes a health check endpoint.

2\. \[k6](https://k6.io/) load-tests that endpoint and produces a summary report (`k6-summary.json`).

3\. The report is uploaded to the backend via a REST endpoint.

4\. The backend parses the report and calculates a performance score (response time, reliability, throughput).

5\. A static HTML dashboard (served by Spring Boot itself) displays the metrics and score.



\## Tech stack



\- \*\*Backend:\*\* Java 17, Spring Boot (Web MVC), Jackson for JSON parsing

\- \*\*Load testing:\*\* \[k6](https://k6.io/)

\- \*\*Frontend:\*\* Plain HTML/CSS/JavaScript (no framework), served as a static file from Spring Boot

\- \*\*Build tool:\*\* Maven



\## Project structure



```

.

├── pom.xml

├── performance-test.js                  # k6 load test script

├── src/main/java/cloud\_performance\_api/

│   ├── CloudPerformanceApiApplication.java

│   ├── controller/

│   │   ├── HealthController.java        # GET /api/health

│   │   ├── PerformanceController.java    # GET /api/performance/report, /score

│   │   └── PerformanceUploadController.java  # POST /api/performance/upload

│   ├── model/

│   │   ├── PerformanceReport.java

│   │   └── PerformanceScore.java

│   └── service/

│       └── PerformanceReportService.java # parses k6 JSON, calculates score

└── src/main/resources/

&#x20;   ├── application.properties

&#x20;   └── static/index.html                 # dashboard UI

```



\## Prerequisites



\- Java 17+

\- Maven (or use the Maven wrapper if you add one)

\- \[k6](https://k6.io/docs/get-started/installation/) installed and available on your PATH



\## Getting started



\### 1. Clone the repo



```bash

git clone https://github.com/Anjali-0910/WIC\_Project.git

cd WIC\_Project

```



\### 2. Run the backend



```bash

mvn clean spring-boot:run

```



Wait for `Started CloudPerformanceApiApplication` in the console. The app runs on `http://localhost:8080` by default.



\### 3. Open the dashboard



Visit \*\*http://localhost:8080\*\* in your browser. You should see the "API online" indicator light up green.



\### 4. Run the k6 load test



In a separate terminal (leave the backend running):



```bash

k6 run --summary-export=k6-summary.json performance-test.js

```



This hits `/api/health` with 10 virtual users for 30 seconds and writes a summary report to `k6-summary.json`.



\### 5. Upload the report



\*\*Option A — via the dashboard:\*\* use the upload box on the page.



\*\*Option B — via terminal (recommended, most reliable):\*\*



```bash

curl -F "file=@k6-summary.json" http://localhost:8080/api/performance/upload

```



Then refresh the dashboard (or click \*\*Refresh report \& score\*\*) to see the results.



\## API endpoints



| Method | Endpoint | Description |

|---|---|---|

| GET | `/api/health` | Health check, returns `Application is healthy` |

| POST | `/api/performance/upload` | Upload a k6 summary JSON file |

| GET | `/api/performance/report` | Returns parsed metrics from the last uploaded report |

| GET | `/api/performance/score` | Returns a calculated performance score and rating |



\## Scoring logic



The score is an average of three sub-scores (each starting at 100 and penalized based on thresholds):



\- \*\*Response time score\*\* — penalized if p95 response time exceeds 100ms / 200ms / 500ms

\- \*\*Reliability score\*\* — penalized if error rate exceeds 1% / 5%

\- \*\*Throughput score\*\* — penalized if requests/sec falls below 10 / 5



Overall rating: `Excellent` (≥90), `Good` (≥75), `Needs Improvement` (≥50), `Poor` (<50).



\## Notes



\- `k6-summary.json` and the Maven `target/` build folder are intentionally excluded from version control (see `.gitignore`) — regenerate them locally as needed.

\- The dashboard is a single static HTML file with no build step; edit `src/main/resources/static/index.html` directly and refresh the browser to see changes (restart not required).



\## Contributors



\- Anjali (Anjali-0910)

