package szczepanski.gerard.runit.search.service.result.impl;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.search.service.result.SearchResult;
import szczepanski.gerard.runit.search.service.result.SearchResultRepresentation;
import szczepanski.gerard.runit.search.service.util.WebPageRunner;
import szczepanski.gerard.runit.settings.service.loader.Alias;

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
			throw new RunitBusinessException(ExceptionCode.B_001, new Object[] { alias.getName(), alias.getValue() });
		}
	}

	private URI createUriFromString(String fullWebAddress) {
		try {
			return new URI(fullWebAddress);
		} catch (URISyntaxException e) {
			throw new RunitBusinessException(ExceptionCode.B_002, new Object[] { webAddress });
		}
	}

	@Override
	public void run() {
		WebPageRunner.browsePage(webAddress);
	}

	@Override
	public int hashCode() {
		final int prime = 47;
		int result = super.hashCode();
		result = prime * result + ((webAddress == null) ? 0 : webAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebPageResult other = (WebPageResult) obj;
		if (webAddress == null) {
			if (other.webAddress != null)
				return false;
		} else if (!webAddress.equals(other.webAddress))
			return false;
		return true;
	}

}
