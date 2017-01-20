package szczepanski.gerard.runit.settings.service.validator.impl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.validator.impl.DirectoryPathValidator;

public class DirectoryPathValidatorTest {

	Validator<String> validator;

	@BeforeTest
	public void beforeTest() {
		validator = new DirectoryPathValidator();
	}

	@Test(dataProvider = "validRootPathProvider")
	public void validateValidRootPathSuccess(String rootPath) throws RunitValidationException {
		// Act
		validator.validate(rootPath);
	}
	
	@Test(expectedExceptions = RunitValidationException.class, dataProvider = "notValidRootPathProvider")
	public void validateNotValidRootPathThrowsException(String rootPath) throws RunitValidationException {
		// Act
		validator.validate(rootPath);
	}
	
	/**
	 * This test only works on machine with real direcotries paths  
	 */
	 @DataProvider(name = "validRootPathProvider")
	 public Object[][] validRootPathProvider() {
	        return new Object[][] { 
	        	{ "C:\\Users" }, 
	        	{ "C:\\Program Files" }
	        };
	    }
	 
	 @DataProvider(name = "notValidRootPathProvider")
	 public Object[][] notValidRootPathProvider() {
	        return new Object[][] { 
	        	{ "NotValidPath" }, 
	        	{ "C:\\ProgramFiles//wrong//path.definetely" },
	        	{ "" }
	        };
	    }

}
