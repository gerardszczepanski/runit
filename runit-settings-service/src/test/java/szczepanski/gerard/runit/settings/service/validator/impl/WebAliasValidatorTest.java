package szczepanski.gerard.runit.settings.service.validator.impl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.validator.Validator;

public class WebAliasValidatorTest {
	
	Validator<Alias> validator;
	
	@BeforeTest
	public void beforetets() {
		validator = new WebAliasValidator();
	}
	
	@Test(dataProvider = "validWebAliasesProvider")
	public void validateValidWebAliasesSuccess(Alias webAlias) throws RunitValidationException {
		// Act
		validator.validate(webAlias);
	}
	
	@Test(expectedExceptions = RunitValidationException.class, dataProvider = "notValidWebAliasesProvider")
	public void validateNotValidWebAliasesThrowsException(Alias webAlias) throws RunitValidationException {
		// Act
		validator.validate(webAlias);
	}
	
	 @DataProvider(name = "validWebAliasesProvider")
	 public Object[][] validWebAliasesProvider() {
	        return new Object[][] { 
	        	{ new Alias("google", "http://google.com") }, 
	        	{ new Alias("google chrome", "https://google.com") }
	        };
	    }
	 
	 @DataProvider(name = "notValidWebAliasesProvider")
	 public Object[][] notValidWebAliasesProvider() {
	        return new Object[][] { 
	        	{ new Alias("", "http://google.com") }, 
	        	{ new Alias("notValid", "www.google.com") },
	        	{ new Alias("notValid", "helloWorld") },
	        	{ new Alias("notValidEnough", "world.com") }
	        };
	    }
	
}
