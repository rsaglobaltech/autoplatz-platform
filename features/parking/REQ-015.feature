Feature: Required fields

  @REQ-015 @SCN-025
  Scenario: Missing license plate blocks the save
    Given the administrator leaves the license plate empty and selects the vehicle type "Car"
    When the administrator saves the entry
    Then no entry is stored
    And the license plate field is highlighted as missing

  @REQ-015 @SCN-026
  Scenario: Missing vehicle type blocks the save
    Given the administrator types the license plate "AB123CD" and selects no vehicle type
    When the administrator saves the entry
    Then no entry is stored
    And the vehicle type field is highlighted as missing
