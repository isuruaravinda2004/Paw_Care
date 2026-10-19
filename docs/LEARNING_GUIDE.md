# Build and understand PawCare — 24 phases

This guide follows your requested development order. The complete files are already supplied, so you can run the working project first and then study one phase at a time. Do not paste an entire project into one Java file. Every public class belongs in its matching file.

For a Windows first run, use `START_HERE.txt`. For all exact variable names, use `GUI_COMPONENTS.md` beside this guide. Full implementations live in `src/main/java/com/pawcare`, with matching GUI Builder metadata in `view/*.form`. Each module below identifies which code to read, what it does, how to test it and a suitable commit message for your own changes.

## Before starting: where things go

| Location | What belongs there |
|---|---|
| `pom.xml` | Java version, dependency versions, build plugins |
| `database.sql` | Tables, primary/foreign keys and sample records |
| `config.properties` | Your local MySQL settings; create from the example |
| `src/main/java/com/pawcare/Main.java` | `main()` startup |
| `src/main/java/com/pawcare/model/` | Data classes |
| `src/main/java/com/pawcare/view/` | Nine JFrame `.java` / `.form` pairs |
| `src/main/java/com/pawcare/controller/` | Action coordination and validation |
| `src/main/java/com/pawcare/dao/` | Database queries |
| `src/main/java/com/pawcare/service/` | Input checks and external API |
| `src/main/java/com/pawcare/util/` | Reusable helpers |
| `src/main/java/com/pawcare/exception/` | Custom exception classes |
| `src/main/java/com/pawcare/report/` | Report generation code |
| `src/main/resources/reports/` | Jasper JRXML templates |
| `src/test/java/com/pawcare/` | Automated tests |
| `target/PawCare.jar` | Output after Clean and Build |
| `dist/PawCare.jar` | Supplied prebuilt application |

`PROJECT_FILES.md` lists every source file. `ARCHITECTURE.md` shows the complete table relationships. Read a model first, its controller second, its DAO third, then the corresponding JFrame to see the entire flow.

## Phase 1 — Create/open a NetBeans project

**Open the supplied project:** File → Open Project → choose `PawCare` (the folder with `pom.xml`). It is a Maven project, so you do not need an Ant `nbproject` folder. Let dependencies finish downloading.

**To practise from scratch:** File → New Project → Java with Maven → Java Application. Use group `com.pawcare`, artifact `pawcare`, Java 17 and main class `com.pawcare.Main`. Create packages by right-clicking Source Packages → New → Java Package. Use the eight package names shown above. Copy the supplied `pom.xml` settings into the practice project.

Create a window by right-clicking `com.pawcare.view` → New → JFrame Form (or New → Other → Swing GUI Forms → JFrame Form). Enter `LoginFrame` for the first window. NetBeans creates both `LoginFrame.java` and `LoginFrame.form`.

In Design, use the Palette to drag JPanel, JLabel, JTextField, JPasswordField, JButton, JComboBox, JTable and JScrollPane. Rename a component in its Code properties / Change Variable Name. Use the exact names in `GUI_COMPONENTS.md`. Component display text and Java variable name are different properties.

The supplied forms use standard BorderLayout, GridLayout and FlowLayout containers; choose a container's Set Layout menu to match them. Place the common sidebar at WEST and the content area at CENTER. Forms contain actual input controls; they are not screenshots or HTML.

**Check:** project opens; Main and all packages are visible; CustomerFrame has a Design tab. **Commit:** `Initial project setup`. **Common error:** Java 8 cannot compile this project. Select a JDK 17+ Java platform; do not downgrade the release setting to 8.

## Phase 2 — Create the MySQL database

Start MySQL/MariaDB. With phpMyAdmin: Import → select `database.sql` → Go/Import. With Workbench: open the SQL script and execute it. The file creates `pet_grooming_db` and the six business tables plus `system_locks`.

