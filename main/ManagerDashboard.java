import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;

public class ManagerDashboard extends JFrame {	
    public ManagerDashboard() {
    	// Set Up Frame
        setTitle("Manager Dashboard");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create Header, Customer, Staff Panels
        JPanel headerPanel = new JPanel(new BorderLayout());
        JPanel customerListPanel = new JPanel(new BorderLayout());
        JPanel customerPanel = new JPanel(new BorderLayout());
        JPanel staffListPanel = new JPanel(new BorderLayout());
        JPanel staffPanel = new JPanel(new BorderLayout());
        
        // Create Header Panel Items
        //*Buttons
        JButton backButton = new JButton("Back"); //Take user back to Staff Dashboard Screen
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
        JLabel trainsOfSheffieldHeader = new JLabel("Trains Of Sheffield - Manager Dashboard");
        trainsOfSheffieldHeader.setHorizontalAlignment(JLabel.CENTER);
        // Add Items To Header Panel
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(trainsOfSheffieldHeader, BorderLayout.CENTER);
        
        // Create Customer Panel Items
        JLabel customerListLabel = new JLabel("Customer List:");
        JLabel selectedCustomerLabel = new JLabel("Selected User: ");
        JButton promoteSelectedUserButton = new JButton("Promote User");
        JList customerList = new JList();
        JScrollPane scrollableCustomerList = new JScrollPane(customerList);
        // Add Items to Customer Panel
        customerListPanel.add(customerListLabel, BorderLayout.NORTH);
        customerListPanel.add(scrollableCustomerList, BorderLayout.WEST);
        customerPanel.add(selectedCustomerLabel, BorderLayout.NORTH);
        customerPanel.add(promoteSelectedUserButton, BorderLayout.CENTER);
        
        // Create Staff Panel Items
        JLabel staffListLabel = new JLabel("Staff List:");
        JLabel selectedStaffLabel = new JLabel("Selected User: ");
        JButton demoteSelectedUserButton = new JButton("Demote User");
        JList staffList = new JList();
        JScrollPane scrollableStaffList = new JScrollPane(staffList);
        // Add Items to Staff Panel
        staffListPanel.add(staffListLabel, BorderLayout.NORTH);
        staffListPanel.add(scrollableStaffList, BorderLayout.WEST);
        staffPanel.add(selectedStaffLabel, BorderLayout.NORTH);
        staffPanel.add(demoteSelectedUserButton, BorderLayout.CENTER);

        
        
        

        // Add Panels To Frame
        add(headerPanel, BorderLayout.NORTH);
        
        add(customerListPanel, BorderLayout.CENTER);
        add(staffListPanel, BorderLayout.SOUTH);
        staffListPanel.add(staffPanel, BorderLayout.CENTER);
        customerListPanel.add(customerPanel, BorderLayout.CENTER);

    }    
    
}