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
 public class WillyTask {
        private StringProperty taskName;
        private IntegerProperty completedPoms;
        private LongProperty dateStartedMilis;
        /** The unique id that sql gives this row*/
        private int uid;

        /**
         * Constructor for when this is a new task which doesnt come from the DB
         * @param fName the name of the task
         */
        public WillyTask(String fName) {
            this.taskName = new SimpleStringProperty(fName);
            this.completedPoms = new SimpleIntegerProperty(0);
            this.dateStartedMilis = new SimpleLongProperty(System.currentTimeMillis());
        }
        /**
         * Constructor for when we are populating the table from the database
         * @param fName
         * @param completedPoms
         * @param dateStarted
         */
        public WillyTask(String fName, int completedPoms, long dateStarted, int uid) {
            this.taskName = new SimpleStringProperty(fName);
            this.completedPoms = new SimpleIntegerProperty(completedPoms);
            this.dateStartedMilis = new SimpleLongProperty(dateStarted);
            this.uid = uid;
        }
        
        public StringProperty taskNameProperty() { return taskName; }
        public IntegerProperty completedPomsProperty() { return completedPoms; }
        public StringProperty dateStartedMilisProperty() { return new SimpleStringProperty(Functions.turnDateInMillisToString(dateStartedMilis.longValue())); }
		/**
		 * @return the uid
		 */
		public int getUid() {
			return uid;
		}
		/**
		 * @param uid the uid to set
		 */
		public void setUid(int uid) {
			this.uid = uid;
		}
        
 }
 
 

  