Read `ARCHITECTURE.md` and the SQL in this order: users → customers → pets → services → appointments → payments. A primary key identifies one row. A foreign key points to an existing related row. `AUTO_INCREMENT` means the database supplies the new ID. `DECIMAL(10,2)` stores money with two decimal places.

The owner relationship is protected twice: the application filters the pet list, and the database checks the pet/customer pair. An appointment's booked price and duration are copied from the selected service. The unique payment appointment ID means an appointment cannot receive two payment rows.

The sample data includes two owners, three pets, five services, three appointments and one payment. All names/contact details are demo records. The administrator password is a salted hash, not a plaintext value.

**Check:** expand `pet_grooming_db`, browse each table and verify relationships. **Commit:** `Create relational database and sample data`. **Common errors:** if the table already exists, do not repeatedly import into your working database; back it up and use a fresh installation when practising. If a foreign key creation fails, verify InnoDB and import the complete script in order.

## Phase 3 — Configure JDBC

The dependency already appears in `pom.xml`:

```xml
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.4.0</version>
</dependency>
```

In this Maven project, NetBeans downloads the driver automatically. Expand Dependencies to see it. In an unrelated older Ant project you would use Libraries → Add JAR/Folder, but do not mix that approach into this project.

Copy `config.example.properties` to `config.properties`. The URL has this database portion: `jdbc:mysql://localhost:3306/pet_grooming_db`. Set your local username/password. Port 3306 is a default; use the port configured in your MySQL server.

**Check:** the MySQL dependency resolves without a red error mark. **Commit:** `Configure MySQL JDBC dependency` (commit the example, not your local password file). **Common errors:** internet/proxy errors during dependency download are different from a database connection error. Resolve the build first; then check whether MySQL is running.

## Phase 4 — Create DBConnection

Open `util/DBConnection.java`. A private constructor prevents unrelated code from constructing multiple configuration providers. The static `INSTANCE` field holds the one Singleton. Call it as follows:

```java
try (Connection connection = DBConnection.getInstance().getConnection()) {
    // Use the connection inside this block.
}
```

`getConnection()` loads defaults, optional local configuration and overrides. DriverManager opens a new JDBC connection. The try-with-resources block closes that connection, even if an exception occurs. The Singleton is the provider, not one permanent connection shared between SwingWorker threads.

**Check:** the Login module in phase 5 reaches the database. **Commit:** `Add configurable JDBC connection factory`. **Common errors:** Access denied means credentials; Unknown database means the script has not been imported; Communications link failure usually means server/host/port. `config.properties` is read relative to the working directory; the supplied launch scripts change to the project directory first.

## Phase 5 — Build Login

**Files:** `view/LoginFrame.java/.form`, `controller/LoginController.java`, `dao/UserDAO.java`, `model/User.java`, `util/PasswordUtil.java`, `util/Session.java`, `Main.java`.

**Build in Design:** drag labels and a JTextField `txtUsername`, JPasswordField `txtPassword`, JButton `btnLogin` and JButton `btnExit`. Give the buttons the visible texts “Sign in” and “Exit”. `getRootPane().setDefaultButton(btnLogin)` also allows Enter to submit.

**Follow the code:** the constructor runs `initComponents()`, applies the visual style and connects button listeners. `login()` reads input. A SwingWorker calls `LoginController.login()`. Empty inputs raise `ValidationException`. UserDAO fetches the matching username with a prepared statement. PasswordUtil derives a hash and compares it to the stored salted hash. Success saves the current User in Session, opens DashboardFrame and disposes LoginFrame. Password char arrays are cleared after use.

```java
btnLogin.addActionListener(e -> login());
```

This is a button-event connection. The method is outside NetBeans' guarded generated region, so regenerating the layout does not delete it. You can alternatively create a named actionPerformed handler in Design and call your own method from there; avoid registering the same action twice.

**SQL:** `SELECT * FROM users WHERE username=?`. The question mark is filled by `setString(1, username)`; the username does not become SQL syntax.

