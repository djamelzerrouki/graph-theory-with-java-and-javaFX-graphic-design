package application;


import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Main extends Application {
	ArrayList<Circle> listcircle=new ArrayList<Circle>();
	ArrayList<Line> listlinee=new ArrayList<Line>();
	ArrayList<node> nodeList=new ArrayList<node>();
	ArrayList<arc> arclist=new ArrayList<arc>();
	ArrayList<Circle> Circles_Orientation=new ArrayList<Circle>();
	DropShadow nodeShadow = new DropShadow();
	Shape selecterdshape=null;
	static int nodeCount=0;
	GridPane gridPane,gridPane2;
	HBox hbox;
	Stage stage =new Stage();
	Stage stage2 =new Stage();
	Stage stage3=new Stage();
	private Line pressedLine;
	private node pressednodeX;
	final private int NodeRadius=15;
	private Boolean IsOrientedGraphe=true;
	Pane root;
	Pane root2;
	static int fileNumber=0; 
	String[][] Mat_Copy ;
	private Stage stagecopy;
	private Circle circleorientation;
	static public double sqr(double a) {
		return a*a;
	}

	static public double Distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(y2 - y1) + sqr(x2 - x1));
	}
	@Override
	public void start(Stage primaryStage) {
		try {

			GridPane roott = new GridPane();
			roott.setHgap(8);
			roott.setVgap(8);
			roott.setPadding(new Insets(5));


			ColumnConstraints cons1 = new ColumnConstraints();
			cons1.setHgrow(Priority.NEVER);
			roott.getColumnConstraints().add(cons1);

			ColumnConstraints cons2 = new ColumnConstraints();
			cons2.setHgrow(Priority.ALWAYS);

			roott.getColumnConstraints().addAll(cons1, cons2);

			RowConstraints rcons1 = new RowConstraints();
			rcons1.setVgrow(Priority.NEVER);

			RowConstraints rcons2 = new RowConstraints();
			rcons2.setVgrow(Priority.ALWAYS);  

			roott.getRowConstraints().addAll(rcons1, rcons2);
			root = new Pane();
			roott.add(root, 0, 1, 4, 2);
			Scene scene = new Scene(roott,400,400);




			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			nodeShadow.setOffsetY(4.0);
			root.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				//Click to make node
				@Override
				public void handle(MouseEvent mouseEvent) {

					MouseButton button = mouseEvent.getButton();
					double x=mouseEvent.getX(), y=mouseEvent.getY();
					if(button==MouseButton.PRIMARY){
						Shape shape=shapeAt(x , y, 20);
						if(shape==null){
							// if there is no node, will create new one 

							Circle c=new Circle( x,  y, NodeRadius);
							c.setFill(Color.BEIGE);
							c.setEffect(nodeShadow);
							StackPane pane=new StackPane();
							Text txt=new Text(nodeCount+++"");
							txt.setX(x);
							txt.setY(y);
							txt.setFill(Color.BLACK);
							pane.getChildren().addAll(c,txt);
							root.getChildren().addAll(c,txt);
							node nd=new node(c, new ArrayList(),txt);
							nodeList.add(nd);
							listcircle.add(c);
							//Update Matrix 
							UpdateMatrix();
							// user click on circle , draw arc
							c.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {


								@Override
								public void handle(MouseEvent mouseEvent) {
									Line l=new Line();
									l.setStartX(x);
									l.setStartY( y);
									l.setEndX(x);
									l.setEndY(y);
									root.getChildren().add(l);
									if(IsOrientedGraphe) {

										Circle c2=new Circle( x, y, 3);
										c2.setFill(Color.RED);

										root.getChildren().addAll(c2);	
										circleorientation = c2;
									}

									pressedLine=l;
									pressednodeX= getNodeAt(x,y,0);

								}
							});
							//if user dragged mouse, draw arc
							c.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									Point A,B,C;
									A=new Point();
									A.setX(pressednodeX.circle.getCenterX());
									A.setY(pressednodeX.circle.getCenterY());

									B=new Point();
									B.setX(mouseEvent.getX());
									B.setY(mouseEvent.getY());

									C=getXY(A,B,NodeRadius);

									pressedLine.setStartX(C.getX());
									pressedLine.setStartY( C.getY());
									pressedLine.setEndX(B.getX());
									pressedLine.setEndY( B.getY());

									if(IsOrientedGraphe) {
										circleorientation.setCenterX(B.getX());
										circleorientation.setCenterY(B.getY());	 
									}

								}});

							c.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent mouseEvent) {
									//pour calculer le point de depart bordure de sommet l7achiya a
									node nodeY=getNodeAt(mouseEvent.getX(),mouseEvent.getY(),0);
									if(nodeY!=null && nodeY!=pressednodeX) {
										Point A,B,C,C_copy;

										//end point

										A=new Point();
										A.setX(pressednodeX.circle.getCenterX());
										A.setY(pressednodeX.circle.getCenterY());

										B=new Point();
										B.setX(nodeY.circle.getCenterX());
										B.setY(nodeY.circle.getCenterY());

										C=getXY(B,A,NodeRadius);
										pressedLine.setEndX(C.getX());
										pressedLine.setEndY(C.getY());
										C_copy=C;



										//start point


										if(!containsCustom(pressedLine)) {
											listlinee.add(pressedLine);
											if(IsOrientedGraphe) {
												circleorientation.setCenterX(C_copy.getX());
												circleorientation.setCenterY(C_copy.getY());  
												Circles_Orientation.add(circleorientation);
											}else {
												circleorientation=new Circle( x, y, 3);
												circleorientation.setFill(Color.RED);

												circleorientation.setCenterX(C_copy.getX());
												circleorientation.setCenterY(C_copy.getY());  
											}

											arc ar=new arc(pressedLine, pressednodeX, nodeY,circleorientation);
											addArcToNode(pressednodeX,nodeY,ar);
											arclist.add(ar); 

											UpdateMatrix();

										}else { root.getChildren().remove(pressedLine);root.getChildren().remove(circleorientation);}
									}else {
										root.getChildren().remove(pressedLine);root.getChildren().remove(circleorientation);
									}
								}

							});
						}
					}else if(button==MouseButton.SECONDARY){
						Shape shape= shapeAt(x, y, 0);
						if(shape!=null) {
							removeshape(shape);
						} }  }

			});
			primaryStage.setScene(scene);
			primaryStage.setTitle("Graph Application");
			primaryStage.show();
			stage.resizableProperty().setValue(Boolean.FALSE);
			stage2.resizableProperty().setValue(Boolean.FALSE);
			stage3.resizableProperty().setValue(Boolean.FALSE);

			this.stagecopy=primaryStage;
			stage.setTitle("Matrice d'adjacence");
			stage2.setTitle("Matrice d'incidence");
			stage3.setTitle("Type de graphe");
			stage3.initStyle(StageStyle.UNDECORATED);
			//  UpdateMatrix();
			setGraphTyapeStage();

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					System.exit(0);
					stage.close();
					stage2.close();
					stage3.close();

				}
			}); 


		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private Point getXY(Point a, Point b, int nodeRadius) {
		double dit=Distance(a.getX(),a.getY(),b.getX(),b.getY());
		double t=nodeRadius/dit;
		double x = (1-t)*a.getX()+t*b.getX();
		double y =(1-t)*a.getY()+t*b.getY();
		Point p=new  Point();
		p.setX(x);
		p.setY(y);
		return p;
	}
	private void UpdateMatrix() {		
		gridPane=getgMatriceAdjacence();
		stage.setX(stagecopy.getX()+stagecopy.getHeight());
		stage.setY(stagecopy.getY());
		stage.setScene(new Scene(gridPane,gridPane.getPrefWidth(),gridPane.getPrefHeight()));
		stage.show();

		gridPane2=getgMatriceIncidence();
		Scene sc=new Scene(gridPane2,gridPane2.getPrefWidth(),gridPane2.getPrefHeight());

		stage2.setScene(sc);
		stage2.setX(stagecopy.getX()-(stage2.getWidth()+10));
		stage2.setY(stagecopy.getY());

		stage2.show();

	}
	private void setGraphTyapeStage() {
		hbox=GrapheOrientation();
		stage3.setScene(new Scene(hbox,stagecopy.getHeight()-20,24));
		stage3.setX(stagecopy.getX());
		stage3.setY(stagecopy.getY()+stagecopy.getWidth()+30);
		stage3.show();
	}
	private void removeshape(Shape shape) {

		if(shape.getClass()==Circle.class) {

			Circle circle=(Circle) shape;
			double centerX=circle.getCenterX(),centerY=circle.getCenterY();
			node nd=getNodeAt(centerX,centerY, 5);
			for (arc arc : nd.arcs) {
				root.getChildren().remove(arc.line);	
				root.getChildren().remove(getCircleOfLine(arc.line));
				Circles_Orientation.remove(getCircleOfLine(arc.line));
				listlinee.remove(arc.line);
				arclist.remove(arc);
			}
			root.getChildren().remove(shape);
			root.getChildren().remove(nd.txt);
			listcircle.remove(circle);
			nodeList.remove(nd);
		}else {
			Line line=(Line) shape;
			root.getChildren().remove(line);
			root.getChildren().remove(getCircleOfLine(line));
			Circles_Orientation.remove(getCircleOfLine(line));
			listlinee.remove(line);
			arclist.remove(arcOf(line));
		}
		UpdateMatrix();
	}
	private arc arcOf(Line line) {

		for (arc arc : arclist) {
			if(arc.line==line) {
				return arc;
			}
		}
		return null;
	}
	private void addArcToNode(node pressednodeX, node nodeY, arc ar) {
		(nodeList.get(nodeList.indexOf(pressednodeX))).arcs.add(ar);
		(nodeList.get(nodeList.indexOf(nodeY))).arcs.add(ar);
	}
	private boolean containsCustom(Line pressedLine) {
		for (Line l : listlinee) {

			Boolean cas1=l.getStartX()==pressedLine.getEndX() &&l.getStartY()==pressedLine.getEndY() &&l.getEndX()==pressedLine.getStartX()&&l.getEndY()==pressedLine.getStartY();
			Boolean cas2=l.getStartX()==pressedLine.getStartX() &&l.getStartY()==pressedLine.getStartY() &&l.getEndX()==pressedLine.getEndX()&&l.getEndY()==pressedLine.getEndY();

			if(cas1||cas2)

				return true;
		}
		return false;
	}
	private node getNodeAt(double x, double y,int dist) {
		node n=null;
		for (node nd: nodeList) {
			if(((Math.hypot(nd.circle.getCenterX()-x, nd.circle.getCenterY()-y))<NodeRadius+dist)) {
				return nd;
			}
		}
		return n;
	}
	private Shape shapeAt(double x, double y,int dist) {
		for (Shape circle: listcircle) {
			if(((Math.hypot(((Circle) circle).getCenterX()-x, ((Circle) circle).getCenterY()-y))<NodeRadius+dist)) {
				return circle;
			}
		}
		for (Line l : listlinee) {
			double ACBC=Distance(l.getStartX(),l.getStartY(),x,y)+Distance(l.getEndX(),l.getEndY(),x,y);
			double AB=Distance(l.getStartX(),l.getStartY(),l.getEndX(),l.getEndY());
			Boolean cas=ACBC<= AB+5 &&ACBC>=AB-5;
			if(cas)
				return l;
		}
		return null;
	}
	private Circle getCircleOfLine(Line line) {
		for (arc ar : arclist) {
			if(ar.line==line)
				return ar.orientation;	
		}
		return null;
	}
	public static void main(String[] args) {
		launch(args);
	}

	private GridPane getgMatriceAdjacence() {

		GridPane roott = new GridPane();

		roott.setHgap(8);
		roott.setVgap(8);
		roott.setPadding(new Insets(5));

		ColumnConstraints cons1 = new ColumnConstraints();
		cons1.setHgrow(Priority.NEVER);

		ColumnConstraints cons2 = new ColumnConstraints();
		cons2.setHgrow(Priority.ALWAYS);

		String[][] Mat = new String[nodeList.size()+1][nodeList.size()+1];
		Mat_Copy = new String[nodeList.size()+1][nodeList.size()+1];

		if(nodeList.size()>0) {
			// roott.add(new Text("-"), 0, 0);
			Mat[0][0]="-";
			Mat_Copy[0][0]="-";
			for (int j = 0; j < nodeList.size(); j++) {
				Mat[0][j+1]=nodeList.get(j).txt.getText().toString();
				Mat[j+1][0]=nodeList.get(j).txt.getText().toString();

				Mat_Copy[0][j+1]=nodeList.get(j).txt.getText().toString();
				Mat_Copy[j+1][0]=nodeList.get(j).txt.getText().toString();
			}
			for (int i = 1; i < nodeList.size()+1; i++) {
				for (int j = 1; j < nodeList.size()+1; j++) {

					//roott.add(new Text("0"), i, j);
					Mat[i][j]="0";
					Mat_Copy[i][j]="0";
				}
			}   
			for (int j = 0; j < arclist.size(); j++) {
				//root.getChildren().remove(( (nodeList.size()+1)* (nodeList.indexOf(arclist.get(j).nodeStart)+1))+(nodeList.indexOf(arclist.get(j).nodeEnd)+1));
				if(!IsOrientedGraphe)
					// roott.add(new Text("1"), nodeList.indexOf(arclist.get(j).nodeStart)+1, nodeList.indexOf(arclist.get(j).nodeStart)+1);
					Mat[nodeList.indexOf(arclist.get(j).nodeStart)+1]  [nodeList.indexOf(arclist.get(j).nodeEnd)+1]  ="1";

				// roott.add(new Text("1"), nodeList.indexOf(arclist.get(j).nodeEnd)+1, nodeList.indexOf(arclist.get(j).nodeStart)+1);
				Mat[ nodeList.indexOf(arclist.get(j).nodeEnd)+1] [nodeList.indexOf(arclist.get(j).nodeStart)+1]="1";
				Mat_Copy[ nodeList.indexOf(arclist.get(j).nodeEnd)+1] [nodeList.indexOf(arclist.get(j).nodeStart)+1]="1";

			}
			for (int i = 0; i < nodeList.size()+1; i++) {
				for (int j = 0; j < nodeList.size()+1; j++) {

					roott.add(new Text(Mat[i][j]), i, j);

				}
			}
		}


		return roott;
	}
	private GridPane getgMatriceIncidence() {
		GridPane roott = new GridPane();
		roott.setHgap(8);
		roott.setVgap(8);
		roott.setPadding(new Insets(5));

		String[][] Mat = new String[arclist.size()+1][nodeList.size()+1];

		Mat[0][0]="-";
		for (int j = 0; j < arclist.size(); j++) {
			Mat[j+1][0]=""+j;
		}
		for (int i = 0; i < nodeList.size(); i++) {
			Mat[0][ 1+i]=  nodeList.get(i).txt.getText().toString();
		}
		for (int i = 1; i < arclist.size()+1; i++) {
			for (int j = 1; j < nodeList.size()+1; j++) {
				Mat[i][j]="0";
			}
		}   
		for (int j = 0; j < arclist.size(); j++) {
			if(!IsOrientedGraphe) {
				Mat[ j+1][nodeList.indexOf(arclist.get(j).nodeStart)+1]="1";
				Mat[ j+1][nodeList.indexOf(arclist.get(j).nodeEnd)+1]="1";
			}

			else {
				Mat[j+1][nodeList.indexOf(arclist.get(j).nodeStart)+1]="1";
				roott.add(new Text("-1"),  j+1,nodeList.indexOf(arclist.get(j).nodeEnd)+1);
				Mat[j+1][nodeList.indexOf(arclist.get(j).nodeEnd)+1]="-1";
			}
		}
		for (int i = 0; i < arclist.size()+1; i++) {
			for (int j = 0; j < nodeList.size()+1; j++) {
				roott.add(new Text(Mat[i][j]), i, j);
			}
		}   
		return roott;
	}
	private HBox GrapheOrientation() {
		HBox root = new HBox();

		root.setSpacing(25);
		root.setPadding(new Insets(2,5,2,5));    
		
		RadioButton orientedGraph=new RadioButton("Graphe Orienté");
		orientedGraph.setSelected(true);
		
		RadioButton nonOrientedGraph=new RadioButton("Graphe Non Orienté");
		
		final ToggleGroup group = new ToggleGroup();
		orientedGraph.setToggleGroup(group);
		nonOrientedGraph.setToggleGroup(group);

		Button DotFile=new Button("Dot");
		DotFile.setMinWidth(110);
		DotFile.setMinHeight(20);
		DotFile.setPadding(new Insets(0));
		
		root.getChildren().add(DotFile);
		root.getChildren().add(orientedGraph);
		root.getChildren().add(nonOrientedGraph);

		DotFile.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				fileNumber++;
				FileMaker f=new FileMaker(Mat_Copy, "MyGraph", "/", IsOrientedGraphe);
				f.makeFile();  
			}
		});

		nonOrientedGraph.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
				if (isNowSelected) { 
					ToNonOrientedGraph();
				} else {
					ToOrientedGraph();
				}
			}
		});
		return root;
	}
	private void ToNonOrientedGraph() {
		IsOrientedGraphe=false;
		for (arc ar : arclist) {
			root.getChildren().remove(ar.orientation);
		}
		UpdateMatrix();
	}
	private void ToOrientedGraph() {
		IsOrientedGraphe=true;

		for (arc ar : arclist) {
			root.getChildren().addAll(ar.orientation);

		}
		UpdateMatrix();
	}

}
