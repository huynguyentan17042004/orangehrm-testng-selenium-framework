# Hướng dẫn chạy OrangeHRM TestNG Selenium Framework

## 1. Yêu cầu môi trường

- macOS, Linux hoặc Windows có Bash tương thích.
- Java 21.
- Maven 3.10 trở lên.
- Google Chrome; Firefox chỉ cần khi chạy `--browser firefox`.

Kiểm tra:

```bash
java -version
mvn -version
```

Maven phải hiển thị Java 21. Trên macOS dùng Homebrew, có thể cấu hình:

```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
```

Project tự động dùng `config/maven-settings.xml` thông qua `.mvn/maven.config` để
tải dependency và plugin từ Maven Central. Cấu hình này tránh Maven truy cập các
snapshot repository không cần thiết từ metadata của plugin.

## 2. Đi đến thư mục project

```bash
cd orangehrm-testng-selenium-framework
```

## 3. Chạy bằng Bash script

Script mặc định chạy toàn bộ suite bằng Chrome headless:

```bash
./run-tests.sh
```

Hiển thị trình duyệt khi chạy:

```bash
./run-tests.sh --headed
```

Chạy theo TestNG group:

```bash
./run-tests.sh --group smoke
./run-tests.sh --group regression
./run-tests.sh --group lifecycle
```

Chọn trình duyệt:

```bash
./run-tests.sh --browser chrome
./run-tests.sh --browser firefox
```

Kết hợp nhiều lựa chọn:

```bash
./run-tests.sh --browser chrome --group regression --headed --timeout 20
```

Ghi đè URL kiểm thử:

```bash
./run-tests.sh --base-url "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
```

Xem Maven command mà không thực thi:

```bash
./run-tests.sh --group smoke --headed --dry-run
```

Xem toàn bộ lựa chọn:

```bash
./run-tests.sh --help
```

## 4. Sinh Allure report

Chạy test và sinh report tĩnh:

```bash
./run-tests.sh --report
```

Report được tạo tại:

```text
target/site/allure-maven-plugin/index.html
```

Chạy test và mở Allure bằng local server:

```bash
./run-tests.sh --serve-report
```

Nhấn `Ctrl+C` để dừng server.

## 5. Chạy Maven trực tiếp

Toàn bộ suite:

```bash
mvn clean test
```

Hiện Chrome:

```bash
mvn clean test -Dheadless=false
```

Chạy theo group:

```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
mvn clean test -Dgroups=lifecycle
```

Sinh Allure report:

```bash
mvn allure:report
```

## 6. Test data và tài khoản demo

Test data nằm tại:

```text
data/login-data.csv
```

Framework mặc định dùng tài khoản demo công khai:

```text
Username: Admin
Password: admin123
```

Có thể ghi đè bằng biến môi trường:

```bash
export ORANGEHRM_USERNAME="Admin"
export ORANGEHRM_PASSWORD="admin123"
./run-tests.sh
```

Password không được ghi vào log.

## 7. Kết quả sau khi chạy

| Nội dung | Đường dẫn |
|---|---|
| TestNG/Surefire report | `target/surefire-reports/` |
| Allure raw results | `target/allure-results/` |
| Allure HTML report | `target/site/allure-maven-plugin/` |
| Execution log | `logs/test-execution.log` |
| Screenshot khi test lỗi | `artifacts/screenshots/` |

`target`, `logs` và `artifacts` là output tự sinh, không phải source code và không
được commit lên GitHub.

## 8. Lỗi thường gặp

### `skip non existing resourceDirectory src/main/resources`

Đây là thông báo `INFO`, không phải lỗi. Framework cố ý dùng cấu trúc rút gọn
`src/core`, `src/pages`, `src/support`, `src/tests` thay cho `src/main` và
`src/test`; Maven vẫn biên dịch test từ `src` theo cấu hình trong `pom.xml`.

### Maven dùng sai Java

```bash
mvn -version
```

Nếu không phải Java 21, đặt lại `JAVA_HOME` như phần 1.

### Script không có quyền chạy

```bash
chmod +x run-tests.sh
```

### Firefox chưa được cài

Chạy bằng Chrome hoặc cài Firefox trước:

```bash
./run-tests.sh --browser chrome
```

### Cảnh báo CDP version

Framework không dùng Chrome DevTools API, vì vậy cảnh báo Selenium tìm phiên bản
CDP gần nhất không ảnh hưởng đến các test WebDriver hiện tại.

### Maven cố truy cập `apache.snapshots`

Hãy chạy Maven từ thư mục gốc của project để `.mvn/maven.config` được áp dụng.
Không cần thêm `--settings` thủ công.
