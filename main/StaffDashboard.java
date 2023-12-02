import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        //JPanel productCategoryPanel = new JPanel(new GridLayout(3,2));
        JPanel productCategoryPanel = new JPanel(new GridLayout(2,1));
        //JPanel footerPanel = new JPanel(new GridLayout(0,1));
        
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


        JButton productRecordButton = new JButton("Product Records");
        productRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ProductRecords productRecords = new ProductRecords(true);
            	productRecords.setLocationRelativeTo(null);
            	productRecords.setVisible(true);
                setVisible(false);
                
            }
        });
        
        //Add Items to Product Category Panel
        //productCategoryPanel.add(viewLocomotivesCategoryButton);
        //productCategoryPanel.add(viewTrackCategoryButton);
        //productCategoryPanel.add(viewControllerCategoryButton);
        //productCategoryPanel.add(viewRollingStockCategoryButton);
        //productCategoryPanel.add(viewTrainSetsCategoryButton);
        //productCategoryPanel.add(viewTrackPacksCategoryButton);
        productCategoryPanel.add(productRecordButton);
        productCategoryPanel.add(viewOrderQueueButton);
        
        //Add Items to Footer Panel
        //footerPanel.add(viewOrderQueueButton);
        
        //Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(productCategoryPanel, BorderLayout.CENTER);
        //add(footerPanel, BorderLayout.SOUTH);
        setVisible(true);
    }  
}