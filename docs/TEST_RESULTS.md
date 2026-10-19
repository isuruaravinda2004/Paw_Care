# Verification results

The delivered application was compiled and tested with Java 17 on Linux. The main database and desktop run used MariaDB 10.11.14 through MySQL Connector/J 8.4.0. The default isolated H2 tests were also run during development.

## Automated test run

**15 tests passed, 0 failures, 0 errors, 0 skipped** in the final run with desktop tests enabled.

| Test class | Passed tests | Coverage |
|---|---:|---|
| WorkflowTest | 7 | Login/password change; customer, pet and service CRUD; FK protection; appointment/payment CRUD; duplicate payment; revenue; overlap/adjacent slots; invalid owner/past date; price snapshots; simultaneous booking |
| ValidationTest | 4 | Numeric/date/time checks, phone/email, salted hashes, breed JSON parsing and malformed input |
| ReportTest | 2 | Compile/fill/export all three joined templates, expected customer/service data, empty range |
| GuiSmokeTest | 2 | All nine windows open; actual UI clicks through login → customer → pet → booking → payment → dashboard |

The final desktop run captured the screenshots in `screenshots/`. The payment screen autofill and final dashboard revenue were asserted during the actual Swing button workflow. These are screenshots of the working Java application.

## Packaged application check

The shaded JAR was loaded independently of NetBeans. Database login, JDBC driver service discovery and all three Jasper report/PDF exports succeeded using the packaged classes/resources and an isolated test database. Jasper extension properties are merged during packaging so bundled font registration is preserved. The JAR's application classes were also compared with the compiled class output.

The three sample PDFs in `sample-reports/` were produced by that packaged-JAR check. Their first pages were rendered and visually inspected for readable headings, rows, amounts, totals and footer alignment. The samples use demonstration data.

## Live API check

The real `BreedApiService` fetched the Dog CEO breed list and a Poodle reference photo. The response contained 207 breed/sub-breed choices; the returned image was 500 × 375 pixels. These are observations from one run, not promised constant API values. JSON parsing and invalid-path handling also have deterministic tests. Failure to reach this optional service does not affect core MySQL operations.

## Reproduce tests

Default local tests, without MySQL or a desktop display:

```bash
mvn test
```

This runs 13 tests; the two optional GUI tests are skipped unless enabled. On a computer with a desktop display:

```bash
mvn test -Dpawcare.uiTests=true
```

To test against a separate MySQL/MariaDB database, create an empty database whose name ends in `_test`, give a dedicated test account access to it, and provide the test properties:

```bash
mvn test -Dpawcare.testUrl=jdbc:mysql://localhost:3306/pawcare_test -Dpawcare.testUser=YOUR_TEST_USER -Dpawcare.testPassword=YOUR_TEST_PASSWORD
```

The test fixture drops and recreates tables inside that dedicated test database. It rejects external database names that do not end in `_test`; do not point it at real customer data. GUI tests can be enabled at the same time with `-Dpawcare.uiTests=true`.

## Remaining environment checks

The Windows launcher, physical printer output, Oracle MySQL 8 server, and NetBeans GUI Builder editing were not executed in this Linux validation environment. Nine paired `.form` files are included and their XML was parsed, but open Design and perform a small edit/build in your installed NetBeans before your viva. The supplied Windows script is straightforward; Java 17+ and the database are prerequisites. GitHub publishing needs your own account and was not performed.

The implementation follows the documented single-station/full-payment scope.
