package io.mosip.print.listener.controller.base;

import java.util.Timer;

import javafx.animation.Timeline;
import org.springframework.stereotype.Component;

import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class FXComponents {
		
	private Stage stage;
	private Scene scene;
	private Timer timer;
	private Timeline timeline;
	private Timeline printerOnlineCheckTimerline;
	private Timeline activeMqOnlineCheckTimerline;

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Timeline getPrinterOnlineCheckTimerline() {
		return printerOnlineCheckTimerline;
	}

	public void setPrinterOnlineCheckTimerline(Timeline printerOnlineCheckTimerline) {
		this.printerOnlineCheckTimerline = printerOnlineCheckTimerline;
	}

	public Timeline getActiveMqOnlineCheckTimerline() {
		return activeMqOnlineCheckTimerline;
	}

	public void setActiveMqOnlineCheckTimerline(Timeline activeMqOnlineCheckTimerline) {
		this.activeMqOnlineCheckTimerline = activeMqOnlineCheckTimerline;
	}
}
