import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTrackPack extends JFrame {
    private AddProduct parentScreen;
    JTextArea productCodeTextArea = new JTextArea(); //Check it starts with "P"
    JTextArea productNameTextArea = new JTextArea();
    JTextArea manufacturerNameTextArea = new JTextArea();
    JTextArea retailPriceTextArea = new JTextArea();
    JTextArea stockTextArea = new JTextArea();
    JComboBox<Gauge> gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
    JComboBox<Scale> scaleComboBox = new JComboBox<Scale>(Scale.values());
    JList<Product> trackProductCodeList = new JList<Product>(); //Check it starts with "P"
    private boolean isEditing = true;

    public AddTrackPack(AddProduct parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Track Pack");
        setSize(600, 600);
        setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel(new GridLayout(10, 1));
        JPanel tempPanel1 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel2 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel3 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel4 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel5 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel6 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel7 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel8 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel9 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel10 = new JPanel(new GridLayout(1, 2));

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

        JButton addTrackButton = new JButton("Add Track");
        addTrackButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Should open up a small screen with a box to enter a product Code (and a button to go back)
                //It should error if the product Code is wrong
            }   
        });
        
        JButton removeTrackButton = new JButton("Remove Track");
        removeTrackButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Removes currently selected item from the list
            }   
        });
        
        tempPanel1.add(new JLabel("productCodeTextArea:"));
        tempPanel1.add(productCodeTextArea);
        tempPanel2.add(new JLabel("productNameTextArea:"));
        tempPanel2.add(productNameTextArea);
        tempPanel3.add(new JLabel("manufacturerNameTextArea:"));
        tempPanel3.add(manufacturerNameTextArea);
        tempPanel4.add(new JLabel("retailPriceTextArea:"));
        tempPanel4.add(retailPriceTextArea);
        tempPanel5.add(new JLabel("stockTextArea:"));
        tempPanel5.add(stockTextArea);
        tempPanel6.add(new JLabel("guageComboBox:"));
        tempPanel6.add(gaugeComboBox);
        tempPanel7.add(new JLabel("scaleComboBox:"));
        tempPanel7.add(scaleComboBox);
        tempPanel8.add(new JLabel("trackProductCodeList:"));
        tempPanel8.add(trackProductCodeList);
        tempPanel9.add(addTrackButton);
        tempPanel9.add(removeTrackButton);
        tempPanel10.add(addProducButton);
        tempPanel10.add(goBackButton);
        detailsPanel.add(tempPanel1);
        detailsPanel.add(tempPanel2);
        detailsPanel.add(tempPanel3);
        detailsPanel.add(tempPanel4);
        detailsPanel.add(tempPanel5);
        detailsPanel.add(tempPanel6);
        detailsPanel.add(tempPanel7);
        detailsPanel.add(tempPanel8);
        detailsPanel.add(tempPanel9);
        detailsPanel.add(tempPanel10);

        add(detailsPanel);
    }

    private void goBack(){
        System.out.println("Go back to product screen");
        parentScreen.setVisible(true);
        this.dispose();
    }

    private void addProduct(){
        //Check that all field are full
        //Check that the product code isnt already in inventory, if it is then return an error of some sort
        System.out.println("Added Product");
        //Create a product object
        //Put the product into inventory
        parentScreen.setVisible(true);
        this.dispose();
    }

    public void editProduct(TrackPack product){
        productCodeTextArea.setText(product.getProductCode());
        productNameTextArea.setText(product.getProductName());
        manufacturerNameTextArea.setText(product.getManufacturerName());
        retailPriceTextArea.setText(Integer.toString(product.getRetailPrice()));
        retailPriceTextArea.setText(Integer.toString(product.getStock()));
        gaugeComboBox.setSelectedItem(product.getGauge());
        scaleComboBox.setSelectedItem(product.getScale());
        //trackProductCodeList make it make sence
        isEditing = false;
    }
}
