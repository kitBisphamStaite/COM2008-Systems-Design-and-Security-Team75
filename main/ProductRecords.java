import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

public class ProductRecords extends JFrame {
    private List<Product> productList;
    private DefaultListModel<Product> listModel;
    private JList<Product> productListUI;
    private JTextField searchField;
    private JTextField priceField;
    private JComboBox<ProductType> productTypeComboBox;
    private JComboBox<Gauge> gaugeComboBox;
    private JComboBox<Scale> scaleComboBox;
    private boolean userIsStaff;

    public ProductRecords(boolean userIsStaff) {
        //Determines the user
        this.userIsStaff = userIsStaff; 

        // Sample product data
        productList = new ArrayList<>();
        productList = Inventory.getInstance().getProducts();

        //making an initial product list
        listModel = new DefaultListModel<>();
        for (Product product : productList) {
            listModel.addElement(product);
        }

        // Set up the UI components
        productListUI = new JList<>(listModel);
        searchField = new JTextField(20); 
        productTypeComboBox = new JComboBox<ProductType>(ProductType.values());
        gaugeComboBox = new JComboBox<Gauge>(Gauge.values());
        scaleComboBox = new JComboBox<Scale>(Scale.values());
        priceField = new JTextField("0", 6); 
        
        JPanel bottomPanel = new JPanel();

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });
        
        if (userIsStaff){
            JButton editProductButton = new JButton("Edit Product");
            editProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (productListUI.getSelectedValue() != null) {
                        editProductDetails(productListUI.getSelectedValue());
                    }
                }
            });
    
            JButton addProductButton = new JButton("Add Product");
            addProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addProductDetails();
                }
            });
    
            JButton deleteProductButton = new JButton("Delete Product");
            deleteProductButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteProduct(productListUI.getSelectedValue());
                }
            });
            bottomPanel.add(addProductButton);
            bottomPanel.add(editProductButton);
            bottomPanel.add(deleteProductButton);
        } else{
            JButton productButton = new JButton("View Product");
            productButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (productListUI.getSelectedValue() != null) {
                        openProductDetails(productListUI.getSelectedValue());
                    }
                }
            });
    
            JButton orderPageButton = new JButton("View Orders");
            orderPageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openOrderPage();
                }
            });
            bottomPanel.add(productButton);
            bottomPanel.add(orderPageButton);
        }


        JButton returnHomeButton = new JButton("Return to Home");
        returnHomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnHome();
            }
        });

        // Set up the layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("ProductType:"));
        topPanel.add(productTypeComboBox);
        topPanel.add(new JLabel("Gauge:"));
        topPanel.add(gaugeComboBox);
        topPanel.add(new JLabel("Scale:"));
        topPanel.add(scaleComboBox);
        topPanel.add(new JLabel("Max Price (£):"));
        topPanel.add(priceField);
        topPanel.add(searchButton);

        JPanel detailsPanel = new JPanel();
        JLabel productCode = new JLabel("Null");
        detailsPanel.add(productCode);

        bottomPanel.add(returnHomeButton);


        JScrollPane productScrollPane = new JScrollPane(productListUI);
        productListUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productListUI.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
              if (productListUI.getSelectedValue() != null) {
                productCode.setText(productListUI.getSelectedValue().toString());
              }
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productScrollPane, detailsPanel);
        if (productListUI != null){
            productListUI.setSelectedIndex(0);
            productCode.setText(productListUI.getSelectedValue().toString());
        }

        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        if (userIsStaff){
            setTitle("Product Records");
        } else {
            setTitle("Selected Category Screen");
        }
        // Set up the frame
        setSize(1600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void editProductDetails(Product product) {
        System.out.println("Edit Product");
        if (product.getProductType() == ProductType.CONTROLLER) {
            AddProduct detailsScreen = new AddProduct(this);
            AddController addController = new AddController(detailsScreen);
            addController.editProduct((Controller) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.LOCOMOTIVE) {
            AddProduct detailsScreen = new AddProduct(this);
            AddLocomotive addController = new AddLocomotive(detailsScreen);
            addController.editProduct((Locomotive) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.ROLLINGSTOCK) {
            AddProduct detailsScreen = new AddProduct(this);
            AddRollingStock addController = new AddRollingStock(detailsScreen);
            addController.editProduct((RollingStock) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACK) {
            AddProduct detailsScreen = new AddProduct(this);
            AddTrack addController = new AddTrack(detailsScreen);
            addController.editProduct((Track) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACKPACK) {
            AddProduct detailsScreen = new AddProduct(this);
            AddTrackPack addController = new AddTrackPack(detailsScreen);
            addController.editProduct((TrackPack) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRAINSET) {
            AddProduct detailsScreen = new AddProduct(this);
            AddTrainSet addController = new AddTrainSet(detailsScreen);
            addController.editProduct((TrainSet) product);
            addController.setVisible(true);
            this.setVisible(false);
        }
    }
    
    private void returnHome(){
        this.dispose();
        System.out.println("Return Home");
    }

    private void addProductDetails(){
        AddProduct detailsScreen = new AddProduct(this);
        detailsScreen.setVisible(true);
        this.setVisible(false);
    }

    private void deleteProduct(Product product){
        InventoryDelete.getInstance().deleteProduct(product);
        searchProducts();
    }

    private void openProductDetails(Product product) {
        ProductPopup detailsScreen = new ProductPopup(product, this);
        detailsScreen.setVisible(true);
        this.setVisible(false);
    }

    private void openOrderPage(){
        System.out.println("Open order page");
        //Sends you to the basket page, which someone else is making
    }

    public void searchProducts() {
        String searchTerm = searchField.getText().toLowerCase();
        int productPrice = Integer.parseInt(priceField.getText());

        ProductType selectedProductType = (ProductType) productTypeComboBox.getSelectedItem();
        Gauge selectedGauge = (Gauge) gaugeComboBox.getSelectedItem();
        Scale selectedScale = (Scale) scaleComboBox.getSelectedItem();

        listModel.clear();

        for (Product product : productList) {
            boolean matchesSearchTerm = searchTerm == null
                    || product.getManufacturerName().toLowerCase().contains(searchTerm)
                    || product.getProductCode().toLowerCase().contains(searchTerm)
                    || product.getProductName().toLowerCase().contains(searchTerm);

            boolean matchesProductType = selectedProductType.equals(ProductType.ALL) || product.getProductType().equals(selectedProductType);
            boolean matchesGauge = selectedGauge.equals(Gauge.ALL) || product.getGauge().equals(selectedGauge);
            boolean matchesScale = selectedScale.equals(Scale.ALL) || product.getScale().equals(selectedScale);
            boolean inPriceRange = productPrice == 0 || product.inPriceRange(productPrice);

            if (matchesSearchTerm && matchesProductType && matchesGauge && matchesScale && product.inStock() && inPriceRange) {
                listModel.addElement(product);
            }
        }
    }

    public static void main(String[] args) {
        new ProductRecords(false);
    }
}