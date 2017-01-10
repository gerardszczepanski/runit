package szczepanski.gerard.runnit.view.controller;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.service.service.SearchService;
import szczepanski.gerard.runnit.view.component.Dimension;
import szczepanski.gerard.runnit.view.component.Position;
import szczepanski.gerard.runnit.view.component.SearchAutocompleteSelect;
import szczepanski.gerard.runnit.view.scene.factory.SettingsStagePresenter;
import szczepanski.gerard.runnit.view.util.ProgramTrayManager;

@RequiredArgsConstructor
public class MainSceneController {
	private static final Logger LOG = Logger.getLogger(MainSceneController.class);
	
	private final SearchService searchService;
	private final SettingsStagePresenter settingsStagePresenter;
	private SearchAutocompleteSelect searchAutocompleteSelect;
	
	@FXML
	private Button closeButton;
	
	@FXML
	public void initialize() {
		Pane currentPane = (Pane) closeButton.getParent();
		Dimension dim = new Dimension(330, 25);
		Position pos = new Position(35, 25);
		searchAutocompleteSelect = SearchAutocompleteSelect.createSearchAutocompleteSelect(currentPane, dim, pos);
		searchAutocompleteSelect.setSearchService(searchService);
	}
	
	@FXML
	public void handleCloseButton() {
		LOG.debug("handleCloseButton");
		ProgramTrayManager.hideProgramStage();
	}
	
	@FXML
	public void handleShowSettingsButton() {
		LOG.debug("handleShowSettingsButton");
		settingsStagePresenter.showSettingsStageInNewWindow();
	}
	
}
