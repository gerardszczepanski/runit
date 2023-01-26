package szczepanski.gerard.runit.settings.spliterator.impl;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import szczepanski.gerard.runit.settings.loader.Alias;

import java.util.List;

import static org.testng.Assert.*;

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
        assertEquals(webAliases.get(0).getName(), "google");
        assertEquals(webAliases.get(0).getValue(), "http://google.com");
        assertEquals(webAliases.get(1).getName(), "starwars");
        assertEquals(webAliases.get(1).getValue(), "http://starwars.com");
    }

}
