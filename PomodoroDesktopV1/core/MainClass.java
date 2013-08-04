package core;

import java.text.DecimalFormat;

import com.sun.corba.se.impl.orbutil.closure.Constant;

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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.PasswordDialogService;
import login.PasswordDialogService.DialogService;
import objects.NewTaskDialogService;
import objects.WillyTask;
import sounds.PlaySounds;
import tables.CreateTable;
import tables.GenericCellFactory;
import database.DatabaseQuerys;
import database.HandlePasswords;

public class MainClass extends Application {

	/** For debugging - determines whether we use the database */
	public final static boolean NO_DATABASE = false;
	
	/** Describes the current status of connectivity to the database*/
	public static Text dataBaseConnectionStatus;

	/** holds the pw field, can be made to dissapear if the password was right */
	private GridPane pwGrid;

	/** The table which contains all the tasks */
	public static TableView taskTable;

	/** The button that starts and pauses the timer */
	private Button startStop;

	/** The button that creates a new task */
	private Button newTask;
	
	/** The primary stage*/
	private Stage primaryStage;

	// / TIME MANAGEMENT GUBBINZ

	/** The number of milliseconds left in this pomodoro */
	private int millisLeft = pomodoroMillis;
	/**
	 * The number of milliseconds which have elapsed while the timer has been
	 * running. That is, it is the total time kept on the timer.
	 */
	private int elapsedMillis = 0;
	/**
	 * Keeps track of the amount of the clock time (CPU clock) when the timer
	 * start button was pressed, or when the last tick even occurred. This is
	 * used to calculate the elapsed time delta.
	 */
	private int lastClockTime = 0;
	private DecimalFormat twoPlaces = new DecimalFormat("00");
	private Timeline time = new Timeline();

	/** The number of milliseconds in a 25 minute pomodoro */
	// public static final int pomodoroMillis = 25*60*1000;
	public static final int pomodoroMillis = 3 * 1000; // for debugging

	/** The number of milliseconds in the 5 minute break */
	public static final int breakMillis = 5 * 60 * 1000;

	// the text object which make up the clockface
	/** the time remaining in minutes */
	private Text timeMins;
	/** the time remaining in seconds */
	private Text timeSecs;
	/** the time remaining in milliseconds */
	private Text timeMilis;

	/** Indicates how far thought the pomodoro you are */
	private ProgressIndicator progressIdicator;

	// Task management
	/** The table view selection model for the task table */
	private TableViewSelectionModel selectedTask;
	/** the current task object */
	private WillyTask currentTask;

	/** The login dialog service */
	private DialogService loginDialogService;
	
	/** The new task dialog service*/
	private objects.NewTaskDialogService.DialogService newTaskDialogService;

