package io.mosip.print.listener.controller;

import io.mosip.print.listener.constant.RegistrationConstants;
import io.mosip.print.listener.controller.base.BaseController;
import io.mosip.print.listener.controller.base.FXComponents;
import io.mosip.print.listener.util.ApplicationResourceContext;
import io.mosip.print.listener.util.PrinterUtil;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
