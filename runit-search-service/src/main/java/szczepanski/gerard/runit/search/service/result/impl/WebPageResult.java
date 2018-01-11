package szczepanski.gerard.runit.search.service.result.impl;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.util.WebPageRunner;
import szczepanski.gerard.runit.settings.service.loader.Alias;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class WebPageResult extends SearchResult {

    private static final String WEB_PAGE_RESULT_DEFAULT_IMAGE_PATH = "/images/search-results/webPageResult.png";
    private static final Image WEB_PAGE_RESULT_IMAGE = new Image(WebPageResult.class.getResourceAsStream(WEB_PAGE_RESULT_DEFAULT_IMAGE_PATH));

    private final URI webAddress;

    private WebPageResult(Alias webAlias) {
        super(new SearchResultRepresentation(WEB_PAGE_RESULT_IMAGE, webAlias.getName()));
        this.webAddress = createUriFromString(webAlias.getValue());
    }

    public static WebPageResult fromWebAlias(Alias webAlias) {
        isWebAliasValid(webAlias);
        return new WebPageResult(webAlias);
    }

    private static void isWebAliasValid(Alias alias) {
        if (alias == null || alias.getName() == null || alias.getValue() == null) {
            throw new RunitBusinessException(ExceptionCode.B_001, new Object[]{alias.getName(), alias.getValue()});
        }
    }

    private URI createUriFromString(String fullWebAddress) {
        try {
            return new URI(fullWebAddress);
        } catch (URISyntaxException e) {
            throw new RunitBusinessException(ExceptionCode.B_002, new Object[]{webAddress});
        }
    }

    @Override
    public void run() {
        WebPageRunner.browsePage(webAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WebPageResult that = (WebPageResult) o;
        return Objects.equals(webAddress, that.webAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), webAddress);
    }
}
