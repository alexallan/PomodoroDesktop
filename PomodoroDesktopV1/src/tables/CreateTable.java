package tables;
/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
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

/**
 * A simple table with a header row.
 *
 * @see javafx.scene.control.TableCell
 * @see javafx.scene.control.TableColumn
 * @see javafx.scene.control.TablePosition
 * @see javafx.scene.control.TableRow
 * @see javafx.scene.control.TableView
 */
public class CreateTable  {
   
	public  static TableView<WillyTask> makeDefaultTable() {
        Group root = new Group();
       
        
        final ObservableList<WillyTask> data = FXCollections.observableArrayList(
            new WillyTask("Twist Balls" ),
            new WillyTask("Wrangle Cock" ),
            new WillyTask("Pillage Gubbinz" )

        );
        
        TableColumn taskNameCol = new TableColumn();
        taskNameCol.setText("Task Name");
        taskNameCol.setCellValueFactory(new PropertyValueFactory("taskName"));
        
       
        
        TableColumn pomsCompletedColumn = new TableColumn();
        pomsCompletedColumn.setText("# Completed");
        pomsCompletedColumn.setCellValueFactory(new PropertyValueFactory("completedPoms"));
        
        TableColumn dateCol = new TableColumn();
        dateCol.setText("Date Created");
        dateCol.setMinWidth(200);
        dateCol.setCellValueFactory(new PropertyValueFactory("dateStartedMilis"));
        
        TableView<WillyTask> tableView = new TableView<WillyTask>();
        tableView.setItems(data);
        tableView.getColumns().addAll(taskNameCol, pomsCompletedColumn, dateCol);
    
        return tableView;
	
	}
	
	// to have on click events we need to define a custom table cell factory

	public static TableView<WillyTask> makeTableWithClickListeners(GenericCellFactory cellFactory)
	{
		final ObservableList<WillyTask> data = FXCollections.observableArrayList(
	            new WillyTask("Twist Balls" ),
	            new WillyTask("Wrangle Cock" ),
	            new WillyTask("Pillage Gubbinz" )

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
	        tableView.getColumns().addAll(taskNameCol, pomsCompletedColumn, dateCol);
	        
	        
	        return tableView;
	}
   

}
