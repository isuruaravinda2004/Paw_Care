# PawCare — Pet Grooming Center Management System

A complete Java Swing coursework project for a Diploma in Software Engineering. Built as an ordinary Maven desktop application that opens in NetBeans, with paired `.java` and `.form` GUI Builder files.

**First read [START_HERE.txt](START_HERE.txt).** Then follow the [24-phase learning guide](docs/LEARNING_GUIDE.md).

## Features

- Nine connected windows: Login, Dashboard, Customers, Pets, Services, Appointments, Payments, Reports and Breed Lookup.
- Database-backed login, salted PBKDF2 password hashes and change-password dialog.
- Full create/read/update/delete operations for customers, pets, services, appointments and payments, plus search and row-to-form selection.
- Appointments filter pets by owner, reject past or overlapping bookings, support cancellation and preserve the price/duration agreed at booking.
- One full payment per appointment, duplicate-payment protection, automatic customer/service/amount details and printable receipts.
- Live dashboard with six database statistics, refreshed on reopening and every five seconds while visible.
- Jasper appointment report, payment report and receipt; joined SQL, date filters, print preview and PDF export.
- Optional Dog CEO breed list and photo lookup, with timeouts and JSON error handling.
- Colour-coded actions and app-drawn navigation icons.
- Controller validation, checked custom exceptions and prepared SQL statements.
- Runnable JAR with dependencies, tests, sample SQL, launch scripts and feature-branch history.

## Technologies and prerequisites

| Item | Choice |
|---|---|
| Language/runtime | Java 17; use a full desktop JDK 17 or newer |
| IDE | Apache NetBeans with Java, Maven and Swing GUI Builder support |
| Desktop UI | Swing + FlatLaf 3.5.4 |
| Database | MySQL 8+ or MariaDB 10.4+ (InnoDB) |
| JDBC | MySQL Connector/J 8.4.0 |
| Reports | JasperReports and bundled fonts 6.21.3 |
| HTTP / JSON | Java HttpClient / Gson 2.11.0 |
| Build | Maven; NetBeans can supply its own Maven |
| Tests | JUnit 5 + isolated H2 database; optional MariaDB/MySQL test mode |

These versions are pinned in `pom.xml`; they are not a claim to be the newest releases. Keep the Jasper 6 templates and Jasper 6 library together.

## Install and run

1. Extract the ZIP and install a desktop JDK 17+.
2. Start MySQL/MariaDB. Import `database.sql` in phpMyAdmin or Workbench. It creates `pet_grooming_db`, six business tables, a small booking-lock table and demo data. Use a fresh database; importing again is not a migration or reset operation.
3. Copy `config.example.properties` to `config.properties` in the project folder. Edit the connection URL, username and password. The local XAMPP defaults are `root` and a blank password. Use your configured account where different.
4. Run `run.bat` on Windows or `bash run.sh` on macOS/Linux. The launcher uses a freshly built `target/PawCare.jar` when present; otherwise it uses the supplied `dist/PawCare.jar`.
5. Log in with **admin / PawCare@123**, then use **Change password** on the dashboard.

The JDBC URL selects Sri Lanka time for connection handling. Application dates and dashboard day boundaries use `Asia/Colombo`. Do not put live database passwords in Git; `config.properties` is ignored.

Configuration precedence: Java `-Dpawcare.db.url/user/password`, then `PAWCARE_DB_URL/USER/PASSWORD`, then local `config.properties`, then bundled defaults. A custom config file can be selected with `-Dpawcare.config=/path/to/config.properties`.

## NetBeans development

- **File → Open Project** and choose the folder containing `pom.xml`.
- Let Maven resolve the dependencies. MySQL Connector/J and JasperReports are already listed; no manual JAR copying is needed.
- **Source Packages → com.pawcare.view → CustomerFrame.java → Design** opens the GUI Builder form. All nine windows have `.form` files.
- Make visual changes in Design. Add business/event logic outside `GEN-BEGIN` / `GEN-END` guarded sections.
- **Clean and Build** creates `target/PawCare.jar`. **Run Project (F6)** starts `com.pawcare.Main`.
- CLI alternative: `mvn clean package`, then `java -jar target/PawCare.jar` from the project directory.
- The single JAR includes runtime dependencies. Java and the database server must still be installed.

