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
    JPanel detailsPanel = new JPanel();

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
                    addProduct();
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

        bottomPanel.add(returnHomeButton);


        JScrollPane productScrollPane = new JScrollPane(productListUI);
        productListUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productListUI.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
              if (productListUI.getSelectedValue() != null && e.getValueIsAdjusting()) {
                changeProductDetails(productListUI.getSelectedValue());
              }
            }   
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, productScrollPane, detailsPanel);
        if (productListUI != null){
            productListUI.setSelectedIndex(0);
            changeProductDetails(productListUI.getSelectedValue());
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

    public void changeProductDetails(Product product){
        resetDetailPanel();
        JLabel productCode = new JLabel("Product Code: " + product.getProductCode());
        JLabel productName = new JLabel("Product Name: " + product.getProductName());
        JLabel manufacturerName = new JLabel("Manufacturer Name: " + product.getManufacturerName());
        JLabel retailPrice = new JLabel("Retail Price: " + Integer.toString(product.getRetailPrice()));
        JLabel stock = new JLabel("Stock: " + Integer.toString(product.getStock()));
        JLabel gauge = new JLabel("Guage: " + product.getGauge().toString());
        JLabel scale = new JLabel("Scale: " + product.getScale().toString());
        
        productCode.setFont(new Font("Dialog", Font.BOLD, 14));
        productName.setFont(new Font("Dialog", Font.BOLD, 14));
        manufacturerName.setFont(new Font("Dialog", Font.BOLD, 14));
        retailPrice.setFont(new Font("Dialog", Font.BOLD, 14));
        stock.setFont(new Font("Dialog", Font.BOLD, 14));
        gauge.setFont(new Font("Dialog", Font.BOLD, 14));
        scale.setFont(new Font("Dialog", Font.BOLD, 14));

        detailsPanel.add(productCode);
        detailsPanel.add(productName);
        detailsPanel.add(manufacturerName);
        detailsPanel.add(retailPrice);
        detailsPanel.add(stock);
        detailsPanel.add(gauge);
        detailsPanel.add(scale);

        if (productListUI.getSelectedValue().getProductType() == ProductType.CONTROLLER){
            displayControllerDetails((Controller) product);
        }
        if (productListUI.getSelectedValue().getProductType() == ProductType.LOCOMOTIVE){
            displayLocomotiveDetails((Locomotive) product);
        }
        if (productListUI.getSelectedValue().getProductType() == ProductType.ROLLINGSTOCK){
            displayRollingStockDetails((RollingStock) product);
        }
        if (productListUI.getSelectedValue().getProductType() == ProductType.TRACK){
            displayTrackDetails((Track) product);
        }
        if (productListUI.getSelectedValue().getProductType() == ProductType.TRACKPACK){
            displayTrackPackDetails((TrackPack) product);
        }
        if (productListUI.getSelectedValue().getProductType() == ProductType.TRAINSET){
            displayTrainSetDetails((TrainSet) product);
        }   
    }

    public void displayControllerDetails(Controller product){
        JLabel chipType = new JLabel("Chip Type: " + product.GetChipType().toString());
        chipType.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(chipType);
    }
    
    public void displayLocomotiveDetails(Locomotive product){
        JLabel controlType = new JLabel("Control Type: " + product.getControlType().toString());
        JLabel eraCode = new JLabel("Era Code: " + product.getEraCode());
        controlType.setFont(new Font("Dialog", Font.BOLD, 14));
        eraCode.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(controlType);
        detailsPanel.add(eraCode);
    }
    
    public void displayRollingStockDetails(RollingStock product){
        JLabel eraCode = new JLabel("Era Code: " + product.getEraCode());
        eraCode.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(eraCode);
    }
    
    public void  displayTrackDetails(Track product){
        JLabel curveRadius = new JLabel("Curve Radius: " + product.getCurveRadius().toString());
        JLabel trackType = new JLabel("Track Type: " + product.getTrackType().toString());
        curveRadius.setFont(new Font("Dialog", Font.BOLD, 14));
        trackType.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(curveRadius);
        detailsPanel.add(trackType);
    }
    
    public void displayTrackPackDetails(TrackPack product){
        JLabel tracks = new JLabel("Tracks: " + product.getTracks().toString());
        tracks.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(tracks);
    }
    
    public void displayTrainSetDetails(TrainSet product){
        JLabel eraCode = new JLabel("Era Code: " + product.getEraCode());
        JLabel controllerProductCode = new JLabel("Controller Product Code: " + product.getController().getProductCode());
        JLabel locomotives = new JLabel("Locomotives: " + product.getLocomotives().toString());
        JLabel rollingStocks = new JLabel("Rolling Stocks: " + product.getRollingStocks().toString());
        JLabel trackPacks = new JLabel("Track Packs: " + product.getTrackPacks().toString());
        eraCode.setFont(new Font("Dialog", Font.BOLD, 14));
        controllerProductCode.setFont(new Font("Dialog", Font.BOLD, 14));
        locomotives.setFont(new Font("Dialog", Font.BOLD, 14));
        rollingStocks.setFont(new Font("Dialog", Font.BOLD, 14));
        trackPacks.setFont(new Font("Dialog", Font.BOLD, 14));
        detailsPanel.add(eraCode);
        detailsPanel.add(controllerProductCode);
        detailsPanel.add(locomotives);
        detailsPanel.add(rollingStocks);
        detailsPanel.add(trackPacks);

    }

    private void editProductDetails(Product product) {
        System.out.println("Edit Product");
        AddProduct addProduct = new AddProduct(this, product.getProductType());
        if (product.getProductType() == ProductType.CONTROLLER) {
            addProduct.editProduct((Controller) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.LOCOMOTIVE) {
            addProduct.editProduct((Locomotive) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.ROLLINGSTOCK) {
            addProduct.editProduct((RollingStock) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACK) {
            addProduct.editProduct((Track) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACKPACK) {
            addProduct.editProduct((TrackPack) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRAINSET) {
            addProduct.editProduct((TrainSet) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
    }

    private void returnHome(){
        this.dispose();
        if (userIsStaff){
            new StaffDashboard();
        }
        System.out.println("Return Home");
    }

    private void addProduct(){
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeProductDetails(productListUI.getSelectedValue());
            }
        });

        JButton addControllerButton = new JButton("Add Controller");
        addControllerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.CONTROLLER);
            }
        });

        JButton AddLocomotiveButton = new JButton("Add Locomotive");
        AddLocomotiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.LOCOMOTIVE);
            }
        });

        JButton addRollingStockButton = new JButton("Add Rolling Stock");
        addRollingStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.ROLLINGSTOCK);
            }
        });

        JButton AddTrackButton = new JButton("Add Track");
        AddTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.TRACK);
            }
        });

        JButton AddTrackPackButton = new JButton("Add Track Pack");
        AddTrackPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.TRAINSET);

            }
        });

        JButton AddTrainSetButton = new JButton("Add Train Set");
        AddTrainSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewProduct(ProductType.TRAINSET);
            }
        });
        resetDetailPanel();
        GridLayout addProductLayout = new GridLayout(4,2);
        detailsPanel.setLayout(addProductLayout);
        detailsPanel.add(addControllerButton);
        detailsPanel.add(AddLocomotiveButton);
        detailsPanel.add(addRollingStockButton);
        detailsPanel.add(AddTrackButton);
        detailsPanel.add(AddTrackPackButton);
        detailsPanel.add(AddTrainSetButton);
        detailsPanel.add(goBackButton);
    }
    
    public void addNewProduct(ProductType productType){
        AddProduct addProduct = new AddProduct(this, productType);
        addProduct.setVisible(true);
        this.setVisible(false);
    }

    public void addController(){
        AddProduct addController = new AddProduct(this, ProductType.CONTROLLER);
        addController.setVisible(true);
        this.setVisible(false);
    }

    public void addLocomotive(){
        AddProduct addLocomotive = new AddProduct(this, ProductType.LOCOMOTIVE);
        addLocomotive.setVisible(true);
        this.setVisible(false);
    }
    
    public void addRollingStock(){
        AddProduct addRollingStock = new AddProduct(this, ProductType.ROLLINGSTOCK);
        addRollingStock.setVisible(true);
        this.setVisible(false);       
    }
    
    public void addTrack(){
        AddProduct addTrack = new AddProduct(this, ProductType.TRACK);
        addTrack.setVisible(true);
        this.setVisible(false);
    }
    
    public void addTrackPack(){
        AddProduct addTrackPack = new AddProduct(this, ProductType.TRACKPACK);
        addTrackPack.setVisible(true);
        this.setVisible(false);
    }

    public void addTrainSet(){
        AddProduct addTrainSet = new AddProduct(this, ProductType.TRAINSET);
        addTrainSet.setVisible(true);
        this.setVisible(false);
    }

    public void resetDetailPanel(){
        detailsPanel.removeAll();
        detailsPanel.repaint();
        detailsPanel.revalidate();
    }

    private void deleteProduct(Product product){
        String deleteConfirmationName = (String) JOptionPane.showInputDialog(null, "Enter Name");
        if (deleteConfirmationName != null &&  deleteConfirmationName.equals(product.getProductName())){
            InventoryDelete.getInstance().deleteProduct(product);
        }
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
        new ProductRecords(true);
    }
}