	private void init(Stage primaryStage) {
		Group root = new Group();
		primaryStage.setScene(new Scene(root));

		// get the primary stage as a field
		this.primaryStage = primaryStage;
		// configure the timeline - deals with passing of time
		configureTimeline();
		
		// data base connectivity text
		dataBaseConnectionStatus = new Text("No Database Connection");

		// Set up the toolbar
		String styledToolBarCss = MainClass.class.getResource(
				"StyledToolBar.css").toExternalForm();
		ToolBar darkToolbar = createToolBar("mainToolbar");
		darkToolbar.getStylesheets().add(styledToolBarCss);


		// grey out start stop button
		startStop.setText("--");

		
		// Set up the task list
		if (!NO_DATABASE) {
			// get table from DB

			// create the password field and populate the table from DB
			setupPWField(primaryStage);
			 taskTable = CreateTable.populateTaskTableFromDB();

	

		}
		else
		{
		// make an default table from local stuff
		taskTable = CreateTable.makeDefaultTable();
		}
		// set up the mouse click listener so we know which task has been
		// selected in the table
		taskTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent click) {
				startStop.setText("Start");

				selectedTask = taskTable.getSelectionModel();
				currentTask = (WillyTask) selectedTask.getSelectedItem();

			}
		});
		// set the font for the clock text TODO make this changeable in the
		// options
		int fontSize = 50;
		String font = "Arial";

		// init the clock text objects
		timeMins = new Text("25");
		timeMins.setFont(new Font(font, fontSize));

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

		// add the progress indicator and the clock to a grid which holds them
		// both on the same line
		GridPane clockProgressContainer = new GridPane();
		clockProgressContainer.add(clockGrid, 0, 0);
		clockProgressContainer.add(progressIdicator, 1, 0);
		clockProgressContainer.setAlignment(Pos.CENTER);

		// put all the views together in the window
		root.getChildren().add(
				VBoxBuilder
						.create()
						.spacing(2)
						.padding(new Insets(10))
						.children(dataBaseConnectionStatus,clockProgressContainer, darkToolbar,
								taskTable).build());

	

	}

	/**
	 * Sets up the password field at the top and checks if there is a pre
	 * existing .scrote with the pw in it
	 */
	private void setupPWField(Stage primaryStage) {

		// do we need to get the password or is it already in file?
		String pw = HandlePasswords.checkForPasswordFile();
		// if its null or doesnt work that means we need to enter it
		if (pw == null || !HandlePasswords.checkIfRightPW(GlobalConstants.DB_USERNAME,pw)) {

			if (loginDialogService != null) {
				loginDialogService.hide();
			}
			PasswordDialogService login = new PasswordDialogService();
			loginDialogService = login.createLoginDialog(primaryStage);
			loginDialogService.start();
			
		}
		else{
			// password was right
			// make the text small
			dataBaseConnectionStatus.setVisible(false);
			dataBaseConnectionStatus.setScaleY(0);
			dataBaseConnectionStatus.setScaleX(0);
			
		}
	}

	/**
	 * Creates the main toolbar and sets up all the buttons
	 * 
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

		newTask = new Button("New Task");

		newTask.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {

				// starts or stops the timer
				makeNewTask();
			}
		});

		return ToolBarBuilder.create().id(id).items(

		startStop, newTask, new Slider()).build();

	}

	protected void makeNewTask() {
		//TODO
		NewTaskDialogService newTask = new NewTaskDialogService();
		newTaskDialogService = newTask.createLoginDialog(primaryStage);
		newTaskDialogService.start();
	}

	// / TIME BITS

	private void configureTimeline() {
		time.setCycleCount(Timeline.INDEFINITE);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(47),
				new EventHandler<ActionEvent>() {

					public void handle(ActionEvent event) {
						calculate();
					}
				});
		time.getKeyFrames().add(keyFrame);
	}

	// MODEL
	/**
	 * Models the remaining time- converts milliseconds to minutes and seconds
	 * on digital watchface
	 */
	private void calculate() {
		if (lastClockTime == 0) {
			lastClockTime = (int) System.currentTimeMillis();
		}

		int now = (int) System.currentTimeMillis();
		int delta = now - lastClockTime;

		elapsedMillis += delta;

		double progressPerc = (double) elapsedMillis / (double) pomodoroMillis;
		progressIdicator.setProgress(progressPerc);

		millisLeft = pomodoroMillis - elapsedMillis;

		if (millisLeft <= 0) {

			currentTask.completedPomsProperty().set(
					currentTask.completedPomsProperty().intValue() + 1);
			DatabaseQuerys.updateTask(currentTask);
			PlaySounds.playAlarmSound();
			startStop();

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
	 * Refreshes the text objects of the clockface with the current remaining
	 * time
	 * 
	 * @param mins
	 *            number of minutes left
	 * @param seconds
	 *            number of seconds left
	 * @param tenths
	 *            number of tenths of seconds left
	 */
	private void refreshTimeDisplay(int mins, int seconds, int tenths) {

		timeMins.setText(twoPlaces.format(mins));
		timeSecs.setText(twoPlaces.format(seconds));
		timeMilis.setText(twoPlaces.format(tenths));

	}

	// These two bits are used by javafx to start the whole shebang

	@Override
	public void start(Stage primaryStage) throws Exception {

		init(primaryStage);

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public static void setDatabaseUsernameAndPassword(String dbUsername,String databasePassword) {
		
		HandlePasswords.setPassword(databasePassword);
		HandlePasswords.setUsername(dbUsername);
	}

}