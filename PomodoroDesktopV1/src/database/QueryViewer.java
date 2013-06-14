package database;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import database.HandlePasswords;
 
public class QueryViewer extends Application {
 
  private final List<String> currentDatabaseFields = new ArrayList<String>();
 
  @Override
  public void start(Stage primaryStage) {
    final VBox root = new VBox(10);
    root.getChildren().add(new TitledPane("Drivers", createDriverManager()));
    root.getChildren().add(createQueryPane());
    Scene scene = new Scene(root, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
 
  private void showSelectedRowDetails(Map<String, Object> selectedRow) {
    int i = 1;
    for (String header : currentDatabaseFields) {
      System.out.printf("Column %d: Field %s, value is %s%n", i, header,
          selectedRow.get(header));
      i++;
    }
  }
 
  private void populateTableFromQuery(String databaseURL, String user,
      String password, String query, TableView<Map<String, Object>> table) {
    System.out.println("Populating table using " + query);
    try (
        Connection conn = DriverManager.getConnection(databaseURL, user, password);
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(query);
    ) {
      ResultSetMetaData metadata = results.getMetaData();
      currentDatabaseFields.clear();
      table.getColumns().clear();
      System.out.printf("Retrieved %d columns:%n", metadata.getColumnCount());
      for (int i = 1; i <= metadata.getColumnCount(); i++) {
        final String columnName = metadata.getColumnName(i);
        System.out.println("column name: " + columnName);
        currentDatabaseFields.add(columnName);
        final TableColumn<Map<String, Object>, Object> col = new TableColumn<Map<String, Object>, Object>(columnName);
        col.setCellValueFactory(new Callback<CellDataFeatures<Map<String, Object>, Object>, ObservableValue<Object>>() {
          @Override
          public ObservableValue<Object> call(
              CellDataFeatures<Map<String, Object>, Object> cdf) {
            return new ReadOnlyObjectWrapper<Object>(cdf.getValue().get(columnName));
          }
        });
        table.getColumns().add(col);
      }
      table.getItems().clear();
      int rowCount = 0;
      while (results.next()) {
        rowCount++;
        Map<String, Object> rowData = new HashMap<String, Object>();
        for (String col : currentDatabaseFields) {
          rowData.put(col, results.getString(col));
        }
        table.getItems().add(rowData);
      }
      System.out.println("Retrieved " + rowCount + " rows");
    } catch (Exception exc) {
      handleError("Could not execute query " + query, exc);
    }
  }
 
  private VBox createQueryPane() {
    final VBox queryPane = new VBox(5);
    final HBox databasePane = new HBox(5);
    final TextField databaseTextField = new TextField();
    databaseTextField.setPromptText("Enter database URL here");
    databaseTextField.setPrefWidth(400);
    databaseTextField.setText("jdbc:mysql://aws.6p-milk.com:3306/pomodoro");
    databasePane.getChildren().addAll(new Label("Database: "), databaseTextField);
    final HBox usernamePane = new HBox(5);
    final TextField userTextField = new TextField();
    userTextField.setPrefWidth(400);
    userTextField.setText("pomodoro");
    usernamePane.getChildren().addAll(new Label("Username:"), userTextField);
    final HBox passwordPane = new HBox(5);
    final PasswordField passwordField = new PasswordField();
    passwordField.setPrefWidth(400);
    passwordField.setText(database.HandlePasswords.checkForPasswordFile());
    passwordPane.getChildren().addAll(new Label("Password: "), passwordField);
 
    final HBox sqlStatementPane = new HBox(5);
    final TextField queryTextField = new TextField();
    queryTextField.setPrefWidth(600);
    queryTextField.setPromptText("Enter SQL query here");
    queryTextField.setText("SELECT * FROM table1");
    final Button executeQueryButton = new Button("Execute Query");
    final TableView<Map<String, Object>> table = new TableView<Map<String, Object>>();
    sqlStatementPane.getChildren().addAll(new Label("SQL Query:"), queryTextField, executeQueryButton);
    queryPane.getChildren().addAll(databasePane, usernamePane, passwordPane, sqlStatementPane, table);
    VBox.setVgrow(table, Priority.ALWAYS);
    final EventHandler<ActionEvent> queryHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        populateTableFromQuery(databaseTextField.getText(),
            userTextField.getText(), passwordField.getText(),
            queryTextField.getText(), table);
      }
    };
    queryTextField.setOnAction(queryHandler);
    executeQueryButton.setOnAction(queryHandler);
 
    table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Map<String, Object>>() {
      @Override
      public void changed(
          ObservableValue<? extends Map<String, Object>> obs,
          Map<String, Object> oldValue, Map<String, Object> newValue) {
        if (newValue != null) {
          showSelectedRowDetails(newValue);
        }
      }
    });
    return queryPane;
  }
 
  private BorderPane createDriverManager() {
    final BorderPane driverManager = new BorderPane();
    final HBox driverManagerTop = new HBox(5);
    final TextField driverTextField = new TextField();
    driverTextField.setPromptText("Enter JDBC Driver Name");
    driverTextField.setPrefWidth(300);
    driverTextField.setText("com.mysql.jdbc.Driver");
    final Button driverLoadButton = new Button("Load");
    final ListView<Class<? extends Driver>> driverList = new ListView<Class<? extends Driver>>();
    EventHandler<ActionEvent> loadDriverHandler = new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          @SuppressWarnings("unchecked")
          Class<Driver> cls = (Class<Driver>) Class.forName(driverTextField
              .getText());
          driverList.getItems().add(cls);
        } catch (ClassNotFoundException e) {
          handleError("Could not find class " + driverTextField.getText(), e);
        } catch (ClassCastException e) {
          handleError("Class " + driverTextField.getText()+ " does not appear to be a Driver", e);
        }
      }
    };
    driverLoadButton.setOnAction(loadDriverHandler);
    driverTextField.setOnAction(loadDriverHandler);
    driverManagerTop.getChildren().addAll(new Label("JDBC Driver:"),
        driverTextField, driverLoadButton);
    driverManager.setTop(driverManagerTop);
    driverManager.setCenter(driverList);
    try {
        @SuppressWarnings("unchecked")
        Class<Driver> cls = (Class<Driver>) Class.forName(driverTextField
            .getText());
        driverList.getItems().add(cls);
      } catch (ClassNotFoundException e) {
        handleError("Could not find class " + driverTextField.getText(), e);
      } catch (ClassCastException e) {
        handleError("Class " + driverTextField.getText()+ " does not appear to be a Driver", e);
      }
    return driverManager;
  }
 
  private void handleError(String msg, Exception exc) {
    // TODO: show dialog box
    System.err.println(msg);
    exc.printStackTrace();
  }
 
  public static void main(String[] args) {
    launch(args);
  }
}