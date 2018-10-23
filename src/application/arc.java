package application;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class arc {

	Line line;
	node nodeStart,nodeEnd;
	Circle orientation;
	public arc(Line line, node nodeStart, node nodeEnd ,Circle orientation) {
	
		this.line = line;
		this.nodeStart = nodeStart;
		this.nodeEnd = nodeEnd;
		this.orientation=orientation;
	}
	
}
