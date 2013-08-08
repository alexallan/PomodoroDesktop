package tables;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import java.sql.SQLException;

import com.mysql.jdbc.ResultSet;

import core.GlobalConstants;
import database.DatabaseQuerys;
import objects.WillyTask;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;





public class CreateTable {
	
	/** This object when updated will dynamically change what is in the table*/
	public static ObservableList<WillyTask> data;

	
	@Deprecated 
	/***
	 * Makes  a default table with a load of bollocks in it for when we arent using the database
	 * @return
	 */
	public static TableView<WillyTask> makeDefaultTable() {
		Group root = new Group();

		final ObservableList<WillyTask> data = FXCollections
				.observableArrayList(new WillyTask("Twist Balls"),
						new WillyTask("Wrangle Cock"), new WillyTask(
								"Pillage Gubbinz")

				);

		TableColumn taskNameCol = new TableColumn();
		taskNameCol.setText("Task Name");
		taskNameCol.setCellValueFactory(new PropertyValueFactory("taskName"));

		TableColumn pomsCompletedColumn = new TableColumn();
		pomsCompletedColumn.setText("# Completed");
		pomsCompletedColumn.setCellValueFactory(new PropertyValueFactory(
				"completedPoms"));

		TableColumn dateCol = new TableColumn();
		dateCol.setText("Date Created");
		dateCol.setMinWidth(200);
		dateCol.setCellValueFactory(new PropertyValueFactory("dateStartedMilis"));

		TableView<WillyTask> tableView = new TableView<WillyTask>();
		tableView.setItems(data);
		tableView.getColumns()
				.addAll(taskNameCol, pomsCompletedColumn, dateCol);

		return tableView;

	}
	
	/**It should be just this simple to update the table
	 * */
	public static void updateTableFromDB()
	{
		data = DatabaseQuerys.pullTaskTable();
	}
	
	/**
	 * Adds a task to the table
	 * @param task the task we wish to add
	 */
	public static void addTaskToTable(WillyTask task)
	{
		data.add(task); // TODO doesnt work
	}

	/**
	 * Populates the table from the database
	 * @return a populate task table
	 */
	public static TableView<WillyTask> populateTaskTableFromDB() {

		// we want to populate the table from the database using a new thread
		// (so it doesnt lag to fuck)

		ObservableList<WillyTask>  data = DatabaseQuerys.pullTaskTable();
	
		TableColumn taskNameCol = new TableColumn();
		taskNameCol.setText("Task Name");
		taskNameCol.setCellValueFactory(new PropertyValueFactory("taskName"));

		TableColumn pomsCompletedColumn = new TableColumn();
		pomsCompletedColumn.setText("# Completed");
		pomsCompletedColumn.setCellValueFactory(new PropertyValueFactory(
				"completedPoms"));

		TableColumn dateCol = new TableColumn();
		dateCol.setText("Date Created");
		dateCol.setMinWidth(200);
		dateCol.setCellValueFactory(new PropertyValueFactory("dateStartedMilis"));

		TableView<WillyTask> tableView = new TableView<WillyTask>();
		tableView.setItems(data);
		tableView.getColumns()
				.addAll(taskNameCol, pomsCompletedColumn, dateCol);

		return tableView;

	}

	// to have on click events we need to define a custom table cell factory

	public static TableView<WillyTask> makeTableWithClickListeners(
			GenericCellFactory cellFactory) {
		final ObservableList<WillyTask> data = FXCollections
				.observableArrayList(new WillyTask("Twist Balls"),
						new WillyTask("Wrangle Cock"), new WillyTask(
								"Pillage Gubbinz")

				);

		TableColumn taskNameCol = new TableColumn();
		taskNameCol.setText("Task Name");
		taskNameCol.setCellValueFactory(cellFactory);

		TableColumn pomsCompletedColumn = new TableColumn();
		pomsCompletedColumn.setText("# Completed");
		pomsCompletedColumn.setCellValueFactory(cellFactory);

		TableColumn dateCol = new TableColumn();
		dateCol.setText("Date Created");
		dateCol.setMinWidth(200);
		dateCol.setCellValueFactory(cellFactory);

		TableView<WillyTask> tableView = new TableView<WillyTask>();
		tableView.setItems(data);
		tableView.getColumns()
				.addAll(taskNameCol, pomsCompletedColumn, dateCol);

		return tableView;
	}

}
