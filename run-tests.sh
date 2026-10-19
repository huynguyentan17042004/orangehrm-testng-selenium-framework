#!/usr/bin/env bash

set -Eeuo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

BROWSER="chrome"
HEADLESS="true"
GROUP="all"
TIMEOUT_SECONDS="15"
BASE_URL="https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
CLEAN_BUILD="true"
GENERATE_REPORT="false"
SERVE_REPORT="false"
DRY_RUN="false"

usage() {
    cat <<'EOF'
OrangeHRM TestNG Selenium runner

Usage:
  ./run-tests.sh [options]

Options:
  --headed                 Show the browser while tests run.
  --headless               Run without showing the browser (default).
  --browser <name>         chrome or firefox (default: chrome).
  --group <name>           all, smoke, regression, or lifecycle (default: all).
  --timeout <seconds>      Explicit-wait timeout (default: 15).
  --base-url <url>         Override the OrangeHRM login URL.
  --no-clean               Run test without Maven clean.
  --report                 Generate a static Allure HTML report after tests.
  --serve-report           Generate and open Allure report after tests.
  --dry-run                Print the commands without executing them.
  -h, --help               Show this help.

Examples:
  ./run-tests.sh
  ./run-tests.sh --headed
  ./run-tests.sh --group smoke --headed
  ./run-tests.sh --browser firefox --group regression
  ./run-tests.sh --group lifecycle
  ./run-tests.sh --report
EOF
}

require_value() {
    local option="$1"
    local value="${2:-}"
    if [[ -z "$value" || "$value" == --* ]]; then
        echo "Error: $option requires a value." >&2
        usage >&2
        exit 2
    fi
}

while [[ $# -gt 0 ]]; do
    case "$1" in
        --headed)
            HEADLESS="false"
            shift
            ;;
        --headless)
            HEADLESS="true"
            shift
            ;;
        --browser)
            require_value "$1" "${2:-}"
            BROWSER="$2"
            shift 2
            ;;
        --group)
            require_value "$1" "${2:-}"
            GROUP="$2"
            shift 2
            ;;
        --timeout)
            require_value "$1" "${2:-}"
            TIMEOUT_SECONDS="$2"
            shift 2
            ;;
        --base-url)
            require_value "$1" "${2:-}"
            BASE_URL="$2"
            shift 2
            ;;
        --no-clean)
            CLEAN_BUILD="false"
            shift
            ;;
        --report)
            GENERATE_REPORT="true"
            shift
            ;;
        --serve-report)
            GENERATE_REPORT="true"
            SERVE_REPORT="true"
            shift
            ;;
        --dry-run)
            DRY_RUN="true"
            shift
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            echo "Error: unknown option '$1'." >&2
            usage >&2
            exit 2
            ;;
    esac
done

case "$BROWSER" in
    chrome|firefox) ;;
    *)
        echo "Error: --browser must be chrome or firefox." >&2
        exit 2
        ;;
esac

case "$GROUP" in
    all|smoke|regression|lifecycle) ;;
    *)
        echo "Error: --group must be all, smoke, regression, or lifecycle." >&2
        exit 2
        ;;
esac

if [[ ! "$TIMEOUT_SECONDS" =~ ^[1-9][0-9]*$ ]]; then
    echo "Error: --timeout must be a positive integer." >&2
    exit 2
fi

if [[ ! "$BASE_URL" =~ ^https?:// ]]; then
    echo "Error: --base-url must begin with http:// or https://." >&2
    exit 2
fi

if ! command -v mvn >/dev/null 2>&1; then
    echo "Error: Maven was not found. Install it with: brew install maven" >&2
    exit 1
fi

if [[ -x /usr/libexec/java_home ]] && JAVA_21_HOME="$(/usr/libexec/java_home -v 21 2>/dev/null)"; then
    export JAVA_HOME="$JAVA_21_HOME"
elif [[ -d /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home ]]; then
    export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
else
    echo "Error: Java 21 was not found." >&2
    exit 1
fi
export PATH="$JAVA_HOME/bin:$PATH"

MAVEN_PHASES=(test)
if [[ "$CLEAN_BUILD" == "true" ]]; then
    MAVEN_PHASES=(clean test)
fi

MAVEN_OPTIONS=(
    "-Dbrowser=$BROWSER"
    "-Dheadless=$HEADLESS"
    "-DtimeoutSeconds=$TIMEOUT_SECONDS"
    "-DbaseUrl=$BASE_URL"
)

if [[ "$GROUP" != "all" ]]; then
    MAVEN_OPTIONS+=("-Dgroups=$GROUP")
fi

print_command() {
    printf '  '
    printf '%q ' "$@"
    printf '\n'
}

echo "Configuration"
echo "  Project : $PROJECT_DIR"
echo "  Java    : $JAVA_HOME"
echo "  Browser : $BROWSER"
echo "  Headless: $HEADLESS"
echo "  Group   : $GROUP"
echo "  Timeout : ${TIMEOUT_SECONDS}s"
echo "  Base URL: $BASE_URL"
echo
echo "Test command"
print_command mvn "${MAVEN_PHASES[@]}" "${MAVEN_OPTIONS[@]}"

if [[ "$DRY_RUN" == "true" ]]; then
    if [[ "$GENERATE_REPORT" == "true" ]]; then
        echo "Report command"
        if [[ "$SERVE_REPORT" == "true" ]]; then
            print_command mvn allure:serve
        else
            print_command mvn allure:report
        fi
    fi
    exit 0
fi

cd "$PROJECT_DIR"
mvn "${MAVEN_PHASES[@]}" "${MAVEN_OPTIONS[@]}"

if [[ "$GENERATE_REPORT" == "true" ]]; then
    if [[ "$SERVE_REPORT" == "true" ]]; then
        echo "Starting Allure report server. Press Ctrl+C to stop it."
        mvn allure:serve
    else
        mvn allure:report
        echo "Allure report: $PROJECT_DIR/target/site/allure-maven-plugin/index.html"
    fi
fi
