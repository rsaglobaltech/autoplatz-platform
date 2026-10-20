Feature: A type in use is deactivated, not deleted

  @REQ-005 @SCN-006
  Scenario: Deleting a used type deactivates it
    Given the catalog contains the active type "Van"
    And one existing entry has the vehicle type "Van"
    When the administrator deletes the type "Van"
    Then "Van" remains in the catalog marked as inactive
    And the entry form no longer offers "Van"
    And the existing entry still shows the vehicle type "Van" in history

  @REQ-005 @SCN-021
  Scenario: Deleting an unused type removes it
    Given the catalog contains the active type "Truck"
    And no entry has the vehicle type "Truck"
    When the administrator deletes the type "Truck"
    Then the catalog no longer contains "Truck"
    And the entry form no longer offers "Truck"
