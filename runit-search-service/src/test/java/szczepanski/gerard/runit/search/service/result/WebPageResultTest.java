package szczepanski.gerard.runit.search.service.result;

import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.search.service.result.impl.WebPageResult;
import szczepanski.gerard.runit.settings.service.loader.Alias;

public class WebPageResultTest {

	/**
	 * Trigger browser open. Enable this test only for run in isolation.
	 * Disable for maven build cycle.
	 */
	@Test(enabled = false)
	public void openPageInBrowserSuccess() {
		// Arrange
		String alias = "google";
		String fullWebAddress = "http://google.com";
		Alias webAlias = new Alias(alias, fullWebAddress);
		
		WebPageResult webPageResult = WebPageResult.fromWebAlias(webAlias);
		
		// Act
		webPageResult.run();
	}
	
	@Test(expectedExceptions = RunitRuntimeException.class)
	public void ifWebAddressIfInvalidThrowException() {
		// Arrange
		String alias = "google";
		String invalidWebAddress = null;
		Alias webAlias = new Alias(alias, invalidWebAddress);
		
		WebPageResult.fromWebAlias(webAlias);
	}

}
