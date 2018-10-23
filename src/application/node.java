package application;

import java.util.ArrayList;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class node {
	Circle circle;
	ArrayList<arc> arcs;
	Text txt;
	public node(Circle circle, ArrayList<arc> arcs,Text txt) {
		this.circle = circle;
		this.arcs = arcs;
		this.txt=txt;
	}

}