**Test:** blank inputs; wrong password; username containing an apostrophe; correct `admin / PawCare@123`; Enter key; Exit. **Commit:** `Create database-backed login interface`. **Common errors:** after changing the password, the demo password no longer works in that database. Do not replace `password_hash` with plaintext. Use the dashboard's password-change feature or a fresh demo database.

## Phase 6 — Build Dashboard

**Files:** `DashboardFrame.java/.form`, `DashboardController.java`, `DashboardDAO.java`, shared Ui/NavIcon helpers.

**Build in Design:** six card panels with title labels and the value labels `lblTotalCustomers`, `lblTotalPets`, `lblTodayAppointments`, `lblPendingAppointments`, `lblCompletedAppointments`, `lblTodayRevenue`. Add the nine navigation buttons listed in the component inventory, plus quick book/payment, refresh and change-password buttons.

**Follow the code:** navigation creates the destination JFrame before disposing the current frame. There is one main workspace window at a time. A five-second Swing Timer triggers `loadDashboardData()`. Its worker calls DashboardController → DashboardDAO. The worker's `done()` method updates labels on the Swing event thread. A `loading` flag prevents overlapping dashboard refreshes; the timer stops when the frame is disposed.

**SQL:** COUNT customers/pets; COUNT today's appointments; COUNT all Pending/Completed appointments; SUM payments whose payment date equals today's Sri Lanka date. An empty SUM becomes zero with COALESCE. Counts are not hard-coded.

**Test:** navigate to every screen; return using Back to Dashboard; confirm no stack of old dashboard windows remains. Statistics should match table data. Disconnect the database briefly: the status label should explain the outage. **Commit:** `Add dashboard layout and navigation`. **Common error:** use `.equals()` for strings, not `==`. Do not replace database values with demo numbers while styling cards.

## Phase 7 — Customer CRUD

**Files:** `Customer.java`, `CustomerController.java`, `CustomerDAO.java`, `CustomerFrame.java/.form`.

**Build in Design:** fields `txtCustomerId` (read-only), `txtCustomerName`, `txtPhone`, `txtEmail`, `txtAddress`; table `tblCustomers` inside a JScrollPane; search field `txtSearch`; Add/Update/Delete/Search/Clear/Back buttons.

**Follow the code:** `save(false)` creates a model with ID 0; the controller validates and calls DAO.insert. `save(true)` uses the selected ID and DAO.update. `refresh()` fetches rows into a list and rebuilds the table. `selectRow()` converts the sorted view row to its model row, then fills the form. `remove()` confirms deletion. `clear()` resets selection and form fields.

The DAO has `insert`, `findAll`, `findById`, `update`, `delete`, `bind` and `map`. `bind` puts model values into query placeholders; `map` creates a model from one ResultSet row. Generated keys supply the new customer ID.

**SQL:** INSERT into customers; SELECT with name/phone/email LIKE filters; UPDATE by primary key; DELETE by primary key. All input values use PreparedStatement.

**Test:** add a customer with a ten-digit phone; edit the address; search by part of the name; click a column heading to sort and edit the selected row; delete a customer without pets. Reject empty name/address, short phone and malformed email. **Commit:** `Implement customer management CRUD`. **Common error:** deleting an owner who has pets is intentionally blocked by the FK; remove dependencies in the correct order instead.

## Phase 8 — Pet CRUD

**Files:** `Pet.java`, `PetController.java`, `PetDAO.java`, `PetFrame.java/.form`, LookupController.

**Build in Design:** `txtPetId`, owner combo `cmbCustomer`, `txtPetName`, `cmbAnimalType`, `txtBreed`, `cmbGender`, `txtAge`, `tblPets`, search/actions. Set ID read-only. Animal choices are Dog/Cat/Rabbit/Other; gender choices are Male/Female.

