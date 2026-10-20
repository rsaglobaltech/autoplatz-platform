Feature: Vehicle type names are unique and not empty

  @REQ-004 @SCN-004
  Scenario: A duplicate name is rejected
    Given the catalog contains "Car", "Motorcycle" and "Van"
    When the administrator saves a type named "Car"
    Then the save is rejected with a validation message stating the name already exists
    And the catalog still contains exactly one type named "Car"

  @REQ-004 @SCN-005
  Scenario: An empty name is rejected
    Given the catalog contains "Car", "Motorcycle" and "Van"
    When the administrator saves a type with the name ""
    Then the save is rejected with a validation message stating the name is required
    And the catalog still contains exactly "Car", "Motorcycle" and "Van"

  @REQ-004 @SCN-020
  Scenario: A duplicate in other case and spaces is rejected
    Given the catalog contains "Car", "Motorcycle" and "Van"
    When the administrator saves a type named " car "
    Then the save is rejected with a validation message stating the name already exists
    And the catalog still contains exactly one type named "Car"
