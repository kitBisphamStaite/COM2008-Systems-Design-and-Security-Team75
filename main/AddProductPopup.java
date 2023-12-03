import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProductPopup extends JFrame {
    private AddTrainSet parentScreen;
    private JTextArea productCodeTextArea = new JTextArea();
    private JTextArea quantityTextArea = new JTextArea();
    private ProductType productType;

    public AddProductPopup(AddTrainSet parentScreenInit, ProductType productTypeInit) {
        this.parentScreen = parentScreenInit;
        this.productType = productTypeInit;
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

        if (productType == ProductType.LOCOMOTIVE){
            detailsPanel.add(new JLabel("productCodeTextArea (Product Code should start with 'L'):"));
        }

        if (productType == ProductType.ROLLINGSTOCK){
            detailsPanel.add(new JLabel("productCodeTextArea (Product Code should start with 'S'):"));
        }

        if (productType == ProductType.TRACKPACK){
            detailsPanel.add(new JLabel("productCodeTextArea (Product Code should start with 'P'):"));
        }

        if (productType == ProductType.TRACK){
            detailsPanel.add(new JLabel("productCodeTextArea (Product Code should start with 'P'):"));
        }

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
        Boolean validProduct = ProductValidator.getInstance().validProduct(productCode, productType);

        if (validQuantity && validProduct){
            int quantityFinal = Integer.parseInt(quantity);
            Product productFinal = Inventory.getInstance().getProduct(productCode);
            System.out.println("Valid Product and Code");
            parentScreen.addProductFromButton(new ProductPair(productFinal, quantityFinal));
            //InventoryUpdate.getInstance().updateProduct();
            goBack();
        }
        if (!validQuantity) {
            JOptionPane.showMessageDialog(null,"Invalid Quantity.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        }
        if (!validProduct) {
            JOptionPane.showMessageDialog(null,"Invalid Product code.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        }
    }
}
