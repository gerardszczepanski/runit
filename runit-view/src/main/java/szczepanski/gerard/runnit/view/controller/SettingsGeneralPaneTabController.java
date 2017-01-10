package szczepanski.gerard.runnit.view.controller;

import java.util.Optional;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;
import szczepanski.gerard.runnit.view.util.DialogDisplayer;

@RequiredArgsConstructor
public class SettingsGeneralPaneTabController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(SettingsGeneralPaneTabController.class);

	private final SettingsLoader settingsLoader;
	private final SettingsWriter settingsWriter;
	private final Validator<String> rootPathValidator;
	private final Validator<String> fileExtensionValidator;

	@FXML
	private ListView<String> rootPathsListView;

	@FXML
	private ListView<String> fileExtensonsListView;

	@FXML
	public void initialize() {
		LOG.debug("Initialize");
		Settings settings = settingsLoader.loadSettings();
		updateRootPaths(settings);
		updateFileExtensions(settings);
	}

	private void updateRootPaths(Settings settings) {
		rootPathsListView.getItems().clear();
		rootPathsListView.getItems().addAll(settings.getRootDirectioresToScan());
	}

	private void updateFileExtensions(Settings settings) {
		fileExtensonsListView.getItems().clear();
		fileExtensonsListView.getItems().addAll(settings.getFileExtensions());
	}

	@FXML
	public void handleAddRootPath() {
		LOG.debug("handleAddRootPath");
		Optional<String> optRootPath = DialogDisplayer.showInputDialog(getStage(rootPathsListView), "Root path");
		if (optRootPath.isPresent()) {
			String addedRootPath = optRootPath.get();
			addRootPath(addedRootPath);
		}
	}

	private void addRootPath(String rootPath) {
		try {
			rootPathValidator.validate(rootPath);
			addNotDuplicatedToListView(rootPathsListView, rootPath);
		} catch (RunitValidationException e) {
			handleValidationException(e);
		}
	}

	@FXML
	public void handleRemoveRootPath() {
		LOG.debug("handleRemoveRootPath");
		removeSelectedItemFromListView(rootPathsListView);
	}

	@FXML
	public void handleAddFileExtension() {
		LOG.debug("handleAddFileExtension");
		Optional<String> optFileExtension = DialogDisplayer.showInputDialog(getStage(rootPathsListView),
				"File extension");
		if (optFileExtension.isPresent()) {
			String addedFileExtension = optFileExtension.get().toLowerCase();
			addFileExtension(addedFileExtension);
		}
	}

	private void addFileExtension(String fileExtension) {
		try {
			fileExtensionValidator.validate(fileExtension);
			addNotDuplicatedToListView(fileExtensonsListView, fileExtension);
		} catch (RunitValidationException e) {
			handleValidationException(e);
		}
	}

	private void removeSelectedItemFromListView(ListView<String> listView) {
		int selectedIndex = listView.getSelectionModel().getSelectedIndex();
		listView.getItems().remove(selectedIndex);
	}

	private void addNotDuplicatedToListView(ListView<String> listView, String object) {
		if (!listView.getItems().contains(object)) {
			listView.getItems().add(object);
		} else {
			DialogDisplayer.showValidationMessageDialog("List can not contains duplicates!");
		}
	}

	@FXML
	public void handleRemoveFileExtension() {
		LOG.debug("handleRemoveFileExtension");
		removeSelectedItemFromListView(fileExtensonsListView);
	}
	
	@FXML
	public void handleSaveGeneralTab() {
		LOG.debug("handleSaveGeneralTab");
		
		Settings actualSettings = settingsLoader.loadSettings();
		Settings newSettings = createNewSettings(actualSettings);
		settingsWriter.updateSettings(newSettings);
		
		DialogDisplayer.showConfirmationMessageDialog("Settings are successfully saved!");
		handleCancelButton();
	}
	
	private Settings createNewSettings(Settings actualSettings) {
		return Settings.builder()
				.rootDirectioresToScan(rootPathsListView.getItems())
				.fileExtensions(fileExtensonsListView.getItems())
				.webAliases(actualSettings.getWebAliases())
				.dirAliases(actualSettings.getDirAliases())
				.build();
	}

	@FXML
	public void handleCancelButton() {
		LOG.debug("handleCancelButton");
		Stage settingsStage = getStage(rootPathsListView);
		settingsStage.close();
	}

}
