import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddLocomotive extends JFrame{
    private ProductRecords parentScreen;
    private JTextArea productCodeTextArea = new JTextArea();
    private JTextArea productNameTextArea = new JTextArea();
    private JTextArea manufacturerNameTextArea = new JTextArea();
    private JTextArea retailPriceTextArea = new JTextArea();
    private JTextArea stockTextArea = new JTextArea();
    private JComboBox<Gauge> gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
    private JComboBox<Scale> scaleComboBox = new JComboBox<Scale>(Scale.values());
    private JTextArea eraCodeTextArea = new JTextArea();
    private JComboBox<ControlType> controlTypeComboBox = new JComboBox<ControlType>(ControlType.values());
    private boolean isEditing = false;

    public AddLocomotive(ProductRecords parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Locomotive");
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

        tempPanel1.add(new JLabel("productCodeTextArea (Should Start with a 'L'):"));
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
        tempPanel8.add(new JLabel("eraCodeTextArea:"));
        tempPanel8.add(eraCodeTextArea);
        tempPanel9.add(new JLabel("controlTypeComboBox:"));
        tempPanel9.add(controlTypeComboBox);
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
        String productCodeText = productCodeTextArea.getText().strip();
        Boolean validProductCode = ProductValidator.getInstance().validProductCode(productCodeText, ProductType.CONTROLLER);

        String productNameText = productNameTextArea.getText().strip();
        Boolean validProductName = ProductValidator.getInstance().validProductName(productNameText);

        String manufacturerNameText = manufacturerNameTextArea.getText().strip();
        boolean validManufacturerName = ProductValidator.getInstance().validManufacturerName(manufacturerNameText);

        String retailPriceText = retailPriceTextArea.getText().strip();
        boolean validRetailPrice = ProductValidator.getInstance().validRetailPrice(retailPriceText);

        String stockText = stockTextArea.getText().strip();
        boolean validStock = ProductValidator.getInstance().validStock(stockText);

        Boolean validGauge = ProductValidator.getInstance().validGauge((Gauge) gaugeComboBox.getSelectedItem());
        Boolean validScale = ProductValidator.getInstance().validScale((Scale) scaleComboBox.getSelectedItem());

        String eraCodeText = eraCodeTextArea.getText().strip();
        Boolean validEraCode = ProductValidator.getInstance().validProductName(eraCodeText);

        Boolean validControlType = ProductValidator.getInstance().validControlType((ControlType) controlTypeComboBox.getSelectedItem());

        if (!isEditing && validProductCode && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validEraCode && validControlType) {
            Inventory.getInstance().addProduct(new Locomotive(productCodeText, productNameText, manufacturerNameText, 
                                                Integer.parseInt(retailPriceText), Integer.parseInt(stockText), 
                                                (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), 
                                                eraCodeText, (ControlType) controlTypeComboBox.getSelectedItem()));
            parentScreen.setVisible(true);
            parentScreen.searchProducts();
            this.dispose();
        } else if (isEditing && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validEraCode && validControlType) {
            InventoryUpdate.getInstance().updateProduct(new Locomotive(productCodeText, productNameText, manufacturerNameText, 
                                                Integer.parseInt(retailPriceText), Integer.parseInt(stockText), 
                                                (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), 
                                                eraCodeText, (ControlType) controlTypeComboBox.getSelectedItem()));
            parentScreen.setVisible(true);
            parentScreen.searchProducts();
            this.dispose();
        }  
        if (!isEditing && !validProductCode){
            JOptionPane.showMessageDialog(null,
            "Invalid Produce code.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validProductName){
            JOptionPane.showMessageDialog(null,
            "Invalid Product Name.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validManufacturerName){
            JOptionPane.showMessageDialog(null,
            "Invalid Manufacturer Name.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validRetailPrice){
            JOptionPane.showMessageDialog(null,
            "Invalid Retail Price.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validStock){
            JOptionPane.showMessageDialog(null,
            "Invalid Stock.",
            "Incorrect Inpits",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validGauge){
            JOptionPane.showMessageDialog(null,
            "Invalid Gauge.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        } 
        if (!validScale){
            JOptionPane.showMessageDialog(null,
            "Invalid Scale.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }
        if (!validEraCode){
            JOptionPane.showMessageDialog(null,
            "Invalid Era Code.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }
        if (!validControlType){
            JOptionPane.showMessageDialog(null,
            "Invalid Control Type.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }


    }

    public void editProduct(Locomotive product){
        productCodeTextArea.setText(product.getProductCode());
        productCodeTextArea.setEditable(false);
        productNameTextArea.setText(product.getProductName());
        manufacturerNameTextArea.setText(product.getManufacturerName());
        retailPriceTextArea.setText(Integer.toString(product.getRetailPrice()));
        stockTextArea.setText(Integer.toString(product.getStock()));
        gaugeComboBox.setSelectedItem(product.getGauge());
        scaleComboBox.setSelectedItem(product.getScale());
        eraCodeTextArea.setText(product.getEraCode());
        controlTypeComboBox.setSelectedItem(product.getControlType());
        isEditing = true;
    }
}