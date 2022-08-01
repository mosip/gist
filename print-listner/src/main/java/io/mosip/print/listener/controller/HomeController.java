package io.mosip.print.listener.controller;

import io.mosip.print.listener.constant.RegistrationConstants;
import io.mosip.print.listener.controller.base.BaseController;
import io.mosip.print.listener.controller.base.FXComponents;
import io.mosip.print.listener.util.ApplicationResourceContext;
import io.mosip.print.listener.util.PrinterUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Class IdaController.
 * 
 * @author Sanjay Murali
 */
@Component
public class HomeController extends BaseController implements Initializable {

    @Autowired
    FXComponents fXComponents;

    @Autowired
    Environment env;

    @FXML
    CheckBox printerLockCheckBox;

    @FXML
    ChoiceBox printerName;

    @FXML
    FlowPane printerLogoPanel;

    private GridPane loginRoot;
    protected Scene scene;


    public void showUserNameScreen(Stage primaryStage) throws IOException {
        fXComponents.setStage(primaryStage);

        loginRoot = BaseController.load(getClass().getResource(RegistrationConstants.MAIN_PAGE));

        scene = getScene(loginRoot);

  //      Screen screen = Screen.getPrimary();
   //     Rectangle2D bounds = screen.getVisualBounds();
 //       primaryStage.setX(bounds.getMinX());
 //       primaryStage.setY(bounds.getMinY());
 //       primaryStage.setWidth(bounds.getWidth());
 //       primaryStage.setHeight(bounds.getHeight());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource(RegistrationConstants.LOGO).toExternalForm()));
        primaryStage.show();
        initialize(null, null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean printRequired  = env.getProperty("mosip.print.pdf.printing.required", boolean.class);

        if(printRequired) {
            if(fXComponents.getPrinterOnlineCheckTimerline() == null) {
                fXComponents.setPrinterOnlineCheckTimerline(new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
                    if(getPrinterOnline()) {
                        printerLogoPanel.getStyleClass().remove(0);
                        printerLogoPanel.getStyleClass().add("printeronline");
                    } else {
                        printerLogoPanel.getStyleClass().remove(0);
                        printerLogoPanel.getStyleClass().add("printeroffline");
                    }
                })));
                fXComponents.getPrinterOnlineCheckTimerline().setCycleCount(Animation.INDEFINITE);
                fXComponents.getPrinterOnlineCheckTimerline().play();
            }
        }
    }

    @FXML
    private void onPrinterSelected() {

    }

    @FXML
    private void lockPrinterDropdown() {
        if(printerLockCheckBox.isSelected()) {
            printerName.setDisable(true);
        } else {
            printerName.setDisable(false);
        }
    }
}