**Follow the code:** the form loads customers from the database. A combo holds actual Customer objects; `toString()` supplies the visible owner name and `Ui.id()` retrieves its ID. No customer names are copied into a foreign-key field. `PetData` groups pet rows and owner choices returned by the background load.

Age is a positive integer from 1–100 years for this brief. Name and breed cannot be empty. The DAO joins customers to display the owner name beside pet details. Updating an owner link that conflicts with existing appointments is blocked by the composite FK.

**SQL:** pets CRUD; list query joins pets.customer_id to customers.customer_id. **Test:** add two pets under different owners, search by pet or owner, edit one and delete an unbooked pet. Try missing owner and non-numeric age. **Commit:** `Implement pet management CRUD`. **Common error:** an empty owner dropdown means no owners exist or the database load failed; add a customer first.

## Phase 9 — Service CRUD

**Files:** `GroomingService.java`, `ServiceController.java`, `ServiceDAO.java`, `ServiceFrame.java/.form`.

**Build in Design:** `txtServiceId`, `txtServiceName`, `txtDescription`, `txtPrice`, `txtDuration`, `tblServices`, common search/actions. Duration is minutes rather than an ambiguous text such as “one hour”.

**Follow the code:** price strings become BigDecimal through `Validation.money`. The controller requires a positive amount with at most two decimal places and duration 5–480 minutes. Service names are unique. The generated ID is read-only.

**SQL:** full CRUD for services. **Test:** add a service at 1250.00 for 30 minutes; update its price; search; delete before it is booked. Reject price 0, 1.999, a duplicate name and an invalid duration. **Commit:** `Implement grooming service management`. **Common error:** use a decimal point and digits only, such as `1500.00`; do not enter `Rs. 1,500` into the numeric input.

## Phase 10 — Appointments

**Files:** `Appointment.java`, `AppointmentController.java`, `AppointmentDAO.java`, `AppointmentFrame.java/.form`, InvalidAppointmentException.

**Build in Design:** `txtAppointmentId`, `cmbCustomer`, `cmbPet`, `cmbService`, `txtDate`, `txtTime`, `cmbStatus`, `tblAppointments`; Book, Update, Cancel appointment, Delete and common search/clear/back buttons.

**Follow the code:** owner selection calls `filterPets()` and includes only that owner's pets. `save()` parses ISO date and 24-hour time. The controller checks required selections and status values. The DAO verifies owner membership again, obtains service price/duration and checks whether the requested interval overlaps a non-cancelled appointment.

The overlap rule is `newStart < existingEnd && newEnd > existingStart`. A visit ending at 10:00 and one starting at 10:00 are allowed. This version has one grooming station. New or rescheduled visits must be in the future and finish before midnight.

The DAO begins a transaction, locks the single calendar row, checks conflicts and saves before committing. On failure it rolls back. This closes the race where two users could see an empty slot at the same moment. Cancelling sets status Cancelled; Delete removes the row after confirmation. Paid appointments must have their payment removed before cancellation/rescheduling/deletion.

**SQL:** joins customers/pets/services for the list; parameterized INSERT/UPDATE; date-based conflict SELECT; a separate cancellation UPDATE; DELETE. **Test:** book a future slot, try an overlap, book an adjacent slot, cancel and reuse the old slot. Try a past date, `25:00`, no pet, and an invalid customer/pet pair. **Commit:** `Implement appointment booking with conflict checks`. **Common error:** time is `14:30`, not `2.30 PM`; the form's service price comes from the selected service and is snapshotted on booking.

## Phase 11 — Payments and receipts

**Files:** `Payment.java`, `PaymentController.java`, `PaymentDAO.java`, `PaymentFrame.java/.form`, InvalidPaymentException, ReportService.

**Build in Design:** `txtPaymentId`, `cmbAppointment`, read-only `txtCustomer` / `txtService`, `txtAmount`, `cmbMethod`, `txtDate`, `tblPayments`; Make payment, Update, Delete, Print receipt and common actions.

