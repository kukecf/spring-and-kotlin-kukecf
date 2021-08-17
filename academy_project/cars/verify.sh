#!/usr/bin/env bash
# build will automatically run detekt, ktlint and test tasks
./gradlew clean build jacocoTestReport
zip -r9 reports.zip build/reports