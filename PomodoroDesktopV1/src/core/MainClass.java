

package core;



import java.text.DecimalFormat;

import com.sun.media.jfxmedia.events.PlayerEvent;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sounds.PlaySounds;
import tables.CreateTable;
import tables.GenericCellFactory;


public class MainClass extends Application {

	public final static boolean NO_DATABASE = true;
	
	/** The table which contains all the tasks*/
    private TableView taskTable;
    
    /** The button that starts and pauses the timer*/
    private Button startStop;
    
    /** The button that creates a new task  */
    private Button newTask;
    
    
    /// TIME MANAGEMENT GUBBINZ
    
    /** The number of milliseconds left in this pomodoro*/
    private int millisLeft = pomodoroMillis;
    /**
     * The number of milliseconds which have elapsed while the timer has
     * been running. That is, it is the total time kept on the timer.
     */
    private int elapsedMillis = 0;
    /**
     * Keeps track of the amount of the clock time (CPU clock) when the
     * timer start button was pressed, or when the last tick even occurred.
     * This is used to calculate the elapsed time delta.
     */
    private int lastClockTime = 0;
    private DecimalFormat twoPlaces = new DecimalFormat("00");
    private Timeline time = new Timeline();
    
    /** The number of milliseconds in a 25 minute pomodoro*/
    public static final int pomodoroMillis = 25*60*1000;
    
    /** The number of milliseconds in the 5 minute break*/
    public static final int breakMillis = 5*60*1000;

    // the text object which make up the clockface
    /** the time remaining in minutes*/
	private Text timeMins;
    /** the time remaining in seconds*/
	private Text timeSecs;
    /** the time remaining in milliseconds*/
	private Text timeMilis;
	

	/** Indicates how far thought the pomodoro you are*/
	private ProgressIndicator progressIdicator;

	
	// Task management
	/** The table view selection model for the task table*/
	private TableViewSelectionModel selectedTask;
	/** the current task object*/
	private Task currentTask;
    
    

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
    
        // configure the timeline - deals with passing of time
        configureTimeline();
        
        // Set up the toolbar
        String styledToolBarCss = MainClass.class.getResource("StyledToolBar.css").toExternalForm();
        ToolBar darkToolbar = createToolBar("mainToolbar");        
        darkToolbar.getStylesheets().add(styledToolBarCss);
        
        
      //  setupTableListeners();

