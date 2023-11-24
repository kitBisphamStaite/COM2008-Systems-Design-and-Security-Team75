import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ProductPopup extends JFrame {

    private SelectedCategoryScreen parentScreen;
    private Product product;
    private JTextField orderNumber;
    public ProductPopup(Product product, SelectedCategoryScreen parentScreen) {
        this.product = product;
        this.parentScreen = parentScreen;

        setTitle("Product Details");
        setSize(300, 400);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Product Code: " + product.getProductCode());
        JButton addToOrderButton = new JButton("Add to Order");
        addToOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the product to the order (you can implement this logic)
                addToOrder();
            }
        });
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add the product to the order (you can implement this logic)
                goBack();
            }
        });
        orderNumber = new JTextField("0", 6);

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.add(nameLabel);
        detailsPanel.add(orderNumber);
        detailsPanel.add(addToOrderButton);
        detailsPanel.add(goBackButton);

        add(detailsPanel);
    }

    private void addToOrder() {
        int quantity = Integer.parseInt(orderNumber.getText());
        // Implement the logic to add the product to the order
       if (quantity > 0) {
            Basket.getInstance().addProduct(product, Integer.parseInt(orderNumber.getText()));
       }
       else{
            System.out.println("Invalid number, must be positive and not zero");
       }
        // Close the product details screen and show the product screen again
        parentScreen.setVisible(true);
        this.dispose();
    }

    private void goBack(){
        System.out.println("Go back to product screen");
        parentScreen.setVisible(true);
        this.dispose();
    }
}