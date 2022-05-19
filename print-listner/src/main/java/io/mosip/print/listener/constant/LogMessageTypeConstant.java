package io.mosip.print.listener.constant;


import javafx.scene.paint.Color;

public enum LogMessageTypeConstant {
	INFO (Color.BLACK),
	WARNING (Color.BROWN),
	ERROR (Color.RED),
	SUCCESS(Color.GREEN);

	private Color color;

	LogMessageTypeConstant(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
