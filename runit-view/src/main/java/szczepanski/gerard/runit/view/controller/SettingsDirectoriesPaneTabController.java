package szczepanski.gerard.runit.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.service.loader.Alias;
import szczepanski.gerard.runit.settings.service.loader.Settings;
import szczepanski.gerard.runit.settings.service.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.service.validator.Validator;
import szczepanski.gerard.runit.settings.service.writer.SettingsWriter;
import szczepanski.gerard.runit.view.util.DialogDisplayer;

import java.io.File;
import java.util.Optional;

@RequiredArgsConstructor
public class SettingsDirectoriesPaneTabController extends AbstractSettingsTabController {
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
        reloadTab();
    }

    @Override
    public void reloadTab() {
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
        getNewAliasFromUserInput()
                .ifPresent(this::addDirAlias);
    }

    /**
     * TODO Gerard Szczepanski 22.01.2017 -> Ugly method, to future refactor
     */
    private Optional<Alias> getNewAliasFromUserInput() {
        Optional<String> optAliasName = DialogDisplayer.showInputDialog(getStage(dirAliasTableView), "Directory Alias Name");
        Optional<File> optAliasValue = Optional.empty();

        if (optAliasName.isPresent()) {
            optAliasValue = DialogDisplayer.showDirectoryChooserDialog(getStage(dirAliasTableView), "Choose directory path");
        }

        if (optAliasName.isPresent() && optAliasValue.isPresent()) {
            String directoryAliasName = optAliasName.get();
            String directoryAliasValue = optAliasValue.get().getPath();
            return Optional.of(new Alias(directoryAliasName, directoryAliasValue));
        }

        return Optional.empty();
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

        if (isSelectedIndexValid(selectedIndex)) {
            dirAliasTableView.getItems().remove(selectedIndex);
        }
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
        return Settings.builder().rootDirectioresToScan(actualSettings.getRootDirectioresToScan()).fileExtensions(actualSettings.getFileExtensions())
                .webAliases(actualSettings.getWebAliases()).dirAliases(dirAliasTableView.getItems()).build();
    }

    private void configureTableView() {
        aliasColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("name"));
        directoryPathColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("value"));
    }

}
