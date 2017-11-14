package io.syndesis.qe.rest;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import io.syndesis.qe.rest.tests.integrations.TwitterSalesforceTest;
import io.syndesis.qe.templates.SyndesisTemplate;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TwitterSalesforceTest.class
})
public class RestTestSuite {



}
