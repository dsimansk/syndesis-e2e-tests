package io.syndesis.qe.steps.customizations.connectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;

import static java.util.Arrays.asList;

import io.fabric8.openshift.api.model.Route;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import io.syndesis.qe.pages.customizations.connectors.ApiClientConnectors;
import io.syndesis.qe.pages.customizations.connectors.wizard.steps.ReviewActions;
import io.syndesis.qe.steps.CommonSteps;
import io.syndesis.qe.steps.customizations.connectors.wizard.WizardSteps;
import io.syndesis.qe.utils.OpenShiftUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiClientConnectorsSteps {

    private static ApiClientConnectors apiClientConnectorsPage = new ApiClientConnectors();
    private static ReviewActions reviewActions = new ReviewActions();

    @Then("^(\\w+) opens new Api Connector wizard$")
    public void openNewApiConnectorWizard(String user) {
        apiClientConnectorsPage.startWizard();
    }

    @Then("^(\\w+) opens the API connector \"([^\"]*)\" detail$")
    public void openApiConnectorDetail(String userName, String connectorName) {
        apiClientConnectorsPage.clickConnectorByTitle(connectorName);
    }

    @Then("(\\w+) is presented with the new connector \"([^\"]*)\"$")
    public void checkNewConnectorIsPresent(String user, String connectorName) throws Throwable {
        Assert.assertTrue("Connector [" + connectorName + "] is not listed.", apiClientConnectorsPage.isConnectorPresent(connectorName));
    }

    @Then("^(\\w+) is presented with a connectors list of size (\\d+)$")
    public void checkConnectorsListSize(String userName, String listSize) {
        Assert.assertTrue("The connectors list should be of size <" + listSize + ">.", apiClientConnectorsPage.isConnectorsListLongAs(Integer.parseInt(listSize)));
    }

    @Then("^checks? the error box (is|is not) present$")
    public void checkErrorBoxPresent(String present) {
        if (present.equals("is")) {
            log.info("checking if ERRORBOX is visible");
            Assert.assertTrue("The validation error box should exist", reviewActions.getValidationErrorBox().is(visible));
        } else {
            log.info("checking if ERRORBOX is not visible");
            Assert.assertTrue("The validation error box should not exist", reviewActions.getValidationErrorBox().is(not(visible)));
        }
    }

    //***************************************************************************
    //******************************* bulk steps ********************************
    //***************************************************************************

    @Then("^uploads? swagger file (.+)$")
    public void uploadSwaggerFile(String filePath) {
       uploadSwagger(new ArrayList<List<String>>(Arrays.asList(Arrays.asList("file", filePath))));
    }

    private void uploadSwagger(List<List<String>> sourceDataTable) {
        CommonSteps commonSteps = new CommonSteps();
        commonSteps.navigateTo("","Customizations");
        commonSteps.validatePage("","Customizations");
        commonSteps.clickOnLink("API Client Connectors");
        commonSteps.validatePage("", "API Client Connectors");

        openNewApiConnectorWizard("");

        WizardSteps wizardSteps = new WizardSteps();

        log.info("Setting up upload type and path of the swagger file");
        wizardSteps.uploadSwaggerFile("", DataTable.create(sourceDataTable));
    }

    @Then("^(\\w+) creates? new API connector$")
    public void createNewApiConnector(String user, DataTable properties) throws Throwable {

        log.info("Connector wizard started");

        /**
         * Upload Swagger Specification wizard phase
         **/
        //| source | file | <path> |
        //| source | url | <path> |
        List<List<String>> sourceDataTable = null;

        /**
         * Specify Security wizard phase
        **/
        //| security | authType | <authType> {HTTP Basic Authentication, OAuth 2.0, ...} |
        //| security | authorizationUrl | <authorizationUrl> |
        //| security | accessTokenUrl | <accessTokenUrl> |
        List<List<String>> securityDataTable = new ArrayList<>();

        /**
         * Review/Edit Connector Details wizard phase
         **/
        //| details | connectorName | <connectorName> |
        String connectorName = null;

        //| details | description | <description> |
        String description = null;

        //| details | hostname | <hostName> |
        //| details | routeHost | <routeName> |
        String host = null;

        //| details | baseUrl | <baseUrl> |
        String baseUrl = null;

        for (List<String> property : properties.raw()) {
            switch (property.get(0)) {
                case "source":
                    switch (property.get(1)) {
                        //| source | file | <path> |
                        case "file":
                        //| source | url | <path> |
                        case "url":
                            log.info("Setting up upload type and path properties of the swagger file");
                            List<String> row = asList(property.get(1), property.get(2));
                            sourceDataTable = new ArrayList<>();
                            sourceDataTable.add(row);
                            break;
                        default:
                    }
                    break;

                case "security":
                    switch (property.get(1)) {
                        //| security | authType | <authType> {HTTP Basic Authentication, OAuth 2.0, ...} |
                        case "authType":
                        //| security | authorizationUrl | <authorizationUrl> |
                        case "authorizationUrl":
                        //| security | accessTokenUrl | <accessTokenUrl> |
                        case "accessTokenUrl":
                            log.info("Setting up access token url property");
                            List<String> row = asList(property.get(1), property.get(2));
                            securityDataTable.add(row);
                            break;
                        default:

                    }
                    break;

                case "details":
                    switch(property.get(1)) {
                        //| details | connectorName | <connectorName> |
                        case "connectorName":
                            log.info("Setting up connector name property");
                            connectorName = property.get(2);
                            break;
                        //| details | description | <description> |
                        case "description":
                            log.info("Setting up description property");
                            description = property.get(2);
                            break;
                        //| details | hostname | <hostName> |
                        case "host":
                            log.info("Setting up hostname property manually");
                            host = property.get(2);
                            break;
                        //| details | routeHost | <routeName> |
                        case "routeHost":
                            log.info("Setting up hostname of the used route property");
                            Route route = OpenShiftUtils.getInstance().getRoute(property.get(2));
                            route.getSpec().getTls().setInsecureEdgeTerminationPolicy("Allow");
                            OpenShiftUtils.client().routes().createOrReplace(route);
                            host = "http://" + route.getSpec().getHost();
                        case "baseUrl":
                            baseUrl = property.get(2);
                    }

                default:
            }
        }

        log.info("Validating \"Customizations\" and \"Client Api Connectors\" pages");

        WizardSteps wizardSteps = new WizardSteps();
        if(sourceDataTable==null) {
            Assert.fail("Swagger upload type and path not set");
        }

        uploadSwagger(sourceDataTable);

        wizardSteps.navigateToNextWizardStep(user, "Review Actions");
        wizardSteps.navigateToNextWizardStep(user, "SpecifySecurity");

        if(connectorName != null) {
            log.info("Setting up auth type");
            wizardSteps.setUpSecurity(user, DataTable.create(securityDataTable));
            wizardSteps.navigateToNextWizardStep(user, "Review/Edit Connector Details");
        }

        if(connectorName != null) {
            log.info("Setting up connector name");
            wizardSteps.setUpConnectorName(user, connectorName);
        }

        if(description != null) {
            log.info("Setting up connector name");
            wizardSteps.setUpDescription(user, description);
        }

        if(host != null) {
            log.info("Setting up host");
            wizardSteps.setUpHost(user, host);
        }

        if(baseUrl != null) {
            log.info("Setting up base url");
            wizardSteps.setUpBaseUrl(user, baseUrl);
        }

        wizardSteps.finishNewConnectorWizard(user);
        checkNewConnectorIsPresent(user, connectorName);
    }
}
