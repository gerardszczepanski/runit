package szczepanski.gerard.runit.service.result;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.scene.image.Image;
import szczepanski.gerard.runit.common.exception.ExceptionCode;
import szczepanski.gerard.runit.common.exception.RunitBusinessException;
import szczepanski.gerard.runit.common.exception.RunitRuntimeException;
import szczepanski.gerard.runit.service.search.loader.Alias;

public class WebPageResult extends SearchResult {

	private final URI webAddress;

	public static WebPageResult fromWebAlias(Alias webAlias) {
		isWebAliasValid(webAlias);
		return new WebPageResult(webAlias);
	}

	private static void isWebAliasValid(Alias alias) {
		if (alias == null || alias.getName() == null || alias.getValue() == null) {
			throw new RunitBusinessException(ExceptionCode.B_001, new Object[] { alias.getName(), alias.getValue() });
		}
	}

	private WebPageResult(Alias webAlias) {
		super(new SearchResultRepresentation(
				new Image(WebPageResult.class.getResourceAsStream("/images/search-results/webPageResult.png")),
				webAlias.getName()));
		this.webAddress = createUriFromString(webAlias.getValue());
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
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.browse(webAddress);
		} catch (IOException e) {
			throw new RunitRuntimeException(ExceptionCode.R_006, webAddress);
		}
	}

}
