Feature: Valid entry is stored as inside

  @REQ-017 @SCN-028
  Scenario: A valid entry is stored and listed
    Given no entry with status "inside" exists for the license plate "AB123CD"
    And the administrator types the license plate "AB123CD", selects the vehicle type "Car" and leaves the owner name empty
    When the administrator saves the entry
    Then the entry is stored in the database with status "inside"
    And the license plate "AB123CD" appears in the list of vehicles currently in the garage
