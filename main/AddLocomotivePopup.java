import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddLocomotivePopup extends JFrame {
    private AddTrainSet parentScreen;
    private JTextArea productCodeTextArea = new JTextArea();
    private JTextArea quantityTextArea = new JTextArea();

    public AddLocomotivePopup(AddTrainSet parentScreen) {
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
        Boolean validQuantity = ProductValidator.getInstance().validQuantity(quantityTextArea.getText());
        Boolean validProduct = ProductValidator.getInstance().validProduct(productCodeTextArea.getText(), ProductType.LOCOMOTIVE);
        if (validQuantity && validProduct){
            parentScreen.addLocomotiveFromButton(new ProductPair(Inventory.getInstance().getProduct(productCodeTextArea.getText()), Integer.parseInt(quantityTextArea.getText())));
            goBack();
        }
        else{
            System.out.println("Invalid Product Code");
        }
    }
}
