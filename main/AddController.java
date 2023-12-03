import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddController extends JFrame{
    private ProductRecords parentScreen;
    private JTextArea productCodeTextArea = new JTextArea(); //Check it starts with "C"
    private JTextArea productNameTextArea = new JTextArea();
    private JTextArea manufacturerNameTextArea = new JTextArea();
    private JTextArea retailPriceTextArea = new JTextArea();
    private JTextArea stockTextArea = new JTextArea();
    private JComboBox<Gauge> gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
    private JComboBox<Scale> scaleComboBox = new JComboBox<Scale>(Scale.values());
    private JComboBox<ChipType> chipTypeComboBox = new JComboBox<ChipType>(ChipType.values());
    private boolean isEditing = false;

    public AddController(ProductRecords parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Controller");
        setSize(600, 600);
        setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel(new GridLayout(9, 1));
        JPanel tempPanel1 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel2 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel3 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel4 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel5 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel6 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel7 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel8 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel9 = new JPanel(new GridLayout(1, 2));

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });

        JButton addProducButton = new JButton("Confirm Product Details");
        addProducButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if is for editing then edit the product instead of adding it
                addProduct();
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
        tempPanel8.add(new JLabel("chipTypeComboBox:"));
        tempPanel8.add(chipTypeComboBox);
        tempPanel9.add(addProducButton);
        tempPanel9.add(goBackButton);
        detailsPanel.add(tempPanel1);
        detailsPanel.add(tempPanel2);
        detailsPanel.add(tempPanel3);
        detailsPanel.add(tempPanel4);
        detailsPanel.add(tempPanel5);
        detailsPanel.add(tempPanel6);
        detailsPanel.add(tempPanel7);
        detailsPanel.add(tempPanel8);
        detailsPanel.add(tempPanel9);
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
        Boolean validManufacturerName = ProductValidator.getInstance().validManufacturerName(manufacturerNameText);

        String retailPriceText = retailPriceTextArea.getText().strip();
        Boolean validRetailPrice = ProductValidator.getInstance().validRetailPrice(retailPriceText);

        String stockText = stockTextArea.getText().strip();
        Boolean validStock = ProductValidator.getInstance().validStock(stockText);

        Boolean validGauge = ProductValidator.getInstance().validGauge((Gauge) gaugeComboBox.getSelectedItem());
        Boolean validScale = ProductValidator.getInstance().validScale((Scale) scaleComboBox.getSelectedItem());
        Boolean validChipType = ProductValidator.getInstance().validChipType((ChipType) chipTypeComboBox.getSelectedItem());

        if (!isEditing && validProductCode && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validChipType) {
            Inventory.getInstance().addProduct(new Controller(productCodeText, productNameText, manufacturerNameText, Integer.parseInt(retailPriceText), Integer.parseInt(stockText), (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), (ChipType) chipTypeComboBox.getSelectedItem()));
            parentScreen.setVisible(true);
            parentScreen.searchProducts();
            this.dispose();
        } else if (isEditing && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validChipType) {
            InventoryUpdate.getInstance().updateProduct(new Controller(productCodeText, productNameText, manufacturerNameText, Integer.parseInt(retailPriceText), Integer.parseInt(stockText), (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), (ChipType) chipTypeComboBox.getSelectedItem()));
            parentScreen.setVisible(true);
            parentScreen.searchProducts();
            this.dispose();
        } 
        if (!isEditing && !validProductCode){
            JOptionPane.showMessageDialog(null,"Invalid Product code.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validProductName){
            JOptionPane.showMessageDialog(null,"Invalid Product Name.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validManufacturerName){
            JOptionPane.showMessageDialog(null,"Invalid Manufacturer Name.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validRetailPrice){
            JOptionPane.showMessageDialog(null,"Invalid Retail Price.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validStock){
            JOptionPane.showMessageDialog(null,"Invalid Stock.","Incorrect Inpits",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validGauge){
            JOptionPane.showMessageDialog(null,"Invalid Gauge.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validScale){
            JOptionPane.showMessageDialog(null,"Invalid Scale.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        } 
        if (!validChipType){
            JOptionPane.showMessageDialog(null,"Invalid Chip Type.","Incorrect Inputs",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void editProduct(Controller product){
        productCodeTextArea.setText(product.getProductCode());
        productCodeTextArea.setEditable(false);
        productNameTextArea.setText(product.getProductName());
        manufacturerNameTextArea.setText(product.getManufacturerName());
        retailPriceTextArea.setText(Integer.toString(product.getRetailPrice()));
        stockTextArea.setText(Integer.toString(product.getStock()));
        gaugeComboBox.setSelectedItem(product.getGauge());
        scaleComboBox.setSelectedItem(product.getScale());
        chipTypeComboBox.setSelectedItem(product.GetChipType());
        isEditing = true;
    }
}