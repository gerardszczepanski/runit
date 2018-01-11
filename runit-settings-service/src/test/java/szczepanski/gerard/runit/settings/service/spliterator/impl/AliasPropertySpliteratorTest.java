package szczepanski.gerard.runit.settings.service.spliterator.impl;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import szczepanski.gerard.runit.settings.service.loader.Alias;

import java.util.List;

public class AliasPropertySpliteratorTest {

    AliasPropertySpliterator spliterator;

    @BeforeTest
    public void beforeTest() {
        spliterator = new AliasPropertySpliterator();
    }

    @Test
    public void testSuccess() {
        // Arrange
        String stringFromProperties = "google=http://google.com;starwars=http://starwars.com";

        // Act
        List<Alias> webAliases = spliterator.fromPropertyString(stringFromProperties);

        // Assert
        Assert.assertEquals(webAliases.get(0).getName(), "google");
        Assert.assertEquals(webAliases.get(0).getValue(), "http://google.com");
        Assert.assertEquals(webAliases.get(1).getName(), "starwars");
        Assert.assertEquals(webAliases.get(1).getValue(), "http://starwars.com");
    }

}
