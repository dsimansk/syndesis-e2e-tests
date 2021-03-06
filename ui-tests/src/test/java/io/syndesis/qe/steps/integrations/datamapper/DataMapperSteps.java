package io.syndesis.qe.steps.integrations.datamapper;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.greaterThan;

import static com.codeborne.selenide.Condition.visible;

import com.codeborne.selenide.SelenideElement;


import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.syndesis.qe.pages.integrations.editor.add.steps.DataMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by sveres on 11/15/17.
 */
@Slf4j
public class DataMapperSteps {

    private DataMapper mapper = new DataMapper();

    @When("^she creates mapping from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void createMapping(String source, String target) {
        mapper.createMapping(source, target);
    }

    @When("^.*creates? mappings from following table$")
    public void createMapping(DataTable stepPositions) {
        for (List<String> row : stepPositions.cells(0)) {
            mapper.createMapping(row.get(0), row.get(1));
        }
    }

    @Then("^she is presented with data mapper ui$")
    public void dataMapperUIpresent() {
        log.info("data mapper ui must load and show fields count");
        assertThat(mapper.fieldsCount(), greaterThan(0));
    }

    @When("^she selects \"([^\"]*)\" from \"([^\"]*)\" selector-dropdown$")
    public void selectFromDropDownByElement(String option, String selectAlias) {
        log.info(option);
        SelenideElement selectElement = mapper.getElementByAlias(selectAlias).shouldBe(visible);
        mapper.selectOption(selectElement, option);
    }

    @Then("^she fills \"([^\"]*)\" selector-input with \"([^\"]*)\" value$")
    public void fillActionConfigureField(String selectorAlias, String value) {
        SelenideElement inputElement = mapper.getElementByAlias(selectorAlias).shouldBe(visible);
        mapper.fillInput(inputElement, value);
    }

    /**
     * @param first     parameter to be combined.
     * @param first_pos position of the first parameter in the final string
     * @param second    parameter to be combined.
     * @param sec_pos   position of the second parameter in the final string.
     * @param combined  above two into this parameter.
     * @param separator used to estethically join first and second parameter.
     */
    // And she combines "FirstName" as "2" with "LastName" as "1" to "first_and_last_name" using "Space" separator
    @Then("^she combines \"([^\"]*)\" as \"([^\"]*)\" with \"([^\"]*)\" as \"([^\"]*)\" to \"([^\"]*)\" using \"([^\"]*)\" separator$")
    public void combinePresentFielsWithAnother(String first, String first_pos,
                                               String second, String sec_pos, String combined, String separator) {
        SelenideElement inputElement;
        SelenideElement selectElement;

        // Then she fills "FirstCombine" selector-input with "FirstName" value
        inputElement = mapper.getElementByAlias("FirstSource").shouldBe(visible);
        mapper.fillInput(inputElement, first);

        // And she selects "Combine" from "ActionSelect" selector-dropdown
        selectElement = mapper.getElementByAlias("ActionSelect").shouldBe(visible);
        mapper.selectOption(selectElement, "Combine");

        // And she selects "Space" from "SeparatorSelect" selector-dropdown
        selectElement = mapper.getElementByAlias("SeparatorSelect").shouldBe(visible);
        mapper.selectOption(selectElement, separator);

        // And clicks on the "Add Source" link
        mapper.getButton("Add Source").shouldBe(visible).click();

        // Then she fills "SecondCombine" selector-input with "LastName" value
        inputElement = mapper.getElementByAlias("SecondSource").shouldBe(visible);
        mapper.fillInputAndConfirm(inputElement, second);

        // And she fills "FirstCombinePosition" selector-input with "2" value
        inputElement = mapper.getElementByAlias("FirstSourcePosition").shouldBe(visible);
        mapper.fillInput(inputElement, first_pos);

        // And she fills "SecondCombinePosition" selector-input with "1" value
        inputElement = mapper.getElementByAlias("SecondSourcePosition").shouldBe(visible);
        mapper.fillInput(inputElement, sec_pos);

        // Then she fills "TargetCombine" selector-input with "first_and_last_name" value
//        inputElement = mapper.getElementByAlias("FirstTarget").shouldBe(visible);
//        mapper.fillInputAndConfirm(inputElement, combined);
    }

    //    And she separates "FirstName" into "company" as "2" and "email" as "1" using "Comma" separator
    @Then("^she separates \"([^\"]*)\" into \"([^\"]*)\" as \"([^\"]*)\" and \"([^\"]*)\" as \"([^\"]*)\" using \"([^\"]*)\" separator$")
    public void separatePresentFielsIntoTwo(String input, String output1, String first_pos, String output2, String second_pos, String separator) {
        SelenideElement inputElement;
        SelenideElement selectElement;

        //inputElement = mapper.getElementByAlias("FirstSource").shouldBe(visible);
        //mapper.fillInput(inputElement, input);

        selectElement = mapper.getElementByAlias("ActionSelect").shouldBe(visible);
        mapper.selectOption(selectElement, "Separate");

        selectElement = mapper.getElementByAlias("SeparatorSelect").shouldBe(visible);
        mapper.selectOption(selectElement, separator);

        // NOTE: THIS STEP SHOULD HAVE BEEN DONE AUTOMATICALLY BY SELECTING "Separate" action
        mapper.getButton("Add Target").shouldBe(visible).click();

        inputElement = mapper.getElementByAlias("FirstTarget").shouldBe(visible);
        mapper.fillInputAndConfirm(inputElement, output1);

        inputElement = mapper.getElementByAlias("FirstTargetPosition").shouldBe(visible);
        mapper.fillInput(inputElement, first_pos);

        inputElement = mapper.getElementByAlias("SecondTarget").shouldBe(visible);
        mapper.fillInputAndConfirm(inputElement, output2);

        inputElement = mapper.getElementByAlias("SecondTargetPosition").shouldBe(visible);
        mapper.fillInput(inputElement, second_pos);

    }

}
