package ca.cmpt213.a4.control;
/**
 * Task Manager class that stores all the tasks in an Array list, performs operations on the task-list
 * and reads/write to a Json file.
 */

import ca.cmpt213.a4.model.TaskInfo;
import ca.cmpt213.a4.view.UI;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class TaskManager {
    List<TaskInfo> tasks = new ArrayList<>();
    UI UI = new UI();

    public TaskManager() {
        //Action Listeners
        //Anonymous class used here -> overwrite the current Action Listener for a 1 time thing
        UI.getAddTaskB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.getAddTaskF().setVisible(true); //set the add task frame to be visible
            }
        });
        UI.getAllTaskB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listAllTasks();
            }
        });
        UI.getCreateB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTasks();
            }
        });
        UI.getOverdueTaskB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.getCenterPanel().removeAll();
                List<TaskInfo> tempTaskInfo = new ArrayList<>();
                for (int i = 0; i < tasks.size(); i++) {
                    if (!tasks.get(i).isComplete())//find all incomplete tasks
                    {
                        tempTaskInfo.add(tasks.get(i));
                    }
                }
                if (tempTaskInfo.size() == 0) //no overdue incomplete tasks to show
                {
                    JOptionPane.showMessageDialog(null, "No overdue incomplete tasks to show");
                } else {
                    for (int i = 1; i <= tempTaskInfo.size(); i++) {
                        LocalDateTime currentTime = LocalDateTime.now();
                        LocalDateTime timeOfTask = tasks.get(i - 1).getDueDate().toZonedDateTime().toLocalDateTime();
                        if (timeOfTask.isBefore(currentTime) && !tasks.get(i - 1).isComplete()) {
                            Border blackLine = BorderFactory.createLineBorder(Color.black);
                            JPanel taskOverDuePanel = new JPanel();
                            taskOverDuePanel.setBorder(blackLine);
                            taskOverDuePanel.setPreferredSize(new Dimension(300, 180));
                            taskOverDuePanel.setLayout(null);
                            taskOverDuePanel.setBackground(Color.WHITE);
                            JLabel taskNumL = new JLabel("");
                            taskNumL.setBounds(10, 10, 200, 25);
                            taskOverDuePanel.add(taskNumL);
                            JLabel taskNameL = new JLabel("");
                            taskNameL.setBounds(10, 40, 200, 25);
                            taskOverDuePanel.add(taskNameL);
                            JLabel taskNotesL = new JLabel("");
                            taskNotesL.setBounds(10, 70, 200, 25);
                            taskOverDuePanel.add(taskNotesL);
                            JLabel dueDateL = new JLabel("");
                            dueDateL.setBounds(10, 100, 200, 25);
                            taskOverDuePanel.add(dueDateL);
                            taskNumL.setText("Task: " + i);
                            taskNameL.setText("Name: " + tempTaskInfo.get(i - 1).getName());
                            taskNotesL.setText("Notes: " + tempTaskInfo.get(i - 1).getNotes());
                            int year = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.YEAR);
                            int month = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.MONTH) + 1;
                            int day = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.DAY_OF_MONTH);
                            int hour = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.HOUR_OF_DAY);
                            int minute = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.MINUTE);
                            //Convert to string to get the appropriate format in the GUI (YYYY-MM-DD HH:MM)
                            //https://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java
                            String formattedYear = String.format("%04d", year);
                            String formattedMonth = String.format("%02d", month);
                            String formattedDay = String.format("%02d", day);
                            String formattedHour = String.format("%02d", hour);
                            String formattedMinute = String.format("%02d", minute);
                            dueDateL.setText("Due date: " + formattedYear + "-" + formattedMonth + "-" + formattedDay
                                    + " " + formattedHour + ":" + formattedMinute);
                            UI.getCenterPanel().add(taskOverDuePanel);
                        }
                    }
                }
                UI.getScrollBar().revalidate();
                UI.getScrollBar().repaint();
            }
        });
        UI.getUpcomingTaskB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.getCenterPanel().removeAll();
                List<TaskInfo> tempTaskInfo = new ArrayList<>();
                for (TaskInfo task : tasks) {
                    //https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
                    //https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/GregorianCalendar.html
                    //Uses local date time to the get the current time and then comparing the task time to see if it occurs
                    //after the current time.
                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime timeOfTask = task.getDueDate().toZonedDateTime().toLocalDateTime();
                    if (timeOfTask.isAfter(currentTime) && (!task.isComplete())) {
                        tempTaskInfo.add(task);//add it to the temporary task info
                    }
                }
                if (tempTaskInfo.size() == 0) //if its empty there is no incomplete tasks to show
                {
                    JOptionPane.showMessageDialog(null, "No upcoming incomplete tasks to show.");
                } else {
                    for (int i = 1; i <= tempTaskInfo.size(); i++) {
                        Border blackLine = BorderFactory.createLineBorder(Color.black);
                        JPanel taskUpcomingPanel = new JPanel();
                        taskUpcomingPanel.setBorder(blackLine);
                        taskUpcomingPanel.setPreferredSize(new Dimension(300, 180));
                        taskUpcomingPanel.setLayout(null);
                        taskUpcomingPanel.setBackground(Color.WHITE);
                        JLabel taskNumL = new JLabel("");
                        taskNumL.setBounds(10, 10, 200, 25);
                        taskUpcomingPanel.add(taskNumL);
                        JLabel taskNameL = new JLabel("");
                        taskNameL.setBounds(10, 40, 200, 25);
                        taskUpcomingPanel.add(taskNameL);
                        JLabel taskNotesL = new JLabel("");
                        taskNotesL.setBounds(10, 70, 200, 25);
                        taskUpcomingPanel.add(taskNotesL);
                        JLabel dueDateL = new JLabel("");
                        dueDateL.setBounds(10, 100, 200, 25);
                        taskUpcomingPanel.add(dueDateL);
                        taskNumL.setText("Task: " + i);
                        taskNameL.setText("Name: " + tempTaskInfo.get(i - 1).getName());
                        taskNotesL.setText("Notes: " + tempTaskInfo.get(i - 1).getNotes());
                        int year = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.YEAR);
                        int month = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.MONTH) + 1;
                        int day = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.DAY_OF_MONTH);
                        int hour = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.HOUR_OF_DAY);
                        int minute = tempTaskInfo.get(i - 1).getDueDate().get(Calendar.MINUTE);
                        //Convert to string to get the appropriate format in the GUI (YYYY-MM-DD HH:MM)
                        //https://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java
                        String formattedYear = String.format("%04d", year);
                        String formattedMonth = String.format("%02d", month);
                        String formattedDay = String.format("%02d", day);
                        String formattedHour = String.format("%02d", hour);
                        String formattedMinute = String.format("%02d", minute);
                        dueDateL.setText("Due date: " + formattedYear + "-" + formattedMonth + "-" + formattedDay
                                + " " + formattedHour + ":" + formattedMinute);
                        UI.getCenterPanel().add(taskUpcomingPanel);
                    }
                }
                UI.getScrollBar().revalidate();
                UI.getScrollBar().repaint();
            }
        });
        UI.getMainF().addWindowListener(new WindowListener() {
            @Override
            //read from the file when the main frame is opened
            public void windowOpened(WindowEvent e) {

                File file = new File(System.getProperty("user.dir") + "/src/tasks.json");
                if (file.length() == 0) {
                    //do nothing
                } else {
                    read(file);
                }
            }

            @Override
            //write to the file when the main frame is closed
            public void windowClosing(WindowEvent e) {
                String fileDirectory = System.getProperty("user.dir");
                String tasks = fileDirectory + "/src/tasks.json";
                write(tasks);
                UI.getMainF().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        UI.getGenerateB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //https://stackoverflow.com/questions/2586975/how-to-use-curl-in-java
                //https://docs.oracle.com/javase/6/docs/api/java/net/URL.html
                //generate task makes use of java.net.URL
                URL url = null;
                try {
                    //URL represents a pointer to the border api
                    url = new URL("https://www.boredapi.com/api/activity");
                } catch (MalformedURLException malformedURLException) {
                    //IDE suggested exception case
                    malformedURLException.printStackTrace();
                }
                try {
                    assert url != null;
                    try (BufferedReader readFile = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                        JsonElement tasks = JsonParser.parseReader(readFile);
                        JsonObject objects = tasks.getAsJsonObject();
                        String taskName = objects.get("activity").getAsString();//get the generated name
                        String notesT = objects.get("type").getAsString();
                        String notesPa = objects.get("participants").getAsString();
                        String notesPr = objects.get("price").getAsString();
                        String notes = "type: " + notesT + ", " + "participants: " + notesPa + ", " + "price: " + notesPr;
                        UI.getNameTF().setText(taskName);
                        UI.getNotesTF().setText(notes);

                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        UI.getCancelB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.getAddTaskF().setVisible(false);
            }
        });
        UI.getCreditsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "This program is created by Trevor", "Credits", JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    public void listAllTasks() {
        if (tasks.size() == 0) {
            JOptionPane.showMessageDialog(null, "No tasks to show");
        }
        UI.getCenterPanel().removeAll();
        for (int i = 1; i <= tasks.size(); i++) {
            /* Can use collection.sort to sort by date
            Collections.sort(tasks, new Comparator<TaskInfo>() {
                @Override
                public int compare(TaskInfo o1, TaskInfo o2) {
                    if (o1.getDueDate().toZonedDateTime().isAfter(o2.getDueDate().toZonedDateTime()))
                    {
                        return o2.getDueDate().toZonedDateTime().getDayOfMonth();
                    }
                    return o1.getDueDate().toZonedDateTime().getDayOfMonth();
                }
            });
             */
            //Make a new panel everytime
            //https://www.tutorialspoint.com/swingexamples/add_border_to_panel.htm
            //This is how the black border is generated on each panel
            Border blackLine = BorderFactory.createLineBorder(Color.black);
            JPanel taskPanel = new JPanel();
            taskPanel.setBorder(blackLine);
            taskPanel.setPreferredSize(new Dimension(300, 180));
            taskPanel.setLayout(null);
            taskPanel.setBackground(Color.WHITE);
            JLabel taskNumL = new JLabel("");
            taskNumL.setBounds(10, 10, 200, 25);
            taskPanel.add(taskNumL);
            JLabel taskNameL = new JLabel("");
            taskNameL.setBounds(10, 40, 300, 25);
            taskPanel.add(taskNameL);
            JLabel taskNotesL = new JLabel("");
            taskNotesL.setBounds(10, 70, 300, 25);
            taskPanel.add(taskNotesL);
            JLabel dueDateL = new JLabel("");
            dueDateL.setBounds(10, 100, 300, 25);
            taskPanel.add(dueDateL);
            JCheckBox isComplete = new JCheckBox("Completed");
            isComplete.setBounds(10, 130, 100, 25);
            taskPanel.add(isComplete);
            JButton removeTaskB = new JButton();
            removeTaskB.setBounds(150, 130, 130, 25);
            taskPanel.add(removeTaskB);
            taskNumL.setText("Task: " + i);
            taskNameL.setText("Name: " + tasks.get(i - 1).getName());
            taskNotesL.setText("Notes: " + tasks.get(i - 1).getNotes());
            int year = tasks.get(i - 1).getDueDate().get(Calendar.YEAR);
            int month = tasks.get(i - 1).getDueDate().get(Calendar.MONTH) + 1;
            int day = tasks.get(i - 1).getDueDate().get(Calendar.DAY_OF_MONTH);
            int hour = tasks.get(i - 1).getDueDate().get(Calendar.HOUR_OF_DAY);
            int minute = tasks.get(i - 1).getDueDate().get(Calendar.MINUTE);
            //Convert to string to get the appropriate format in the GUI (YYYY-MM-DD HH:MM)
            //https://stackoverflow.com/questions/275711/add-leading-zeroes-to-number-in-java
            //String[] months = {"Jan", "Feb", "Mar", "Apr"};
            String formattedYear = String.format("%04d", year);
            String formattedMonth = String.format("%02d", month);
            String formattedDay = String.format("%02d", day);
            String formattedHour = String.format("%02d", hour);
            String formattedMinute = String.format("%02d", minute);
            dueDateL.setText("Due date: " + formattedYear + "-" + formattedMonth + "-" + formattedDay
                    + " " + formattedHour + ":" + formattedMinute);
            if (tasks.get(i - 1).isComplete()) {
                isComplete.setSelected(true);
            } else {
                isComplete.setSelected(false);
            }
            removeTaskB.setText("Remove Task ");
            //Add to the center panel
            UI.getCenterPanel().add(taskPanel);
            //Removing a task
            int index = i - 1;
            removeTaskB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tasks.remove(index);
                    UI.getScrollBar().revalidate();
                    UI.getScrollBar().repaint();
                }
            });
            isComplete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tasks.get(index).setComplete(true);
                    if (!isComplete.isSelected()) {
                        tasks.get(index).setComplete(false);
                    }
                    UI.getScrollBar().revalidate();
                    UI.getScrollBar().repaint();
                }
            });

        }
        UI.getScrollBar().revalidate();
        UI.getScrollBar().repaint();
    }

    public void addTasks() {
        String name = UI.getNameTF().getText();
        String notes = UI.getNotesTF().getText();
        //Date
        String dateString = (UI.getDate().getDateStringOrEmptyString());
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Error: Task Name should not be empty");
        } else if (dateString.length() != 0) {
            //Getting the substrings of dateString and converting it to ints
            String yearString = dateString.substring(0, 4);
            int dueDateYear = Integer.parseInt(yearString);
            String monthString = dateString.substring(5, 7);
            int dueDateMonth = Integer.parseInt(monthString);
            String dayString = dateString.substring(8, 10);
            int dueDateDay = Integer.parseInt(dayString);

            //Time
            String timeString = (UI.getTime().toString());
            String hourString = timeString.substring(0, 2);
            int dueDateHour = Integer.parseInt(hourString);
            String minuteString = timeString.substring(3, 5);
            int dueDateMinute = Integer.parseInt(minuteString);
            GregorianCalendar newCalendar = new GregorianCalendar(dueDateYear, dueDateMonth - 1, dueDateDay, dueDateHour, dueDateMinute);
            TaskInfo newTask = new TaskInfo(name, notes, newCalendar, false);
            tasks.add(newTask);
            UI.getAddTaskF().setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Error: Due Date should not be empty");
        }

    }

    private void read(File inputFile) {
        FileReader file;
        try {
            file = new FileReader(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JsonElement tasks = JsonParser.parseReader(file);
        JsonArray objects = tasks.getAsJsonObject().getAsJsonArray("tasks");
        //for each the task in the JsonArray
        for (JsonElement task : objects) {
            JsonObject jsonTask = task.getAsJsonObject();
            //Get the name associated with the task name and place it in a String
            String taskName = jsonTask.get("taskName").getAsString();
            //Following similarly for the other task information
            String taskNotes = jsonTask.get("taskNotes").getAsString();
            GregorianCalendar dueDate = new GregorianCalendar();
            boolean isComplete = jsonTask.get("taskCompleted").getAsBoolean();
            dueDate.set(Calendar.YEAR, jsonTask.get("taskYear").getAsInt());
            dueDate.set(Calendar.MONTH, jsonTask.get("taskMonth").getAsInt());
            dueDate.set(Calendar.DAY_OF_MONTH, jsonTask.get("taskDay").getAsInt());
            dueDate.set(Calendar.HOUR_OF_DAY, jsonTask.get("taskHour").getAsInt());
            dueDate.set(Calendar.MINUTE, jsonTask.get("taskMinute").getAsInt());
            //Add the information to a temporary task info when done
            TaskInfo tempTaskInfo = new TaskInfo(taskName, taskNotes, dueDate, isComplete);
            this.tasks.add(tempTaskInfo);

        }

    }

    public void write(String file) {
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.beginObject();
            writer.name("tasks");
            writer.beginArray();
            //for all the tasks in the task list
            for (int i = 0; i < tasks.size(); i++) {
                writer.beginObject();
                //write to the title "taskName", "taskNotes"....
                writer.name("taskName").value(tasks.get(i).getName());
                writer.name("taskNotes").value(tasks.get(i).getNotes());
                writer.name("taskYear").value(tasks.get(i).getDueDate().get(Calendar.YEAR));
                writer.name("taskMonth").value(tasks.get(i).getDueDate().get(Calendar.MONTH));
                writer.name("taskDay").value(tasks.get(i).getDueDate().get(Calendar.DAY_OF_MONTH));
                writer.name("taskHour").value(tasks.get(i).getDueDate().get(Calendar.HOUR_OF_DAY));
                writer.name("taskMinute").value(tasks.get(i).getDueDate().get(Calendar.MINUTE));
                writer.name("taskCompleted").value(tasks.get(i).isComplete());
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
