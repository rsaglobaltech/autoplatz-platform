Feature: Add a vehicle type

  @REQ-003 @SCN-003
  Scenario: A type with a unique name becomes available in the entry form
    Given the catalog contains "Car", "Motorcycle" and "Van"
    And the administrator is on the catalog screen
    When the administrator adds a type named "Truck"
    Then the catalog contains "Truck" as an active type
    And the entry form offers "Car", "Motorcycle", "Van" and "Truck"
