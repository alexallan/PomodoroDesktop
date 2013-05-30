

package core;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MainClass extends Application {

	public final static boolean NO_DATABASE = true;
	
	/** The table which contains all the tasks*/
    private TableView taskTable;
    
    /** The button that starts and pauses the timer*/
    private Button startStop;
    
    /** The button that creates a new task  */
    private Button newTask;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
    
        // Set up the toolbar
        String styledToolBarCss = MainClass.class.getResource("StyledToolBar.css").toExternalForm();
        ToolBar darkToolbar = createToolBar("mainToolbar");        
        darkToolbar.getStylesheets().add(styledToolBarCss);
        

        // Set up the task list
		if (NO_DATABASE)
		{
        	taskTable = CreateTable.makeDefaultTable();
		}
		else
		{
			// get table from DB
		}
		
		Text timeHours = new Text ("24");
		timeHours.setFont("Ariel 20pt");
		
		// put all the views together in the window
        root.getChildren().add(VBoxBuilder.create().spacing(10).padding(new Insets(10)).children( taskTable,darkToolbar).build());

    }

 

    /**
     * Creates the main toolbar and sets up all the buttons
     * @param id
     * @return
     */
    private ToolBar createToolBar(String id) {
    	
    	startStop = new Button("Start");
    	 newTask = new Button ("New Task");

        return ToolBarBuilder.create().id(id).items(

                startStop,
                newTask,
                new Slider()).build();

    }

 

    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }
    
    
    
    
    
    
    
    
    
    
    

    public static void main(String[] args) { launch(args); }

}