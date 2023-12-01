import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
//SQL Packages
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class StaffDashboard extends JFrame {	
    public StaffDashboard() {
    	//Set Up Frame
        setTitle("Staff Dashboard");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //Create Header and Footer Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel productCategoryPanel = new JPanel(new GridLayout(3,2));
        JPanel footerPanel = new JPanel(new GridLayout(0,1));
        
        //Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Take user back to the main screen - Sets the StaffDashboard frame to be invisible
                setVisible(false);
            }
        });
        JButton viewManagerDashboardButton = new JButton("Manager Dashboard");
        viewManagerDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerDashboard managerDashboardScreen = new ManagerDashboard();
                managerDashboardScreen.setLocationRelativeTo(null);
                managerDashboardScreen.setVisible(true);
                setVisible(false);
            }
        });
        //*HEADER LABELS
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Staff Dashboard");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        
        //Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        headerPanel.add(viewManagerDashboardButton, BorderLayout.EAST);
        
        //Create Footer Panel Items
        //*FOOTER BUTTONS
        JButton viewOrderQueueButton = new JButton("Pending Order Queue");
        viewOrderQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	PendingOrderQueue pendingOrderQueue = new PendingOrderQueue();
            	pendingOrderQueue.setLocationRelativeTo(null);
            	pendingOrderQueue.setVisible(true);
                setVisible(false);
            }
        });
        
        //Create Product Category Panel Items
        //*VIEW CATEGORY BUTTONS
        JButton viewLocomotivesCategoryButton = new JButton("Locomotives");
        viewLocomotivesCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditLocomotives editLocomotives = new EditLocomotives();
            	editLocomotives.setLocationRelativeTo(null);
            	editLocomotives.setVisible(true);
                setVisible(false);
            }
        });
        JButton viewTrackCategoryButton = new JButton("Tracks");
        viewTrackCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditTracks editTracks = new EditTracks();
            	editTracks.setLocationRelativeTo(null);
            	editTracks.setVisible(true);
                setVisible(false);
            }
        });
        JButton viewControllerCategoryButton = new JButton("Controllers");
        viewControllerCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditControllers editControllers = new EditControllers();
            	editControllers.setLocationRelativeTo(null);
            	editControllers.setVisible(true);
                setVisible(false);
            }
        });
        JButton viewRollingStockCategoryButton = new JButton("Rolling Stock");
        viewRollingStockCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditRollingStock editRollingStock = new EditRollingStock();
            	editRollingStock.setLocationRelativeTo(null);
            	editRollingStock.setVisible(true);
            	setVisible(false);
            }
        });
        JButton viewTrainSetsCategoryButton = new JButton("Train Sets");
        viewTrainSetsCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditTrainSets editTrainSets = new EditTrainSets();
            	editTrainSets.setLocationRelativeTo(null);
            	editTrainSets.setVisible(true);
                setVisible(false);
            }
        });
        JButton viewTrackPacksCategoryButton = new JButton("Track Packs");
        viewTrackPacksCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	EditTrackPacks editTrackPacks = new EditTrackPacks();
            	editTrackPacks.setLocationRelativeTo(null);
            	editTrackPacks.setVisible(true);
                setVisible(false);
            }
        });
        
        //Add Items to Product Category Panel
        productCategoryPanel.add(viewLocomotivesCategoryButton);
        productCategoryPanel.add(viewTrackCategoryButton);
        productCategoryPanel.add(viewControllerCategoryButton);
        productCategoryPanel.add(viewRollingStockCategoryButton);
        productCategoryPanel.add(viewTrainSetsCategoryButton);
        productCategoryPanel.add(viewTrackPacksCategoryButton);
        
        
        
        //Add Items to Footer Panel
        footerPanel.add(viewOrderQueueButton);
        
        //Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(productCategoryPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        setVisible(true);
    }  
}