package io.mosip.print.listener.controller.base;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.constant.RegistrationConstants;
import io.mosip.print.listener.controller.PrintListenerController;
import io.mosip.print.listener.logger.PrintListenerLogger;
import io.mosip.print.listener.util.ApplicationResourceContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class BaseController {
    Logger logger = PrintListenerLogger.getLogger(PrintListenerController.class);

    ApplicationContext applicationContext;

    @Autowired
    FXComponents fXComponents;

    protected Scene scene;

    public static Boolean isPrinterOnline;

    /**
     * Loading FXML files along with beans.
     *
     * @param <T> the generic type
     * @param url the url
     * @return T
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static <T> T load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url, ApplicationResourceContext.getInstance().getLabelBundle());
        loader.setControllerFactory(ApplicationResourceContext.getInstance().getApplicationContext()::getBean);
        return loader.load();
    }

    /**
     * Gets the scene.
     *
     * @param borderPane the border pane
     * @return the scene
     */
    protected Scene getScene(Parent borderPane) {
        scene = fXComponents.getScene();
        if (scene == null) {
            scene = new Scene(borderPane);
            fXComponents.setScene(scene);
        }
        scene.setRoot(borderPane);
        fXComponents.getStage().setScene(scene);
        scene.getStylesheets().add(ClassLoader.getSystemClassLoader().getResource(getCssName()).toExternalForm());
        return scene;
    }

    protected String getCssName() {
        return RegistrationConstants.CSS_PATH;
    }

    public static Boolean getPrinterOnline() {
        return isPrinterOnline;
    }

    public static void setPrinterOnline(Boolean printerOnline) {
        isPrinterOnline = printerOnline;
    }
}
