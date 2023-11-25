import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffDashboard extends JFrame {	
    public StaffDashboard() {
    	// Set Up Frame
        setTitle("Staff Dashboard");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create Header and Footer Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel footerPanel = new JPanel(new GridLayout(0,2));
        
        // Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//Take user back to the main screen
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
        
        
        
        
        
        //*Labels
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Staff Dashboard");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        
        // Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        headerPanel.add(viewManagerDashboardButton, BorderLayout.EAST);
        
        // Create Footer Panel Items
        JButton viewProductRecordsButton = new JButton("Product Records");
        JButton viewOrderQueueButton = new JButton("Order Queue");
        
        // Add Items to Footer Panel
        footerPanel.add(viewProductRecordsButton);
        footerPanel.add(viewOrderQueueButton);
        
        // Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    
    public static void main(String[] args) {
        new StaffDashboard();
    }
    
    
    
    
    
    
    
}