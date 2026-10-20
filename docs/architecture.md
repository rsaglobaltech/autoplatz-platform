# Architecture

Hexagonal (ports and adapters) per module. There is a single rule: **dependencies point towards the
domain**.

```
infrastructure  ──►  application  ──►  domain
 (Spring/Quarkus,     (use cases,        (entities, value objects,
  REST, JDBC,          ports)             rules: pure Java)
  security)
```

| Layer          | Package                                 | May use                                | May not use                            |
| -------------- | --------------------------------------- | -------------------------------------- | -------------------------------------- |
| Domain         | `com.autoplatz.<module>.domain`         | standard Java, `shared.domain`         | framework, application, infrastructure |
| Application    | `com.autoplatz.<module>.application`    | domain, its ports (`application.port`) | framework, infrastructure              |
| Infrastructure | `com.autoplatz.<module>.infrastructure` | all of the above and the framework     | another module's infrastructure        |

Use cases carry no framework annotations: they are instantiated in
`infrastructure/config`. That way they are tested with in-memory doubles, without starting anything.

`ArchitectureTest` (ArchUnit) checks these rules on every build: a framework-free domain, an application
without infrastructure, ports as interfaces, and modules with no dependencies between their infrastructures
and no cycles.

## Adding a module

1. `domain/`: the aggregate and its value objects, with their unit tests.
2. `application/port/`: the interfaces it needs (repository, external services).
3. `application/`: one use case per class; tests with in-memory doubles.
4. `infrastructure/`: the REST adapter, the persistence adapter (and its migration in
   `src/main/resources/db/migration`) and the configuration that wires them.
5. An end-to-end API test.

## Errors

Every error comes out as RFC 9457 (`application/problem+json`) with a stable `code`
(`task_not_found`, `invalid_title`…): clients decide on the `code`, not on the text.

## A test that starts the real application

`StartupTest` starts the application with its real pieces, no doubles. The other tests replace pieces;
this one guarantees that the real ones exist and are wired.
