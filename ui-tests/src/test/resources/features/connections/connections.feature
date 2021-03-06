@smoke
Feature: Connection - CRUD
# Enter feature description here

  Background:
    Given clean application state
    Given "Camilla" logs into the Syndesis


  @validate-connection-credentials
  Scenario: Credentials
    And validate credentials

  @connection-create-delete-test
  Scenario: Create & delete
    And "Camilla" navigates to the "Connections" page
    And click on the "Create Connection" button
    And Camilla selects "Twitter" connection type
    Then she is presented with the "Validate" button

    When she fills "Twitter Listener" connection details
    Then click on the "Validate" button
    Then she can see "Twitter has been successfully validated" in alert-success notification

    Then scroll "top" "right"
    And click on the "Next" button

    And she checks "Create" button is "Disabled"

    And type "my sample tw conn" into connection name
    And type "this connection is awesome" into connection description
    And click on the "Create" button
    Then Camilla is presented with the Syndesis page "Connections"

    When opens the "my sample tw conn" connection detail
    Then Camilla is presented with "my sample tw conn" connection details

    When "Camilla" navigates to the "Connections" page
    Then Camilla is presented with the Syndesis page "Connections"

    When Camilla deletes the "my sample tw conn" connection
  # delete was not fast enough some times so sleep is necessary
  # Then she stays there for "2000" ms
    Then Camilla can not see the "my sample tw conn" connection anymore

  @connection-kebab-menu-test
  Scenario: Kebab menu
    When "Camilla" navigates to the "Connections" page
  # is there any connection? If there are no default connections there is nothing
  #     so we have to add at least one connection first
    And click on the "Create Connection" button
    And Camilla selects "Twitter" connection type
    Then she is presented with the "Validate" button
    When she fills "Twitter Listener" connection details

  # no validation as its not necessary for this scenario

    Then scroll "top" "right"
    And click on the "Next" button
    And type "my sample tw conn" into connection name
    And type "this connection is awesome" into connection description
    And click on the "Create" button
    Then Camilla is presented with the Syndesis page "Connections"

  # now we know there is at least one connection
    And clicks on the kebab menu icon of each available connection
    Then she can see unveiled kebab menu of all connections, each of this menu consist of "View", "Edit" and "Delete" actions

  # garbage collection
    When "Camilla" navigates to the "Connections" page
    Then Camilla is presented with the Syndesis page "Connections"

    When Camilla deletes the "my sample tw conn" connection
  # Then she stays there for "2000" ms
    Then Camilla can not see the "my sample tw conn" connection anymore


  @connection-edit-view-test
  Scenario: Kebab menu edit & view
    When "Camilla" navigates to the "Connections" page
    And click on the "Create Connection" button
    And Camilla selects "Twitter" connection type
    Then she is presented with the "Validate" button

    When she fills "Twitter Listener" connection details
    Then click on the "Validate" button
    Then she can see "Twitter has been successfully validated" in alert-success notification

    Then scroll "top" "right"
    And click on the "Next" button
    And type "my sample tw conn" into connection name
    And type "this connection is awesome" into connection description
    And click on the "Create" button
    Then Camilla is presented with the Syndesis page "Connections"

    When clicks on the "Edit" kebab menu button of "my sample tw conn"
    Then Camilla is presented with "my sample tw conn" connection details

    When "Camilla" navigates to the "Connections" page
    Then Camilla is presented with the Syndesis page "Connections"

    When clicks on the "View" kebab menu button of "my sample tw conn"
    Then Camilla is presented with "my sample tw conn" connection details

    When "Camilla" navigates to the "Connections" page
    Then Camilla is presented with the Syndesis page "Connections"

    When Camilla deletes the "my sample tw conn" connection
  # delete was not fast enough some times so sleep is necessary
  # Then she stays there for "2000" ms
    Then Camilla can not see the "my sample tw conn" connection anymore