package szczepanski.gerard.runit.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import szczepanski.gerard.runit.common.exception.RunitValidationException;
import szczepanski.gerard.runit.settings.loader.Alias;
import szczepanski.gerard.runit.settings.loader.Settings;
import szczepanski.gerard.runit.settings.loader.SettingsLoader;
import szczepanski.gerard.runit.settings.validator.Validator;
import szczepanski.gerard.runit.settings.writer.SettingsWriter;
import szczepanski.gerard.runit.view.util.DialogDisplayer;

import java.util.Optional;

@RequiredArgsConstructor
public class SettingsWebPaneTabController extends AbstractSettingsTabController {
    private static final Logger LOG = Logger.getLogger(SettingsWebPaneTabController.class);

    private final SettingsLoader settingsLoader;
    private final SettingsWriter settingsWriter;
    private final Validator<Alias> webAliasValidator;

    @FXML
    private TableView<Alias> webAliasTableView;

    @FXML
    private TableColumn<Alias, String> aliasColumn;

    @FXML
    private TableColumn<Alias, String> webAddressColumn;

    @FXML
    public void initialize() {
        LOG.debug("Initialize");
        reloadTab();
    }

    @Override
    public void reloadTab() {
        Settings settings = settingsLoader.getSettings();
        updateWebAliases(settings);
    }

    private void updateWebAliases(Settings settings) {
        configureTableView();
        webAliasTableView.getItems().clear();
        webAliasTableView.getItems().addAll(settings.getWebAliases());
    }

    @FXML
    public void handleAddWebAlias() {
        LOG.debug("handleAddWebAlias");
        getNewAliasFromUserInput().ifPresent(this::addWebAlias);
    }

    /**
     * TODO Gerard Szczepanski 22.01.2017 -> Ugly method, to future refactor
     *
     * 23.01.2023 -> it can wait a little bit longer ;)
     */
    private Optional<Alias> getNewAliasFromUserInput() {
        Optional<String> optAliasName = DialogDisplayer.showInputDialog(getStage(webAliasTableView), "Web Alias Name");
        Optional<String> optAliasValue = Optional.empty();

        if (optAliasName.isPresent()) {
            optAliasValue = DialogDisplayer.showInputDialog(getStage(webAliasTableView), "Web Alias Address", "http://");
        }

        if (optAliasName.isPresent() && optAliasValue.isPresent()) {
            return Optional.of(new Alias(optAliasName.get(), optAliasValue.get()));
        }

        return Optional.empty();
    }

    private void addWebAlias(Alias newWebAlias) {
        try {
            webAliasValidator.validate(newWebAlias);
            addNotDuplicatedWebAliasToTableView(newWebAlias);
        } catch (RunitValidationException e) {
            handleValidationException(e);
        }
    }

    private void addNotDuplicatedWebAliasToTableView(Alias newWebAlias) {
        if (!webAliasTableView.getItems().contains(newWebAlias)) {
            webAliasTableView.getItems().add(newWebAlias);
        } else {
            DialogDisplayer.showValidationMessageDialog("List can not contains duplicates!");
        }
    }

    @FXML
    public void handleRemoveWebAlias() {
        LOG.debug("handleRemoveWebAlias");
        int selectedIndex = webAliasTableView.getSelectionModel().getSelectedIndex();

        if (isSelectedIndexValid(selectedIndex)) {
            webAliasTableView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    public void handleSaveTab() {
        LOG.debug("handleSaveTab");

        Settings actualSettings = settingsLoader.getSettings();
        Settings newSettings = createNewSettings(actualSettings);
        settingsWriter.updateSettings(newSettings);

        DialogDisplayer.showConfirmationMessageDialog("Settings are successfully saved!");
        handleCancelButton();
    }

    private Settings createNewSettings(Settings actualSettings) {
        return Settings.builder().rootDirectioresToScan(actualSettings.getRootDirectioresToScan()).fileExtensions(actualSettings.getFileExtensions())
                .webAliases(webAliasTableView.getItems()).dirAliases(actualSettings.getDirAliases()).build();
    }

    private void configureTableView() {
        aliasColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("name"));
        webAddressColumn.setCellValueFactory(new PropertyValueFactory<Alias, String>("value"));
    }

}
