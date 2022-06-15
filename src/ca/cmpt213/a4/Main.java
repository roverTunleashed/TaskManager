package ca.cmpt213.a4;
/**
 *  MVC Treasure Hunting Text Game 
 */

import ca.cmpt213.a4.control.TaskManager;
import ca.cmpt213.a4.view.UI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskManager();
            }
        });
    }
}
