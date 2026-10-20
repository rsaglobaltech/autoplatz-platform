package com.autoplatz;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The hexagonal architecture, checked on every build: the domain is pure Java, the application knows no
 * frameworks or adapters, and modules are not coupled through their infrastructure.
 */
@AnalyzeClasses(packages = "com.autoplatz", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {
  @ArchTest
  static final ArchRule domainIsPureJava = noClasses().that().resideInAPackage("..domain..")
      .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..",
          "org.springframework..", "jakarta..", "io.swagger..");

  @ArchTest
  static final ArchRule applicationHasNoFrameworkNorAdapters = noClasses().that().resideInAPackage("..application..")
      .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..", "org.springframework..", "jakarta..", "io.swagger..");

  @ArchTest
  static final ArchRule portsAreInterfaces = classes().that().resideInAPackage("..application.port..")
      .should().beInterfaces();

  @ArchTest
  static final ArchRule modulesDoNotUseEachOthersInfrastructure = slices().matching("com.autoplatz.(*).infrastructure..")
      .should().notDependOnEachOther();

  @ArchTest
  static final ArchRule noCycles = slices().matching("com.autoplatz.(*)..").should().beFreeOfCycles();
}
