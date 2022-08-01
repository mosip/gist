package io.mosip.print.listener;

import io.mosip.print.listener.controller.HomeController;
import io.mosip.print.listener.controller.base.FXComponents;
import io.mosip.print.listener.util.ApplicationResourceContext;
import io.mosip.print.listener.util.PrinterUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Timer;

@SpringBootApplication (exclude = {SecurityAutoConfiguration.class})
public class PrintListenerApplication extends Application {

    private ConfigurableApplicationContext context;
    FXMLLoader loader = new FXMLLoader();

    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("file.encoding", "UTF-8");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationResourceContext.getInstance();
        HomeController controller = ApplicationResourceContext.getInstance().getApplicationContext().getBean(HomeController.class);
        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ApplicationResourceContext.getInstance().getApplicationContext().getBean(PrinterUtil.class).isPrintArchievePathExist();
                ApplicationResourceContext.getInstance().getApplicationContext().getBean(PrinterUtil.class).printerHealthCheck();
            }
        });

        controller.showUserNameScreen(primaryStage);
    }

    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PrintListenerApplication.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
        ApplicationResourceContext.getInstance().setApplicationLanguage(context.getEnvironment().getProperty("mosip.primary-language"));
        ApplicationResourceContext.getInstance().setApplicationSupportedLanguage(context.getEnvironment().getProperty("mosip.supported-languages"));
        ApplicationResourceContext.getInstance().setApplicationContext(context);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        FXComponents fxComponents =  ApplicationResourceContext.getInstance().getApplicationContext().getBean(FXComponents.class);
        fxComponents.getTimer().cancel();
        fxComponents.getTimer().purge();
        fxComponents.getTimeline().stop();
        fxComponents.getPrinterOnlineCheckTimerline().stop();
        fxComponents.getStage().close();
        System.exit(1);
    }
}
