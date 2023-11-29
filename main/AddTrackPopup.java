import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTrackPopup extends JFrame {
    private AddTrackPack parentScreen;
    private JTextArea productCodeTextArea = new JTextArea();
    private JTextArea quantityTextArea = new JTextArea();

    public AddTrackPopup(AddTrackPack parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Train Set");
        setSize(600, 600);
        setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel(new GridLayout(6, 1));

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

        detailsPanel.add(new JLabel("productCodeTextArea (Product Code should start with 'L'):"));
        detailsPanel.add(productCodeTextArea);
        detailsPanel.add(new JLabel("quantityTextArea:"));
        detailsPanel.add(quantityTextArea);

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
        String quantity = quantityTextArea.getText().strip();
        Boolean validQuantity = ProductValidator.getInstance().validQuantity(quantity);

        String productCode = productCodeTextArea.getText().strip();
        Boolean validProduct = ProductValidator.getInstance().validProduct(productCode, ProductType.TRACK);

        if (validQuantity && validProduct){
            int quantityFinal = Integer.parseInt(quantity);
            Product productFinal = Inventory.getInstance().getProduct(productCode);
            System.out.println("Valid Product and Code");
            parentScreen.addTrackFromButton(new ProductPair(productFinal, quantityFinal));
            goBack();
        }
        else{
            System.out.println("Invalid Product Code and Quantity - AddTrackPopup");
        }
    }
}
