package io.mosip.print.listener.controller;

import io.mosip.print.listener.constant.RegistrationConstants;
import io.mosip.print.listener.controller.base.BaseController;
import io.mosip.print.listener.controller.base.FXComponents;
import io.mosip.print.listener.logger.LogMessage;
import io.mosip.print.listener.logger.PrintListenerLogger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.*;

/**
 * The Class IdaController.
 * 
 * @author Sanjay Murali
 */
@Component
public class PrintLogController extends BaseController implements Initializable {

    @Autowired
    FXComponents fXComponents;

    @FXML
    ScrollPane logScrollPane;

    @FXML
    TextFlow logTextFlow;

    private Boolean isAutoScrollDownRequired = true;

    private Integer count = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logTextFlow.getChildren().clear();

        if(fXComponents.getTimeline() == null) {
            init();
            fXComponents.setTimeline(new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                List<LogMessage>  messageList = new ArrayList<>(PrintListenerLogger.logMessageList);

                for(LogMessage message : messageList) {
                    Text text = new Text();
                    text.setFill(message.getMessageType().getColor());
                    text.setText(message.getMessage() + "\r\n");
                    text.setFont(Font.font(" Times New Roman", FontPosture.REGULAR, 12));
                    logTextFlow.getChildren().add(text);
                }
                PrintListenerLogger.logMessageList.removeAll(messageList);
                messageList.clear();

                if(logTextFlow.getChildren().size() > 100) {
                    logTextFlow.getChildren().remove(0);
                }
                if(isAutoScrollDownRequired)
                    logScrollPane.setVvalue(logScrollPane.getVmax());
            })));

            fXComponents.getTimeline().setCycleCount(Animation.INDEFINITE);
            fXComponents.getTimeline().play();
        }
    }

    public void init() {
        logScrollPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isAutoScrollDownRequired=false;
            }
        });

        logScrollPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isAutoScrollDownRequired=true;
            }
        });
    }
}