**Follow the code:** `appointmentChanged()` supplies customer/service/amount from the selected appointment. This version requires one full payment equal to the booked price. PaymentController checks positive amount, known method and non-future date. PaymentDAO checks that the appointment exists, is not cancelled and does not already have another payment. A transaction uses the same calendar lock as booking changes.

Update changes method/date/amount validation while preserving the payment's appointment link. Delete removes only the recorded payment. The UI has no bank gateway. Print receipt compiles/fills the selected payment's JRXML and opens JasperViewer.

**SQL:** INSERT/SELECT/UPDATE/DELETE payments; joins provide customer/service columns. **Test:** select a booking and verify autofill, pay it, update Cash to Card, print receipt, try duplicate payment, delete and check revenue decreases. **Commit:** `Implement payment management and receipts`. **Common error:** “Amount must match” means this system has no partial payments; enter the appointment's quoted total.

## Phase 12 — Connect and verify live dashboard data

The supplied code already connects the cards so every module can be tested end to end. Now study DashboardDAO's six queries. Trace one payment from PaymentDAO commit to the next dashboard SELECT/SUM.

The current child screen reloads its JTable after saving. Returning to Dashboard creates a fresh dashboard and queries immediately. While visible, it polls every five seconds, so database changes from another application instance become visible. There is no hidden list of fixed card numbers.

**Test:** note totals, add a customer/pet/payment, return and compare. Delete the payment and confirm today's revenue drops. Check the date filter: a payment recorded for yesterday is excluded from today's revenue. **Commit:** `Connect dashboard statistics to database`. **Common error:** Pending/Completed cards cover all dates; Today's Appointments is a separate day-specific count.

## Phase 13 — Validation

Open `service/Validation.java` and each controller's `validate`/`save` method. Required text has length limits matching SQL columns. Phone requires exactly ten digits. Email has a basic format check. Price/amount uses BigDecimal and at most two decimal places. Dates use strict ISO parsing; times use strict HH:mm parsing. Combo values are checked against allowed values.

UI parsing gives immediate feedback, but controller and database checks remain necessary because a caller could bypass a form. Number/date parsing errors become understandable validation messages. Do not leave an empty catch block or silently replace invalid amounts with zero.

**Test:** `mvn test` includes validation tests. Also try common incorrect inputs in the UI. **Commit:** `Add shared input validation`. **Common error:** `Integer.parseInt` throws NumberFormatException for text; the helper catches that and explains the valid range.

## Phase 14 — Custom exceptions

Read all three classes in `exception/`. `InvalidAppointmentException` and `InvalidPaymentException` are user-defined checked exceptions. For example, AppointmentController contains:

```java
if (a.getPetId() <= 0) {
    throw new InvalidAppointmentException(
        "Please select a pet before booking the appointment.");
}
```

`throw new` creates and throws the error object. A `throws` clause declares that the method can propagate it. A try/catch block handles it. Because saving runs in SwingWorker, a background exception arrives wrapped by ExecutionException; Tasks extracts its cause and passes it to Ui.error.

**Test:** trigger a missing pet, overlap and duplicate payment. Locate each corresponding throw statement. **Commit:** `Add custom appointment and payment exceptions`. **Common error:** printing an exception without telling the user is not sufficient for form validation; dialogs/status messages make the failure visible.

## Phase 15 — Review MVC, DAO and Singleton

Take CustomerFrame's Add operation and draw its route: View → Controller → DAO → DBConnection → MySQL. Model data travels between these classes. SQL is absent from JFrame classes. A model's getters/setters do not connect to the database.

`DBConnection.INSTANCE` is the Singleton. Explain why its factory returns a new connection per operation: workers can overlap, and a shared connection's transaction state would be unsafe. `Tasks.run()` separates slow work from the UI event thread. `Session` only stores the logged-in User for navigation/password changes; it is not a database connection.

