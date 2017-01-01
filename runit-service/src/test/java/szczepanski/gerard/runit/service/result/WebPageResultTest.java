package szczepanski.gerard.runit.service.result;

import org.testng.annotations.Test;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.search.loader.WebAlias;

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
		WebAlias webAlias = new WebAlias(alias, fullWebAddress);
		
		WebPageResult webPageResult = WebPageResult.fromWebAlias(webAlias);
		
		// Act
		webPageResult.run();
	}
	
	@Test(expectedExceptions = RunitRuntimeException.class)
	public void ifWebAddressIfInvalidThrowException() {
		// Arrange
		String alias = "google";
		String invalidWebAddress = null;
		WebAlias webAlias = new WebAlias(alias, invalidWebAddress);
		
		WebPageResult.fromWebAlias(webAlias);
	}

}
