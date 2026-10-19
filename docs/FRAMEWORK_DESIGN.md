# Framework design

## Layers

1. `src/core`: configuration, browser and test lifecycle.
2. `src/pages`: page-specific locators and user actions.
3. `src/tests`: TestNG test classes and SoftAssert.
4. `src/support`: DataProvider, listener, screenshots and test-data models.

Package paths are intentionally direct and domain-focused:

```text
src/core
src/pages
src/tests
src/support
data
config
```

The project does not use personal-name or ceremonial `main/test/java` package layers.
Because this repository contains only test automation code, `src` is configured as
the single Maven test source root. Every visible folder describes a real responsibility.

## Runtime flow

```text
testng.xml
  -> BaseTest.@BeforeMethod
  -> TestConfig (system property overrides XML)
  -> BrowserFactory
  -> LoginPage / DashboardPage
  -> SoftAssert.assertAll()
  -> TestListener (result + failure screenshot)
  -> BaseTest.@AfterMethod (quit driver)
```

`BasePage` sets no implicit wait. Page actions use explicit waits against visible
or clickable UI state. Selenium Manager resolves the browser driver automatically.

## Behavior-based data-driven tests

Login coverage combines behavior separation with external CSV data:

- Each `LoginTest` method owns one expected behavior: successful login, invalid
  credentials, username required, password required, or both fields required.
- Each named `LoginDataProvider` filters the shared CSV by `ExpectedResult` and
  supplies only the rows relevant to that behavior.
- `LoginPage` owns browser interactions; test methods own verification.
- Adding another row for an existing behavior requires only a CSV change, not a
  new test method.

## Configuration precedence

```text
Maven -D property > testng.xml parameter > framework default
```

Supported properties are `browser`, `headless`, `baseUrl` and `timeoutSeconds`.

## TestNG lifecycle demonstration

`LifecycleAnnotationsTest` logs all suite, test, class, group and method before/after
annotations. It is intentionally separate from browser setup so educational lifecycle
coverage does not force unnecessary WebDriver work into the production test flow.
