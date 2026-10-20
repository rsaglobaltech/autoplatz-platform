Feature: Entry date/time pre-filled

  @REQ-013 @SCN-023
  Scenario: Entry date/time defaults to now
    Given the system clock reads 2026-10-20 09:30
    When the administrator opens the entry form
    Then the entry date/time field is pre-filled with 2026-10-20 09:30