**Test:** search the `view` package for SELECT/INSERT/UPDATE; ordinary query code should be in DAOs, with report SQL in JRXML. **Commit:** `Review MVC DAO and Singleton boundaries`. **Common error:** do not paste SQL into button action handlers simply because NetBeans creates the handler there.

## Phase 16 — Jasper appointment report

**Files:** `report/ReportService.java`, `resources/reports/appointments.jrxml`, `view/ReportFrame.java/.form`.

JasperReports and its font library are already Maven dependencies. The provided JRXML is a complete template, so no report-design plugin is required. Open it as XML in NetBeans to inspect/edit it. If using a separate visual report designer, keep it compatible with this pinned Jasper 6 template format.

The template defines date parameters, a JOIN query, output fields, title, column header, detail rows, footer and record count. It connects appointments with customers, pets and services. A field name must match its SQL alias and Java type.

The three stages are:

```java
JasperReport compiled = JasperCompileManager.compileReport(templateStream);
JasperPrint filled = JasperFillManager.fillReport(compiled, parameters, connection);
JasperViewer.viewReport(filled, false);
```

Compile turns the design into a report; fill executes its query and produces pages; view displays those pages. `false` prevents closing a report viewer from exiting the entire application. Date parameters use `$P{FROM_DATE}` and `$P{TO_DATE}`.

**Test:** include a date range with bookings; verify customer, pet, service, date, time and status. Test an empty range. **Commit:** `Add Jasper appointment JOIN report`. **Common error:** do not rename a query alias without renaming its JRXML field. Keep font/report dependencies in the packaged JAR.

## Phase 17 — Jasper payment report

Open `payments.jrxml` and `receipt.jrxml`. The report connects payments → appointments → customers/services. A Jasper SUM variable computes the displayed total; each row shows amount, method and date. Receipt uses PAYMENT_ID to retrieve one payment.

ReportFrame has `txtFromDate`, `txtToDate`, `btnGenerateAppointments`, `btnGeneratePayments` and `btnExport`. Validate the range before generating. Export adds `.pdf` when omitted and asks before replacing an existing file. The PDF button exports the last generated report, even if you subsequently edit the date fields.

**Test:** compare report total to selected database payments; print-preview a receipt; export PDF and open it. **Commit:** `Add Jasper payment report and PDF export`. **Common errors:** a blank preview often means the date range contains no records; a file permission error means the chosen output folder is not writable. Save to your Documents folder and try again.

## Phase 18 — External REST API

**Files:** `BreedApiService.java`, `BreedFrame.java/.form`.

Drag a breed filter field `txtBreedFilter`, combo `cmbBreed`, buttons `btnLoadBreeds` / `btnImage`, an image label `lblBreedImage` and status/back controls. Click Load breeds to request the breed list. The filter changes the combo locally. Click Show breed image for an HTTP GET to the chosen breed's random-image endpoint.

HttpClient creates requests; the status code is checked; Gson parses JSON. The JSON `message` is either a breed map or a photo URL. A second request downloads image bytes for ImageIO. Images are scaled to fit the preview while preserving their aspect ratio. Breed paths are validated and photo addresses must use HTTPS on the expected image host.

Requests have timeouts, and failures are caught. The feature is loaded on demand; Login, CRUD and reports do not call the external API. A network outage here does not make local MySQL functions unavailable.

**Test:** load list, filter `poodle`, view a photo, select a sub-breed and retry with internet unavailable. **Commit:** `Integrate optional dog breed REST API`. **Common error:** the API returns JSON with an image URL, not image bytes in the first response. Do not pass the JSON body to ImageIO.

## Phase 19 — Colours, icons and layout

Read Ui.style and NavIcon. Navy is the main navigation/background accent; green marks primary save/book/login actions; blue marks information/update/search; red identifies Delete. Gray supports secondary actions. Text remains the primary action label. The colour rationale is in `ARCHITECTURE.md`.

