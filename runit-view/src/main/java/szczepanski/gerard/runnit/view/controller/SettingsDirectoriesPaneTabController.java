package szczepanski.gerard.runnit.view.controller;

import java.util.Optional;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;
import szczepanski.gerard.runnit.view.util.DialogDisplayer;

@RequiredArgsConstructor
public class SettingsDirectoriesPaneTabController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(SettingsDirectoriesPaneTabController.class);

	private final SettingsLoader settingsLoader;
	private final SettingsWriter settingsWriter;
	private final Validator<Alias> directoryAliasValidator;

	@FXML
	private TableView<Alias> dirAliasTableView;

	@FXML
	private TableColumn<Alias, String> aliasColumn;

	@FXML
	private TableColumn<Alias, String> directoryPathColumn;

	@FXML
	public void initialize() {
		LOG.debug("Initialize");
		Settings settings = settingsLoader.getSettings();
		updateDirAliases(settings);
	}

	private void updateDirAliases(Settings settings) {
		configureTableView();
		dirAliasTableView.getItems().clear();
		dirAliasTableView.getItems().addAll(settings.getDirAliases());
	}

	@FXML
	public void handleAddDirAlias() {
		LOG.debug("handleAddDirAlias");
		Alias newDirAlias = getNewAliasFromUserInput();
		addDirAlias(newDirAlias);
	}
	
	private Alias getNewAliasFromUserInput() {
		Optional<String> optAliasName = DialogDisplayer.showInputDialog(getStage(dirAliasTableView), "Web Alias Name");
		Optional<String> optAliasValue = DialogDisplayer.showInputDialog(getStage(dirAliasTableView), "Directory path");
		return new Alias(optAliasName.get(), optAliasValue.get());
	}
	
	private void addDirAlias(Alias newDirAlias) {
		try {
			directoryAliasValidator.validate(newDirAlias);
			addNotDuplicatedDirAliasToTableView(newDirAlias);
		} catch (RunitValidationException e) {
			handleValidationException(e);
		}
	}
	
	private void addNotDuplicatedDirAliasToTableView(Alias newDirAlias) {
		if (!dirAliasTableView.getItems().contains(newDirAlias)) {
			dirAliasTableView.getItems().add(newDirAlias);
		} else {
			DialogDisplayer.showValidationMessageDialog("List can not contains duplicates!");
		}
	}

	@FXML
	public void handleRemoveDirAlias() {
		LOG.debug("handleRemoveDirAlias");
		int selectedIndex = dirAliasTableView.getSelectionModel().getSelectedIndex();
		dirAliasTableView.getItems().remove(selectedIndex);
	}

	@FXML
	public void handleSaveTab() {
		LOG.debug("handleSaveGeneralTab");
		
		Settings actualSettings = settingsLoader.getSettings();
		Settings newSettings = createNewSettings(actualSettings);
		settingsWriter.updateSettings(newSettings);
		
		DialogDisplayer.showConfirmationMessageDialog("Settings are successfully saved!");
		handleCancelButton();
	}
	
	private Settings createNewSettings(Settings actualSettings) {
		return Settings.builder()
				.rootDirectioresToScan(actualSettings.getRootDirectioresToScan())
				.fileExtensions(actualSettings.getFileExtensions())
				.webAliases(actualSettings.getWebAliases())
				.dirAliases(dirAliasTableView.getItems())
				.build();
	}

	@FXML
	public void handleCancelButton() {
		LOG.debug("handleCancelButton");
		Stage settingsStage = getStage(dirAliasTableView);
		settingsStage.close();
	}

	private void configureTableView() {
		aliasColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("name"));
		directoryPathColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("value"));
	}

}
