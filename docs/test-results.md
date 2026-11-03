# Week 6 test results

Command: mvn clean test
Date: 2026-11-03
Result: Tests run: 213, Failures: 0, Errors: 0, Skipped: 0 (BUILD SUCCESS)

Week 6 added 25 tests on top of 188:
ImportConfigTest (7), ImportReportTest (2), OrderImporterTest (7),
OrderRecordParserTest (7), ReportExporterTest (2).

Covered cases: valid file, malformed dates, missing fields, empty file,
duplicate records, unavailable paths.