[Component inventory](docs/GUI_COMPONENTS.md) • [Architecture and database](docs/ARCHITECTURE.md)

## Architecture

A view reads form input and calls its controller. The controller validates the model and calls a DAO. DAOs own JDBC queries and transactions; models hold data only. `DBConnection` is a Singleton **configuration/connection factory**, returning a separate short-lived connection to each operation. This avoids sharing a single JDBC connection between background workers.

`Tasks.run()` uses SwingWorker for database, API and report work. Tables and labels update on Swing's event thread. Report SQL lives in JRXML; ordinary data SQL lives in DAO classes.

## Reports

Open **Reports**, enter an inclusive date range and generate either report. JasperViewer provides printing/export. **Save last report as PDF** saves the last generated preview. An empty range still produces a titled, zero-record report.

- `appointments.jrxml`: appointments + customers + pets + services.
- `payments.jrxml`: payments + appointments + customers + services, including total revenue.
- `receipt.jrxml`: the selected payment with its customer/service details.

Templates live under `src/main/resources/reports`. `ReportService` compiles a bundled template, passes typed parameters and fills its JOIN query through JDBC. `$P{...}` query parameters are bound by JasperReports.

## API integration

The optional **Breed lookup** screen calls the documented Dog CEO endpoints:

- `GET https://dog.ceo/api/breeds/list/all`
- `GET https://dog.ceo/api/breed/{breed}/images/random`
- For a sub-breed, `{breed}` contains a path such as `hound/afghan`.

The JSON supplies a breed list or photo URL; Gson parses it and HttpClient downloads the image. This feature shows breed reference photos, not veterinary advice or your registered pet's actual photo. API failure does not affect database modules.

## Business assumptions

This version models **one grooming station**. Non-cancelled appointment time intervals cannot overlap; adjacent slots are allowed. Each appointment has one service and one full payment. New bookings must start in the future and finish before midnight. A paid booking cannot be cancelled, deleted or rescheduled until its payment is removed. Pet age is a positive whole number of years, matching the coursework requirement. Users may edit historical status/payment records subject to those rules. There are no tax, discounts, refunds, partial-payment, employee scheduling or real card-processing integrations.

Database foreign keys protect related records: remove payments before appointments, and appointments before their pets/services/customers. Cancellation keeps history and frees a slot.

## Verification

Run `mvn test` for isolated database, validation, concurrency and report tests. No installed MySQL is needed for the default tests. For optional real-server and desktop tests, see [TEST_RESULTS.md](docs/TEST_RESULTS.md). Tests never use the production connection URL.

## Screenshots

Actual application screenshots are in `docs/screenshots/`.

![Dashboard](docs/screenshots/02-dashboard.png)

![Appointments](docs/screenshots/06-appointments.png)

## Git and GitHub

The download includes `PawCare-history.bundle` beside the project folder. It preserves actual commits and nine feature branches created during generation. It is generated starter history, not a claim about work you personally performed. Continue making your own changes and commits.

See [GITHUB.md](docs/GITHUB.md) to restore the history and publish it to your own account. **No GitHub repository has been published and no repository URL exists yet.** Add your actual URL to [SUBMISSION.md](docs/SUBMISSION.md) after pushing.

## Documentation

- [24-phase beginner guide](docs/LEARNING_GUIDE.md)
- [GUI component names](docs/GUI_COMPONENTS.md)
- [Architecture, relationships and colour rationale](docs/ARCHITECTURE.md)
- [Testing and remaining environment checks](docs/TEST_RESULTS.md)
- [GitHub instructions](docs/GITHUB.md)
- [Submission checklist](docs/SUBMISSION.md)
- [Official references](docs/REFERENCES.md)
