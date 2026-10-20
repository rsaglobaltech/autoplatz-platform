Feature: Entry form fields

  @REQ-012 @SCN-022
  Scenario: The entry form shows its fields
    Given the vehicle type catalog contains the active type "Car"
    When the administrator opens the entry form and it is displayed
    Then it contains a license plate field marked as required
    And it contains a vehicle type field marked as required that offers the types of the vehicle type catalog, including "Car"
    And it contains an owner name field marked as optional
    And it contains an entry date/time field
