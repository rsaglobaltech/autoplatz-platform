<!-- specgate:derived — generated from spec.md, features/ and tests. Do not edit by hand: run `specgate matrix`. -->
# Traceability Matrix — autoplatz-platform

Map requirements to scenarios, domain model elements, implementation artifacts, and tests.

| Requirement | Scenario ID | Feature file | Use Case | Command/Query | Aggregate | Event | Technical artifact | Test artifact | Status |
|---|---|---|---|---|---|---|---|---|---|
| REQ-001 | SCN-001 | `features/adoption/baseline.feature` | UC-001 Preserve existing behaviour | - | - | - | `existing codebase` | npm test | Draft |
| REQ-002 | SCN-002 | `features/parking/REQ-002.feature` | - | - | - | - | `apps/api/src/main/java/com/autoplatz/parking/application/SeedDefaultVehicleTypes.java, apps/api/src/main/java/com/autoplatz/parking/domain/VehicleType.java` | apps/api/src/test/java/com/autoplatz/parking/application/VehicleTypeUseCasesTest.java, apps/api/src/test/java/com/autoplatz/parking/infrastructure/VehicleTypeApiTest.java | Implemented |
| REQ-003 | SCN-003 | `features/parking/REQ-003.feature` | - | - | - | - | `apps/api/src/main/java/com/autoplatz/parking/application/AddVehicleType.java, apps/web/app/catalog/page.tsx` | apps/api/src/test/java/com/autoplatz/parking/application/VehicleTypeUseCasesTest.java, apps/api/src/test/java/com/autoplatz/parking/infrastructure/AddVehicleTypeApiTest.java | Implemented |
| REQ-004 | SCN-004 | `features/parking/REQ-004.feature` | - | - | - | - | `apps/api/src/main/java/com/autoplatz/parking/application/AddVehicleType.java, apps/api/src/main/java/com/autoplatz/parking/domain/VehicleTypeName.java` | apps/api/src/test/java/com/autoplatz/parking/application/VehicleTypeUseCasesTest.java, apps/api/src/test/java/com/autoplatz/parking/infrastructure/AddVehicleTypeApiTest.java | Implemented |
| REQ-005 | SCN-006 | `features/parking/REQ-005.feature` | - | - | - | - | `apps/api/src/main/java/com/autoplatz/parking/application/DeleteVehicleType.java, apps/api/src/main/java/com/autoplatz/parking/application/ListEntryHistory.java, apps/api/src/main/java/com/autoplatz/parking/application/port/EntryRepository.java, apps/web/app/catalog/actions.ts, apps/web/app/catalog/page.tsx` | apps/api/src/test/java/com/autoplatz/parking/application/VehicleTypeUseCasesTest.java, apps/api/src/test/java/com/autoplatz/parking/infrastructure/DeleteVehicleTypeApiTest.java | Implemented |
