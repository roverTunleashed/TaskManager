package ca.cmpt213.a4.view;
/**
 * User Interface class that displays the user interface using Java swing elements to construct the interface
 */

import ca.cmpt213.a4.control.TaskManager;
import com.github.lgooddatepicker.components.CalendarPanel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.zinternaltools.YearMonthChangeEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;

public class UI {
    public JFrame getMainF() {
        return mainF;
    }

    //Home page
    JFrame mainF;
    JFrame addTaskF;
    JPanel topPanel;
    JPanel botPanel;
    JPanel centerPanel;
    JButton allTaskB;
    JButton overdueTaskB;
    JButton upcomingTaskB;
    JButton addTaskB;
    //Add Task Page
    JButton createB;
    JTextField nameTF;
    JTextField notesTF;
    DatePicker date;
    TimePicker time;
    //Display all tasks
    JLabel taskNumL;
    JLabel taskNotesL;
    JLabel taskNameL;
    JLabel dueDateL;
    JCheckBox isComplete;
    JButton removeTaskB;
    JButton generateB;
    JButton cancelB;
    //ScrollPane
    JScrollPane scrollBar;
    //Quiz
    JButton creditsButton;

    public JButton getCreditsButton() {
        return creditsButton;
    }


    public JButton getCancelB() {
        return cancelB;
    }

    public JButton getGenerateB() {
        return generateB;
    }

    public TimePicker getTime() {
        return time;
    }

    public DatePicker getDate() {
        return date;
    }

    public JScrollPane getScrollBar() {
        return scrollBar;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public JTextField getNotesTF() {
        return notesTF;
    }

    public JTextField getNameTF() {
        return nameTF;
    }

    public JButton getCreateB() {
        return createB;
    }

    public JFrame getAddTaskF() {
        return addTaskF;
    }

    public JButton getAddTaskB() {
        return addTaskB;
    }

    public JButton getUpcomingTaskB() {
        return upcomingTaskB;
    }

    public JButton getOverdueTaskB() {
        return overdueTaskB;
    }

    public JButton getAllTaskB() {
        return allTaskB;
    }

    //locations for Swing components were inspired from https://beginnersbook.com/2015/07/java-swing-tutorial/ and then done to my liking
    public UI() {
        //Main Frame
        mainF = new JFrame("My To-Do list");
        mainF.setSize(400, 300);
        mainF.setLayout(new BorderLayout());
        mainF.setLocationRelativeTo(null); //keeps the frame in the center
        mainF.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Panels that associate to the border layout
        topPanel = new JPanel();
        botPanel = new JPanel();
        //Adding the buttons to the panels
        allTaskB = new JButton("All");
        allTaskB.setBounds(40, 20, 50, 25);
        topPanel.add(allTaskB);
        overdueTaskB = new JButton("Overdue");
        overdueTaskB.setBounds(90, 20, 135, 25);
        topPanel.add(overdueTaskB);
        upcomingTaskB = new JButton("Upcoming");
        upcomingTaskB.setBounds(225, 20, 135, 25);
        topPanel.add(upcomingTaskB);
        addTaskB = new JButton("Add Task");
        addTaskB.setBounds(150, 300, 100, 25);
        botPanel.add(addTaskB);
        creditsButton = new JButton("Credits");
        creditsButton.setBounds(200, 300, 100, 25);
        botPanel.add(creditsButton);
        //Add task frame
        addTaskF = new JFrame("Add Task");
        addTaskF.setSize(350, 250);
        addTaskF.setLocationRelativeTo(null);
        //Add task panel that adds to the frame
        JPanel addTaskPanel = new JPanel();
        addTaskPanel.setPreferredSize(new Dimension(350, 250));
        addTaskPanel.setLayout(null);
        JLabel nameL = new JLabel("Name:");
        nameL.setBounds(10, 20, 80, 25);
        addTaskPanel.add(nameL);
        nameTF = new JTextField();
        nameTF.setBounds(100, 20, 165, 25);
        addTaskPanel.add(nameTF);
        JLabel notesL = new JLabel("Notes:");
        notesL.setBounds(10, 50, 80, 25);
        addTaskPanel.add(notesL);
        notesTF = new JTextField();
        notesTF.setBounds(100, 50, 165, 25);
        addTaskPanel.add(notesTF);
        JLabel dueDateY = new JLabel("Due Date: ");
        dueDateY.setBounds(10, 80, 80, 25);
        addTaskPanel.add(dueDateY);
        //Date and Time Picker
        date = new DatePicker();
        date.setBounds(100, 80, 200, 25);
        addTaskPanel.add(date);
        time = new TimePicker();
        time.setBounds(100, 110, 100, 25);
        addTaskPanel.add(time);
        //Buttons in the Add task panel
        createB = new JButton("Create");
        createB.setBounds(150, 180, 80, 25);
        addTaskPanel.add(createB);
        cancelB = new JButton("Cancel");
        cancelB.setBounds(230, 180, 80, 25);
        addTaskPanel.add(cancelB);
        generateB = new JButton("Generate");
        generateB.setBounds(20, 180, 100, 25);
        addTaskPanel.add(generateB);
        addTaskF.add(addTaskPanel);
        //Task Info scroll panel
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 1));
        centerPanel.setBackground(Color.WHITE);
        //Task information -> creating buttons and setting their bounds
        taskNumL = new JLabel("");
        taskNumL.setBounds(10, 10, 200, 25);
        taskNameL = new JLabel("");
        taskNameL.setBounds(10, 40, 200, 25);
        taskNotesL = new JLabel("");
        taskNotesL.setBounds(10, 70, 200, 25);
        dueDateL = new JLabel("");
        dueDateL.setBounds(10, 100, 200, 25);
        isComplete = new JCheckBox("");
        isComplete.setBounds(10, 130, 100, 25);
        removeTaskB = new JButton();
        removeTaskB.setBounds(150, 130, 130, 25);
        //ScrollBar
        scrollBar = new JScrollPane(centerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainF.add(scrollBar, BorderLayout.CENTER);
        mainF.add(topPanel, BorderLayout.NORTH);
        mainF.add(botPanel, BorderLayout.SOUTH);
        mainF.setVisible(true);
    }

}
