package szczepanski.gerard.runit.settings.service.writer.impl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.validator.impl.FileExtensionValidator;

public class FileExtensionValidatorTest {

	Validator<String> validator;

	@BeforeTest
	public void beforeTest() {
		validator = new FileExtensionValidator();
	}
	
	@Test(dataProvider = "validFileExtensionsProvider")
	public void validateValidFileExtensionsSuccess(String fileExtension) throws RunitValidationException {
		// Act
		validator.validate(fileExtension);
	}
	
	@Test(expectedExceptions = RunitValidationException.class, dataProvider = "notValidFileExtensionsProvider")
	public void validateNotValidFileExtensionsThrowsException(String rootPath) throws RunitValidationException {
		// Act
		validator.validate(rootPath);
	}
	
	 @DataProvider(name = "validFileExtensionsProvider")
	 public Object[][] validFileExtensionsProvider() {
	        return new Object[][] { 
	        	{ "exe" }, 
	        	{ "txt" },
	        	{ "jar" },
	        	{ "csv" },
	        	{ "doc" },
	        	{ "html" }
	        };
	    }
	 
	 @DataProvider(name = "notValidFileExtensionsProvider")
	 public Object[][] notValidFileExtensionsProvider() {
	        return new Object[][] { 
	        	{ ".exe" }, 
	        	{ "..txt" },
	        	{ "jar." },
	        	{ "csv4" },
	        	{ "666" },
	        	{ "//dd" },
	        	{ "" },
	        };
	    }


}