        // grey out start stop button
        startStop.setText("--");
     
        
        // Set up the task list
		if (NO_DATABASE)
		{
        	taskTable = CreateTable.makeDefaultTable();
        	taskTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
          

			@Override
			public void handle(MouseEvent click) {
				 startStop.setText("Start");
				 
					selectedTask = taskTable.getSelectionModel();
					  
				
			}
        });
        
    
		}
		else
		{
			// get table from DB
		}
		
		// set the font for the clock text TODO make this changeable in the options
		int fontSize = 50;
		String font= "Arial";
		
		//init the clock text objects
		timeMins = new Text ("25");
		timeMins.setFont(new Font(font ,fontSize));
		
		Text colon1 = new Text(":");
		colon1.setFont(new Font(font, fontSize));
		
		timeSecs = new Text("00");
		timeSecs.setFont(new Font(font, fontSize));
		
		Text colon2 = new Text(":");
		colon2.setFont(new Font(font, fontSize));
		
		timeMilis = new Text("00");
		timeMilis.setFont(new Font(font, fontSize));
		
		// put the clock together using a grid object
		GridPane clockGrid = new GridPane();
	    clockGrid.add(timeMins, 0, 0);
	    clockGrid.add(colon1, 1, 0);
	    clockGrid.add(timeSecs, 2, 0);
	    clockGrid.add(colon2, 3, 0);
	    clockGrid.add(timeMilis, 4, 0);
	     // set the spacing between the grid cells 
	    clockGrid.setHgap(10);
	    clockGrid.setVgap(10);
	    // sets alignment to centre
	    clockGrid.setAlignment(Pos.CENTER);
	        
		
		// create the progress indicator

		progressIdicator = new ProgressIndicator();
        progressIdicator.setPrefSize(100, 100);
        progressIdicator.setProgress(0);

  
        // add the progress indicator and the clock to a grid which holds them both on the same line
		GridPane clockProgressContainer = new GridPane();
        clockProgressContainer.add(clockGrid, 0, 0);
        clockProgressContainer.add(progressIdicator, 1, 0);
        clockProgressContainer.setAlignment(Pos.CENTER);
		
		// put all the views together in the window
        root.getChildren().add(VBoxBuilder.create().spacing(2).padding(new Insets(10)).children(clockProgressContainer, darkToolbar,taskTable).build());

    }

 

    private void setupTableListeners() {

    	// Create a context menu
        ContextMenu menu = new ContextMenu();
        MenuItem item = new MenuItem("View Task");
      
        item.setOnAction(new EventHandler() {
        	@Override
			public void handle(Event event) {
				 Task task = (Task) taskTable.getSelectionModel().getSelectedItem();
                 // do something
				
			}
        });
        menu.getItems().addAll(item);
        
        // Create a click event that looks for double clicks
        EventHandler click = new EventHandler() {
        	public void handle(MouseEvent t) {
                  if(t.getClickCount() >= 1) {
                      Task task = (Task) taskTable.getItems().get(((TableCell)t.getSource()).getIndex());
                      System.out.println("task name: " + task.getTitle());
                  }
              }

			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				
			}
        };
        
        GenericCellFactory cellFactory = new GenericCellFactory(click, menu);
       taskTable =  CreateTable.makeTableWithClickListeners(cellFactory);
		
	}



	/**
     * Creates the main toolbar and sets up all the buttons
     * @param id
     * @return
     */
    private ToolBar createToolBar(String id) {
    	
    	startStop = new Button("Start");
    	
    	// set the listener for this button - eg what happens when its pressed
    	startStop.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

            	// starts or stops the timer
                startStop();
            }
        });
    	
    	 newTask = new Button ("New Task");

        return ToolBarBuilder.create().id(id).items(

                startStop,
                newTask,
                new Slider()).build();

    }

 

    
    
    /// TIME BITS
    
    
    private void configureTimeline() {
        time.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(47), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                calculate();
            }
        });
        time.getKeyFrames().add(keyFrame);
    }
    
    //MODEL
    /**
     * Models the remaining time- converts milliseconds to minutes and seconds on digital watchface
     */
    private void calculate() {
        if (lastClockTime == 0) {
            lastClockTime = (int) System.currentTimeMillis();
        }

        int now = (int) System.currentTimeMillis();
        int delta = now - lastClockTime;

        elapsedMillis += delta;
        
        double progressPerc = (double)elapsedMillis/ (double)pomodoroMillis;
        progressIdicator.setProgress(progressPerc);
        
        millisLeft = pomodoroMillis - elapsedMillis;
        
        if (millisLeft <= 0 )
        {
        	PlaySounds.playAlarmSound();
        	// finished a pomodoro
        	
        }

        int tenths = (millisLeft / 10) % 100;
        int seconds = (millisLeft / 1000) % 60;
        int mins = (millisLeft / 60000) % 60;

        refreshTimeDisplay(mins, seconds, tenths);

        lastClockTime = now;
    }

    public void startStop() {
        if (time.getStatus() != Status.STOPPED) {
            // if started, stop it
            time.stop();
            lastClockTime = 0;
            // change button text to say 'start'
            startStop.setText("Start");
        } else {
            // if stopped, restart
            time.play();
            PlaySounds.playTickSound();
            // set button to say 'Pause'
            startStop.setText("Pause");
        }
    }

    public void stopReset() {
        if (time.getStatus() != Status.STOPPED) {
            // if started, stop it
            time.stop();
            lastClockTime = 0;
        } else {
            // if stopped, reset it
            lastClockTime = 0;
            elapsedMillis = 0;
            refreshTimeDisplay(0, 0, 0);
        }
    }

    /**
     * Refreshes the text objects of the clockface with the current remaining time
     * @param mins number of minutes left
     * @param seconds number of seconds left
     * @param tenths number of tenths of seconds left
     */
    private void refreshTimeDisplay(int mins, int seconds, int tenths) {

    	timeMins.setText(twoPlaces.format(mins));
    	timeSecs.setText(twoPlaces.format(seconds));
		timeMilis.setText(twoPlaces.format(tenths));
    	
    }

    
    // These two bits are used by javafx to start the whole shebang

    @Override public void start(Stage primaryStage) throws Exception {

        init(primaryStage);

        primaryStage.show();

    }
    

    public static void main(String[] args) { launch(args); }

}