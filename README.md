# OrangeHRM TestNG Selenium Mini Framework

Framework UI mini chạy trên OrangeHRM demo thật, dùng Java 21, Maven, Selenium,
TestNG, Page Object Model, external CSV DataProvider, SoftAssert, SLF4J/Logback,
failure screenshot và Allure report.

Source tree chỉ còn các nhóm có ý nghĩa: `src/core`, `src/pages`, `src/tests`,
`src/support`, `data` và `config`. Vì project này chỉ chứa test automation,
`src` được khai báo trực tiếp là Maven test source root trong `pom.xml`.

Hướng dẫn chạy đầy đủ: [`RUN_GUIDE.md`](RUN_GUIDE.md).

## Yêu cầu

- Java 21
- Maven 3.10+
- Google Chrome hoặc Firefox

Kiểm tra môi trường:

```bash
java -version
mvn -version
```

## Chạy test

### Chạy bằng Bash script

```bash
./run-tests.sh
```

Một số lựa chọn thường dùng:

```bash
./run-tests.sh --headed
./run-tests.sh --group smoke --headed
./run-tests.sh --browser firefox --group regression
./run-tests.sh --group lifecycle
./run-tests.sh --report
./run-tests.sh --help
```

Xem cấu hình và Maven command mà không chạy test:

```bash
./run-tests.sh --group smoke --headed --dry-run
```

### Chạy Maven trực tiếp

Mặc định `testng.xml` chạy Chrome headless:

```bash
mvn clean test
```

Hiển thị browser:

```bash
mvn clean test -Dheadless=false
```

Chạy smoke hoặc regression:

```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
```

Ghi đè cấu hình XML:

```bash
mvn clean test -Dbrowser=chrome -Dheadless=true -DtimeoutSeconds=20
```

Nếu tài khoản public demo thay đổi:

```bash
export ORANGEHRM_USERNAME='Admin'
export ORANGEHRM_PASSWORD='admin123'
mvn clean test
```

## Báo cáo và bằng chứng

- TestNG/Surefire: `target/surefire-reports/`
- Allure raw results: `target/allure-results/`
- Log: `logs/test-execution.log`
- Screenshot khi thất bại: `artifacts/screenshots/`

Sinh report tĩnh:

```bash
mvn allure:report
```

Mở report qua local server:

```bash
mvn allure:serve
```

## Tài liệu học

- `docs/FRAMEWORK_DESIGN.md`: kiến trúc và luồng chạy.
- `docs/TEST_CASES.md`: decision table và assertion objects.
- `docs/TEST_COVERAGE.md`: requirement/scenario coverage.

Selenium Manager đi kèm Selenium nên không cần tải ChromeDriver thủ công.
