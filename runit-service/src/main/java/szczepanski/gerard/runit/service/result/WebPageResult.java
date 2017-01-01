package szczepanski.gerard.runit.service.result;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.search.loader.WebAlias;

public class WebPageResult implements SearchResult {

	private final URI webAddress;
	private final SearchResultRepresentation searchResultRepresentation;
	
	public static WebPageResult fromWebAlias(WebAlias webAlias) {
		isWebAliasValid(webAlias);
		return new WebPageResult(webAlias);
	}
	
	private static void isWebAliasValid(WebAlias webAlias) {
		if (webAlias == null || webAlias.getAlias() == null || webAlias.getFullAddress() == null) {
			throw new RunitRuntimeException("SearchResult creation failure. Input File is null");
		}
	}
	
	private WebPageResult(WebAlias webAlias) {
		this.webAddress = createUriFromString(webAlias.getFullAddress());
		this.searchResultRepresentation = new SearchResultRepresentation(null, webAlias.getAlias());
	}
	
	private URI createUriFromString(String fullWebAddress) {
		try {
			return new URI(fullWebAddress);
		} catch (URISyntaxException e) {
			throw new RunitRuntimeException("URI can not be created from Web Address: " + webAddress);
		}
	}
	
	@Override
	public void run() {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.browse(webAddress);
		} catch (IOException e) {
			throw new RunitRuntimeException("URI can not be browsed.");
		}
	}

	@Override
	public SearchResultRepresentation getSearchResultRepresentation() {
		return searchResultRepresentation;
	}
	
}
