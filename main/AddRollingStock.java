import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddRollingStock extends JFrame {
    private AddProduct parentScreen;

    public AddRollingStock(AddProduct parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Rolling Stock");
        setSize(300, 400);
        setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        JButton addProducButton = new JButton("Add Product");
        addProducButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }        
        });

        //Produce Code -- Check it has the right start letter "S"
        //Product Name
        //Manufacturer Name
        //Retail Price
        //Stock
        //Gauge
        //Scale
        //Era Code

        detailsPanel.add(addProducButton);
        detailsPanel.add(goBackButton);
        add(detailsPanel);
    }

    private void goBack(){
        System.out.println("Go back to product screen");
        parentScreen.setVisible(true);
        this.dispose();
    }

    private void addProduct(){
        System.out.println("Added Product");
        parentScreen.setVisible(true);
        this.dispose();
    }
    //Can add and Edit
}
