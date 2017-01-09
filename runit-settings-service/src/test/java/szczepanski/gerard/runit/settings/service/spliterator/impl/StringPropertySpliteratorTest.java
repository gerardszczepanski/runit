package szczepanski.gerard.runit.settings.service.spliterator.impl;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import szczepanski.gerard.runit.settings.service.spliterator.impl.StringPropertySpliterator;

public class StringPropertySpliteratorTest {

	StringPropertySpliterator spliterator;

	@BeforeTest
	public void beforeTest() {
		spliterator = new StringPropertySpliterator();
	}

	@Test
	public void testSuccess() {
		// Arrange
		String stringFromProperties = ".exe;.zip";

		// Act
		List<String> extensions = spliterator.fromPropertyString(stringFromProperties);

		// Assert
		Assert.assertEquals(extensions.get(0), ".exe");
		Assert.assertEquals(extensions.get(1), ".zip");
	}

}