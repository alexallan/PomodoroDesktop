package objects;

import utils.Functions;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Task object which holds details and records stats about a specific task
 * @author Alex
 *
 */
 public class Task {
        private StringProperty taskName;
        private IntegerProperty completedPoms;
        private LongProperty dateStartedMilis;

        public Task(String fName) {
            this.taskName = new SimpleStringProperty(fName);
            this.completedPoms = new SimpleIntegerProperty(0);
            this.dateStartedMilis = new SimpleLongProperty(System.currentTimeMillis());
        }
        
        public StringProperty taskNameProperty() { return taskName; }
        public IntegerProperty completedPomsProperty() { return completedPoms; }
        public StringProperty dateStartedMilisProperty() { return new SimpleStringProperty(Functions.turnDateInMillisToString(dateStartedMilis.longValue())); }
    }
 
 

  