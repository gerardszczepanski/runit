package szczepanski.gerard.runit.service.search.loader;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class WebAliasPropertySpliteratorTest {

	WebAliasPropertySpliterator spliterator;

	@BeforeTest
	public void beforeTest() {
		spliterator = new WebAliasPropertySpliterator();
	}

	@Test
	public void testSuccess() {
		// Arrange
		String stringFromProperties = "google=http://google.com;starwars=http://starwars.com";

		// Act
		List<WebAlias> webAliases = spliterator.fromPropertyString(stringFromProperties);

		// Assert
		Assert.assertEquals(webAliases.get(0).getAlias(), "google");
		Assert.assertEquals(webAliases.get(0).getFullAddress(), "http://google.com");
		Assert.assertEquals(webAliases.get(1).getAlias(), "starwars");
		Assert.assertEquals(webAliases.get(1).getFullAddress(), "http://starwars.com");
	}

}
