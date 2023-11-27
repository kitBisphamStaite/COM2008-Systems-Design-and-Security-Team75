import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddTrackPack extends JFrame {
    private AddProduct parentScreen;
    JTextArea productCodeTextArea = new JTextArea(); //Check it starts with "P"
    JTextArea productNameTextArea = new JTextArea();
    JTextArea manufacturerNameTextArea = new JTextArea();
    JTextArea retailPriceTextArea = new JTextArea();
    JTextArea stockTextArea = new JTextArea();
    JComboBox<Gauge> gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
    JComboBox<Scale> scaleComboBox = new JComboBox<Scale>(Scale.values());
    private ArrayList<ProductPair> trackList;
    private DefaultListModel<ProductPair> trackListModel;
    private JList<ProductPair> trackListUI;

    private boolean isEditing = true;

    public AddTrackPack(AddProduct parentScreen) {
        this.parentScreen = parentScreen;
        setTitle("Track Pack");
        setSize(600, 600);
        setLocationRelativeTo(null);


        // Sample product data
        trackList = new ArrayList<>();

        //making an initial product list
        trackListModel = new DefaultListModel<>();
        for (ProductPair productPair : trackList) {
            trackListModel.addElement(productPair);
        }

        // Set up the UI components
        trackListUI = new JList<>(trackListModel);

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
                int currentlySelected = trackListUI.getSelectedIndex();
                trackListUI.remove(currentlySelected);
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
        tempPanel8.add(trackListUI);
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

    public void addTrackFromButton(ProductPair productPair){
        if (productPair.getProduct().getProductType() == ProductType.TRACK) {
            trackListModel.addElement(productPair);
        }
        else{
            System.out.println("Wrong Product Type");
        }
    }

    private void addProduct(){
        String productCodeText = productCodeTextArea.getText().strip();
        Boolean validProductCode = Inventory.getInstance().validProductCode(productCodeText, ProductType.CONTROLLER);

        String productNameText = productNameTextArea.getText().strip();
        Boolean validProductName = Inventory.getInstance().validProductName(productNameText);

        String manufacturerNameText = manufacturerNameTextArea.getText().strip();
        boolean validManufacturerName = Inventory.getInstance().validManufacturerName(manufacturerNameText);

        String retailPriceText = retailPriceTextArea.getText().strip();
        boolean validRetailPrice = Inventory.getInstance().validRetailPrice(retailPriceText);

        String stockText = stockTextArea.getText().strip();
        boolean validStock = Inventory.getInstance().validStock(stockText);

        Boolean validGauge = Inventory.getInstance().validGauge((Gauge) gaugeComboBox.getSelectedItem());
        Boolean validScale = Inventory.getInstance().validScale((Scale) scaleComboBox.getSelectedItem());

        Boolean validTrackList = Inventory.getInstance().validProductList(trackList, ProductType.TRACK, 1);

        if (validProductCode && validProductName && validManufacturerName && validRetailPrice && validStock && validGauge && validScale && validTrackList) {
            Inventory.getInstance().addProduct(new TrackPack(productCodeText, productNameText, manufacturerNameText, 
                                                Integer.parseInt(retailPriceText), Integer.parseInt(stockText), 
                                                (Gauge) gaugeComboBox.getSelectedItem(), (Scale) scaleComboBox.getSelectedItem(), 
                                                trackList));
            parentScreen.setVisible(true);
            this.dispose();
        }
    }

    public void editProduct(TrackPack product){
        productCodeTextArea.setText(product.getProductCode());
        productNameTextArea.setText(product.getProductName());
        manufacturerNameTextArea.setText(product.getManufacturerName());
        retailPriceTextArea.setText(Integer.toString(product.getRetailPrice()));
        retailPriceTextArea.setText(Integer.toString(product.getStock()));
        gaugeComboBox.setSelectedItem(product.getGauge());
        scaleComboBox.setSelectedItem(product.getScale());
        trackList = product.getTracks();
        isEditing = false;
    }
}
