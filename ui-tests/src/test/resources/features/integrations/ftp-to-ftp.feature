@integrations-ftp-to-ftp
Feature: Integration - FTP to FTP

  Background: Clean application state
    Given clean application state
    Given "Camilla" logs into the Syndesis
    Given clean FTP server
    Given deploy FTP server
    Given created connections
      | FTP | FTP | FTP | FTP on OpenShift |
#
#  1. download - upload
#
  @ftp-download-ftp-upload
  Scenario: Create

#     -- TBD. for the time being, no need for adding file to FTP server:
#    Then puts "1MB.zip" file in the FTP 'from' "directoryName"

    When "Camilla" navigates to the "Home" page
    And clicks on the "Create Integration" button to create a new integration.
    Then she is presented with a visual integration editor
    And she is prompted to select a "Start" connection from a list of available connections

      # select salesforce connection as 'from' point
    When Camilla selects the "FTP" connection
    And she selects "download" integration action
    And she fills in values
      | File name expression               | test.txt |
      | FTP directory                      | download |
      | Milliseconds before polling starts | 1000     |
      | Milliseconds before the next poll  | 500      |
      | Delete file after download         | Yes      |

    And clicks on the "Next" button
#    And she fills in values
#      | Select Type           | JSON Schema        |
#      | Definition            | sample text        |
#      | Data Type Name        | sample name        |
#      | Data Type Description | sample description |
    And clicks on the "Done" button
#
    Then Camilla is presented with the Syndesis page "Choose a Finish Connection"
    When Camilla selects the "FTP" connection
    And she selects "Upload" integration action
    And she fills in values
      | File name expression                | test.txt         |
      | FTP directory                       | upload           |
      | If file exists                      | Override         |
      | Temporary file prefix while copying | copyingprefix    |
      | Temporary file name while copying   | copying_test_out |

    And clicks on the "Next" button

#    And she fills in values
#      | Select Type           | JSON Schema        |
#      | Definition            | sample text        |
#      | Data Type Name        | sample name        |
#      | Data Type Description | sample description |
    And clicks on the "Done" button

    Then Camilla is presented with the Syndesis page "Add to Integration"
    And clicks on the "Publish" button
    And she sets the integration name "ftp-to-ftp E2E"
    And clicks on the "Publish" button
    Then Camilla is presented with "ftp-to-ftp E2E" integration details
    Then "Camilla" navigates to the "Integrations" page
    Then she waits until integration "ftp-to-ftp E2E" gets into "Published" state

#    to be done:
    Then validate that file "test.txt" has been transfered from "/download" to "/upload" directory
