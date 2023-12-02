import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddTrainSet extends JFrame {
    private ProductRecords parentScreen;
    private JTextArea productCodeTextArea = new JTextArea();
    private JTextArea productNameTextArea = new JTextArea();
    private JTextArea manufacturerNameTextArea = new JTextArea();
    private JTextArea retailPriceTextArea = new JTextArea();
    private JTextArea stockTextArea = new JTextArea();
    private JComboBox<Gauge> gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
    private JComboBox<Scale> scaleComboBox = new JComboBox<Scale>(Scale.values());
    private JTextArea eraCodeTextArea = new JTextArea();
    private JTextArea controllerProductCodeTextArea = new JTextArea();
    private ArrayList<ProductPair> locomotiveList;
    private DefaultListModel<ProductPair> locomotiveListModel;
    private JList<ProductPair> locomotiveListUI;
    private ArrayList<ProductPair> rollingStockList;
    private DefaultListModel<ProductPair> rollingStockListModel;
    private JList<ProductPair> rollingStockListUI;
    private ArrayList<ProductPair> trackPackList;
    private DefaultListModel<ProductPair> trackPackListModel;
    private JList<ProductPair> trackPackListUI;
    private boolean isEditing = false;

    public AddTrainSet(ProductRecords parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Train Set");
        setSize(600, 600);
        setLocationRelativeTo(null);

        // Sample product data
        locomotiveList = new ArrayList<>();
        rollingStockList = new ArrayList<>();
        trackPackList = new ArrayList<>();

        //making an initial product list
        locomotiveListModel = new DefaultListModel<>();
        rollingStockListModel = new DefaultListModel<>();
        trackPackListModel = new DefaultListModel<>();
        for (ProductPair productPair : locomotiveList) {
            locomotiveListModel.addElement(productPair);
        }
        for (ProductPair productPair : rollingStockList) {
            rollingStockListModel.addElement(productPair);
        }        
        for (ProductPair productPair : trackPackList) {
            trackPackListModel.addElement(productPair);
        }
        // Set up the UI components
        locomotiveListUI = new JList<>(locomotiveListModel);
        rollingStockListUI = new JList<>(rollingStockListModel);
        trackPackListUI = new JList<>(trackPackListModel);

        JPanel detailsPanel = new JPanel(new GridLayout(16, 1));
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
        JPanel tempPanel11 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel12 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel13 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel14 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel15 = new JPanel(new GridLayout(1, 2));
        JPanel tempPanel16 = new JPanel(new GridLayout(1, 2));

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

        JButton addLocomotiveButton = new JButton("Add Locomotive");
        addLocomotiveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductPopup(ProductType.LOCOMOTIVE);
            }   
        });
        
        JButton removeLocomotiveButton = new JButton("Remove Locomotive");
        removeLocomotiveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentlySelected = locomotiveListUI.getSelectedIndex();
                locomotiveListUI.remove(currentlySelected);            }   
        });

        JButton addRollingStockButton = new JButton("Add Rolling Stock");
        addRollingStockButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductPopup(ProductType.ROLLINGSTOCK);
            }   
        });
        
        JButton removeRollingStockButton = new JButton("Remove Rolling Stock");
        removeRollingStockButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentlySelected = rollingStockListUI.getSelectedIndex();
                rollingStockListUI.remove(currentlySelected);            }   
        });


        JButton addTrackPackButton = new JButton("Add Track Pack");
        addTrackPackButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ProductPopup(ProductType.TRACKPACK);
            }   
        });

        JButton removeTrackPackButton = new JButton("Remove Track Pack");
        removeTrackPackButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentlySelected = trackPackListUI.getSelectedIndex();
                trackPackListUI.remove(currentlySelected);            }   
        });


        tempPanel1.add(new JLabel("productCodeTextArea (Product Code should start with 'M'):"));
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
        tempPanel9.add(new JLabel("controllerProductCodeTextArea (Product Code should start with a 'C'):"));
        tempPanel9.add(controllerProductCodeTextArea);
        tempPanel10.add(new JLabel("locomotiveListUI:"));
        tempPanel10.add(locomotiveListUI);
        tempPanel11.add(addLocomotiveButton);
        tempPanel11.add(removeLocomotiveButton);
        tempPanel12.add(new JLabel("rollingStockListUI:"));
        tempPanel12.add(rollingStockListUI);
        tempPanel13.add(addRollingStockButton);
        tempPanel13.add(removeRollingStockButton);
        tempPanel14.add(new JLabel("trackPackListUI:"));
        tempPanel14.add(trackPackListUI);
        tempPanel15.add(addTrackPackButton);
        tempPanel15.add(removeTrackPackButton);
        tempPanel16.add(addProducButton);
        tempPanel16.add(goBackButton);
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
        detailsPanel.add(tempPanel11);
        detailsPanel.add(tempPanel12);
        detailsPanel.add(tempPanel13);
        detailsPanel.add(tempPanel14);
        detailsPanel.add(tempPanel15);
        detailsPanel.add(tempPanel16);
        add(detailsPanel);
    }

    private void goBack(){
        System.out.println("Go back to product screen");
        parentScreen.setVisible(true);
        this.dispose();
    }
    
    private void ProductPopup(ProductType productType){
        AddProductPopup detailsScreen = new AddProductPopup(this, productType);
        detailsScreen.setVisible(true);
        this.setVisible(false);
    }

    public void addProductFromButton(ProductPair productPair){
        if (productPair.getProduct().getProductType() == ProductType.TRACKPACK) {
            trackPackListModel.addElement(productPair);
            trackPackList.add(productPair);
        }
        if (productPair.getProduct().getProductType() == ProductType.ROLLINGSTOCK) {
            rollingStockListModel.addElement(productPair);
            rollingStockList.add(productPair);
        }
        if (productPair.getProduct().getProductType() == ProductType.LOCOMOTIVE) {
            locomotiveListModel.addElement(productPair);
            locomotiveList.add(productPair);
        }
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
        Boolean validEraCode = ProductValidator.getInstance().validEraCode(eraCodeText);
        
        Boolean validController = ProductValidator.getInstance().validProduct(controllerProductCodeTextArea.getText(), ProductType.CONTROLLER);

        Boolean validLocomotiveList = ProductValidator.getInstance().validProductList(locomotiveList, ProductType.LOCOMOTIVE, 1);
        Boolean validRollingStockList = ProductValidator.getInstance().validProductList(rollingStockList, ProductType.ROLLINGSTOCK, 1);
        Boolean validTrackPackList = ProductValidator.getInstance().validProductList(trackPackList, ProductType.TRACKPACK, 0);

        if (!isEditing && validProductCode && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validEraCode && validController && validLocomotiveList && validRollingStockList && validTrackPackList) {
            Inventory.getInstance().addProduct(new TrainSet(productCodeText, productNameText, manufacturerNameText, Integer.parseInt(retailPriceText), Integer.parseInt(stockText), (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), eraCodeText ,(Controller) Inventory.getInstance().getProduct(controllerProductCodeTextArea.getText()) , locomotiveList, rollingStockList,trackPackList));
            parentScreen.setVisible(true);
            parentScreen.searchProducts();
            this.dispose();
        } else if (isEditing && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validEraCode && validController && validLocomotiveList && validRollingStockList && validTrackPackList) {
            InventoryUpdate.getInstance().updateProduct(new TrainSet(productCodeText, productNameText, manufacturerNameText, Integer.parseInt(retailPriceText), Integer.parseInt(stockText), (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), eraCodeText ,(Controller) Inventory.getInstance().getProduct(controllerProductCodeTextArea.getText()), locomotiveList, rollingStockList,trackPackList));
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
        if (!validController){
            JOptionPane.showMessageDialog(null,
            "Invalid Controller Product Code.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }
        if (!validLocomotiveList){
            JOptionPane.showMessageDialog(null,
            "Invalid Locomotive List.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }
        if (!validRollingStockList){
            JOptionPane.showMessageDialog(null,
            "Invalid Rolling Stock List.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }
        if (!validTrackPackList){
            JOptionPane.showMessageDialog(null,
            "Invalid Track Pack List.",
            "Incorrect Inputs",
            JOptionPane.WARNING_MESSAGE);
        }

    }

    public void editProduct(TrainSet product){
        productCodeTextArea.setText(product.getProductCode());
        productCodeTextArea.setEditable(false);
        productNameTextArea.setText(product.getProductName());
        manufacturerNameTextArea.setText(product.getManufacturerName());
        retailPriceTextArea.setText(Integer.toString(product.getRetailPrice()));
        stockTextArea.setText(Integer.toString(product.getStock()));
        gaugeComboBox.setSelectedItem(product.getGauge());
        scaleComboBox.setSelectedItem(product.getScale());
        eraCodeTextArea.setText(product.getEraCode());
        controllerProductCodeTextArea.setText(product.getController().getProductCode());
        locomotiveList = product.getLocomotives();
        rollingStockList = product.getRollingStocks();
        trackPackList = product.getTrackPacks();
        isEditing = true;
    }
}
