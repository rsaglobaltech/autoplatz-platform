Feature: Default vehicle types on first start

  @REQ-002 @SCN-002
  Scenario: Fresh installation seeds the default types
    Given a fresh installation with an empty vehicle type catalog
    When the system starts
    Then the catalog contains exactly the vehicle types "Car", "Motorcycle" and "Van"
    And each of them is active and available in the entry form
