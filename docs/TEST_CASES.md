# Login decision table

| ID | Username | Password | Expected UI state | Assertion object |
|---|---|---|---|---|
| `valid-login` | Existing | Correct | Dashboard opens | `DashboardPage` heading, URL, user menu |
| `username-required` | Empty | Present | Username validation | `LoginPage` username error |
| `password-required` | Present | Empty | Password validation | `LoginPage` password error |
| `both-required` | Empty | Empty | Two validations | Both `LoginPage` field errors |
| `unknown-user` | Unknown | Present | Invalid credentials | `LoginPage` alert |
| `wrong-password` | Existing | Incorrect | Invalid credentials | `LoginPage` alert |

OrangeHRM intentionally gives the same UI error for an unknown username and a wrong
password, so UI automation must not claim it can distinguish those server-side states.

The executable values live in `data/login-data.csv`. Public demo
credentials default to `Admin` / `admin123` and can be overridden through the
`ORANGEHRM_USERNAME` and `ORANGEHRM_PASSWORD` environment variables.

Rows are routed to behavior-specific TestNG providers: `validLoginData`,
`invalidLoginData`, `usernameRequiredData`, `passwordRequiredData`, and
`bothRequiredData`.
