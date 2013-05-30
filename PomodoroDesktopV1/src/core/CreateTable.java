package core;
/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import objects.Task;
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
   
	public  static TableView makeDefaultTable() {
        Group root = new Group();
       
        
        final ObservableList<Task> data = FXCollections.observableArrayList(
            new Task("Twist Balls" ),
            new Task("Wrangle Cock" ),
            new Task("Pillage Gubbinz" )

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
        
        TableView tableView = new TableView();
        tableView.setItems(data);
        tableView.getColumns().addAll(taskNameCol, pomsCompletedColumn, dateCol);
    
        return tableView;
	
	}

   

}