The `.form` files hold layout, components and their names. Shared runtime styling is applied after `initComponents()`, which explains why Design preview may look plainer than the running application. Change layout in Design and shared colours in Ui; avoid editing generated blocks by hand. The app uses app-drawn icons, so there are no fragile absolute icon paths.

**Test:** resize the window, scroll long tables, sort/select rows, and confirm button labels remain readable. **Commit:** `Improve UI colours and navigation icons`. **Common error:** removing or renaming a component used by handwritten event code needs corresponding refactoring in that code.

## Phase 20 — Feature tracking with Git

Read `GITHUB.md`. Supplied branches are login, dashboard, customer-management, pet-management, service-management, appointments, payments, reports and api-integration under `feature/`. The supplied bundle preserves the actual generated history. Add your own implementation/improvement commits as you learn.

Before committing: `git status`, `git add` the intended files, `git diff --staged`, then `git commit -m "Clear change message"`. For form edits, stage `.java` and `.form` together. Return to main and merge when the feature is tested.

**Test:** `git log --oneline --graph --all`. **Commit:** describe the actual feature you changed. **Common error:** `git add.` is invalid; the space in `git add .` matters.

## Phase 21 — Build the runnable application

Use NetBeans Clean and Build or `mvn clean package`. Compiler → tests → package → shade is the build sequence. Maven Shade packages runtime dependencies and writes the main class in the manifest. The output is `target/PawCare.jar`.

Run `java -jar target/PawCare.jar` from the project folder. `build.bat` also copies it into dist. `run.bat` picks a newly built target JAR first. End users need Java 17+ and MySQL configured; a single JAR cannot replace the database server or Java installation.

**Test:** close NetBeans and launch the JAR with run.bat. Test login, booking and a report. **Commit:** `Prepare runnable application and launch scripts`. **Common errors:** UnsupportedClassVersionError means Java is too old; no main manifest attribute means you selected an unshaded/original JAR; a missing driver means the wrong JAR/build configuration was used.

## Phase 22 — SQL export and backup

`database.sql` is the fresh-install schema/sample script supplied for coursework. It is not a backup of records you later add. To export your actual developed data, select the database in phpMyAdmin → Export → SQL, or use Workbench's data export. Name your working backup clearly and restore it into a separate database to check it.

Do not replace the starter script with a partial one-table export. Preserve table definitions, foreign keys and necessary seed configuration such as the calendar lock row. Remove real customer details from a public coursework repository.

**Test:** restore into a new local test database and use a copied config to point the app at it. **Commit:** `Add database setup script` or a truthful message for a changed schema. **Common error:** exporting data only will not create the missing table structures on another computer.

## Phase 23 — README and learning documentation

The supplied README explains installation, architecture, features, API, reports, assumptions and tests. Update it after your changes. `GUI_COMPONENTS.md` is a quick variable-name reference; `TEST_RESULTS.md` separates automated verification from checks that must still be performed on your target computer.

Add your own application screenshots if you customize the design. Keep names/paths relative so documentation still works after cloning. Do not document features that have not been implemented.

**Test:** follow your README from a freshly extracted folder. **Commit:** `Document setup and user workflow`. **Common error:** running only inside NetBeans can conceal missing local config or packaged resources; follow the independent JAR instructions too.

## Phase 24 — Final GitHub submission and viva

Use your GitHub account to create the repository, push main and confirm its files and history are visible. Add the actual link to `SUBMISSION.md`; the generated project does not claim a published repository. Include your student/group information and any lecturer-required paperwork.

Practise the short demonstration in `SUBMISSION.md`. Explain why a specific query is in a DAO, where a custom exception is thrown, how a price snapshot works and which tables each report joins. Show the `.form` Design tab and one of your own commits.

**Test:** open the GitHub link and clone it into another folder, configure MySQL, build and run. **Commit:** `Prepare final coursework submission`. **Common error:** a local Git repository or bundle is not automatically uploaded to GitHub; a successful push and a real remote URL are still required.
