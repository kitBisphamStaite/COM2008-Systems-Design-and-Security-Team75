import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//SQL Packages
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class StaffOrderQueue extends JFrame {	
    public StaffOrderQueue() {
    	//Set Up Frame
        setTitle("Staff Order Queue");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //Create Header and Footer Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel footerPanel = new JPanel(new BorderLayout());
        
        //Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StaffDashboard staffDashboardScreen = new StaffDashboard();
                staffDashboardScreen.setLocationRelativeTo(null);
                staffDashboardScreen.setVisible(true);
                setVisible(false);
            }
        });

        //*Labels
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Staff Order Queue");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        //Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        
        //Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.CENTER);
        setVisible(true);
    }    
    
}