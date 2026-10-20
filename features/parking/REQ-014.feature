Feature: License plate normalization

  @REQ-014 @SCN-024
  Scenario: A lowercase plate with spaces is stored normalized
    Given no entry with status "inside" exists for the license plate "AB123CD"
    And the administrator types the license plate "ab 123 cd" and selects the vehicle type "Car"
    When the administrator saves the entry
    Then the entry is stored with the license plate "AB123CD"
