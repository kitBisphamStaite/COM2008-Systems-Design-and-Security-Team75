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

public class Home extends JFrame {	
    public Home() {
    	String accountType = Login.getUserType();
    	
    	//Set Up Frame
        setTitle("Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        //Create Header and Footer Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel footerPanel = new JPanel(new GridLayout(0,2));
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        //Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("LogOut");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Take user back to the main screen - Sets the StaffDashboard frame to be invisible
            	dispose();
            	Login.main(null);
            }
        });
        
        JButton staffDashboard = new JButton("StaffDashboard");
        staffDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            	StaffDashboard staffDashboardScreen = new StaffDashboard();
            	staffDashboardScreen.setLocationRelativeTo(null);
            	staffDashboardScreen.setVisible(true);
            }
        });
        
        JButton accountSettings = new JButton("Account Settings");
        accountSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            	EditAccountPage.main(null);
            }
        });
        
        
        JButton basket = new JButton("Basket");
        basket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            	ViewBasket.main(null);
            }
        });
        
        JButton productSearch = new JButton("Product Search");
        productSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            	new ProductRecords(!Login.getUserType().equals("CUSTOMER"));
            	
            }
        });
        
        JButton pastOrders = new JButton("Past Orders");
        pastOrders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            	new PastOrders();
            	
            }
        });
        
        //*HEADER LABELS
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Home");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        
        //Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        
        //Add Items To Main Panel
        System.out.println(accountType);
        if (accountType.equals("CUSTOMER")) {
        	mainPanel.add(basket, BorderLayout.EAST);
        	mainPanel.add(pastOrders, BorderLayout.NORTH);
        }else {
        	mainPanel.add(staffDashboard, BorderLayout.EAST);
        }
        mainPanel.add(productSearch, BorderLayout.WEST);
        mainPanel.add(accountSettings, BorderLayout.SOUTH);
        
        //Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    
    public static void main(String[] args) {
    	//Database Details
        String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
        String usernameDB = "team075";
        String passwordDB = "mood6Phah";
        //Try To Establish Connection With DB
        try {
            Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB); 
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error in connecting to the database");
        }
        new Home();
    }   
}