import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTrack extends JFrame {
    private AddProduct parentScreen;

    public AddTrack(AddProduct parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Track");
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

        //Produce Code -- Check it has the right start letter "R"
        //Product Name
        //Manufacturer Name
        //Retail Price
        //Stock
        //Gauge
        //Scale
        //Curve radius
        //Track type
        
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
