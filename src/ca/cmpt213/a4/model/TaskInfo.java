package ca.cmpt213.a4.model;

/**
 * TaskInfo class holding the information of the task in private variables
 * Includes a setters,getters, toString and also a converter method from true/false to yes/no
 */
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskInfo
{
    //Private variables
    private final String name;
    private final String notes;
    private final GregorianCalendar dueDate;
    private boolean isComplete;
    //Constructor
    public TaskInfo(String name, String notes, GregorianCalendar dueDate, boolean isComplete) {
        this.name = name;
        this.notes = notes;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }
    //getters
    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }
    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }
    //setters
    public void setComplete(Boolean isComplete)
    {
        this.isComplete = isComplete;
    }
    //converter from stack over that uses the ternary operator to change the true/false into yes/no
    //https://stackoverflow.com/questions/2216527/how-to-write-a-method-that-translates-a-boolean-into-yes-or-no
    public String converter (boolean trueOrFalse)
    {
        return trueOrFalse ? "Yes" : "No";
    }
    //toString, had to add each dueDate individually in order to get the formatting given on the assignment
    @Override
    public String toString() {
        return  "Task: " + name + "\n" +
                "Notes: " + notes + "\n" +
                "Due date: " + dueDate.get(Calendar.YEAR) + "-" + dueDate.get(Calendar.MONTH)
                + "-" + dueDate.get(Calendar.DAY_OF_MONTH) + " " + dueDate.get(Calendar.HOUR_OF_DAY) + ":" +
                dueDate.get(Calendar.MINUTE) + "\n" +
                "Completed? " + converter(isComplete) + "\n";
    }
}

