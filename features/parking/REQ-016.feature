Feature: One open entry per plate

  @REQ-016 @SCN-027
  Scenario: A vehicle still inside cannot enter again
    Given an entry for the license plate "AB123CD" with status "inside" exists
    When the administrator registers a new entry for the license plate "AB123CD" with the vehicle type "Car"
    Then the entry is rejected with the message "Vehicle is already in the garage"
    And exactly one entry for the license plate "AB123CD" with status "inside" exists
