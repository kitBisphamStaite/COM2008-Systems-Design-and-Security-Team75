import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.DriverManager;

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
	
    //Database Details
    String urlDB = "jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team075";
    String usernameDB = "team075";
    String passwordDB = "mood6Phah";

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
            JButton productButton = new JButton("Add Product to Basket");
            productButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (productListUI.getSelectedValue() != null) {
                        addProductToBastket(productListUI.getSelectedValue());
                    }
                }
            });
    
            JButton orderPageButton = new JButton("View Basket");
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

    private void openOrderPage(){
        ViewBasket viewBasket = new ViewBasket();
        viewBasket.setVisible(true);
        this.setVisible(false);
    }

    private void addProductToBastket(Product product){
        String quantityTemp = JOptionPane.showInputDialog("How much " + product.getProductName() + " do you want?");

        if (ProductValidator.getInstance().validQuantity(quantityTemp)) {
            if (Integer.parseInt(quantityTemp) <= product.getStock()) {
                int quantity = Integer.parseInt(quantityTemp);
                try {
                    Connection connection = DriverManager.getConnection(urlDB, usernameDB, passwordDB);
    		        listModel = new DefaultListModel<>();
    		        PreparedStatement getAllBasketOrderstmt = connection.prepareStatement("SELECT * FROM Orders WHERE status='BASKET' AND customer_id=" + Login.getUserID() + "ORDER BY date_ordered");
        	        ResultSet basketOrdersResultSet = getAllBasketOrderstmt.executeQuery();

                    if (basketOrdersResultSet.next()) {
                        int orderNumber = basketOrdersResultSet.getInt("order_number");
                        int orderPrice = basketOrdersResultSet.getInt("cost");

                        PreparedStatement getAllBasketOrderLinestmt = connection.prepareStatement("SELECT * FROM Order_Lines WHERE order_number=" + orderNumber + ", product_code=" + product.getProductCode());
                        ResultSet basketOrderLinesResultSet = getAllBasketOrderLinestmt.executeQuery();
                        if (basketOrderLinesResultSet.next()) {
                            int currentQuantity = basketOrderLinesResultSet.getInt("quantity");
                            int finalQuantity = quantity + currentQuantity;

                            String updateSQL = "UPDATE Order_Lines SET quantity=? WHERE order_number =?, product_code=?";
                            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                
                            preparedStatement.setInt(1, finalQuantity);
                            preparedStatement.executeUpdate();
                        } else {
                            String insertSQL = "INSERT INTO Order_Lines (order_number, product_code, quantity) VALUES (?, ?, ?)";
                            PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                            insertStatement.setInt(1, orderNumber);
                            insertStatement.setString(2, product.getProductCode());
                            insertStatement.setInt(3, quantity);

                            String updateSQL = "UPDATE Orders SET cost=? WHERE order_number =" + orderNumber;
                            PreparedStatement updateStatement = connection.prepareStatement(updateSQL); 
                
                            int newPrice = orderPrice + product.getRetailPrice()*quantity;
                            updateStatement.setInt(1, newPrice);
                            updateStatement.executeUpdate();
                        }
                    } else{
                        PreparedStatement getAllOrderstmt = connection.prepareStatement("SELECT * FROM Orders");
                        ResultSet getAllOrderSet = getAllOrderstmt.executeQuery();
                        getAllOrderSet.last();
                        int order_number = getAllOrderSet.getRow();
                        java.sql.Date sqlDate = new Date(System.currentTimeMillis());

                        String insertSQL = "INSERT INTO Orders (order_number, customer_id, date_order, cost, status) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                        insertStatement.setInt(1, order_number);
                        insertStatement.setInt(2, Login.getUserID());
                        insertStatement.setDate(1, sqlDate);
                        insertStatement.setInt(4, product.getRetailPrice()*quantity);
                        insertStatement.setString(5, "'BASKET'");

                        insertSQL = "INSERT INTO Order_Lines (order_number, product_code, quantity) VALUES (?, ?, ?)";
                        insertStatement = connection.prepareStatement(insertSQL);
                        insertStatement.setInt(1, order_number);
                        insertStatement.setString(2, product.getProductCode());
                        insertStatement.setInt(3, quantity);
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null,"Not Enought Stock.","Invalid Input",JOptionPane.WARNING_MESSAGE);            }
        } else {
           JOptionPane.showMessageDialog(null,"Invalid Quantity input.","Invalid Quantity",JOptionPane.WARNING_MESSAGE);
        }
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
        if (product.getProductType() == ProductType.CONTROLLER) {
            AddController addProduct = new AddController(this);
            addProduct.editProduct((Controller) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.LOCOMOTIVE) {
            AddLocomotive addProduct = new AddLocomotive(this);
            addProduct.editProduct((Locomotive) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.ROLLINGSTOCK) {
            AddRollingStock addProduct = new AddRollingStock(this);
            addProduct.editProduct((RollingStock) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACK) {
            AddTrack addProduct = new AddTrack(this);
            addProduct.editProduct((Track) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRACKPACK) {
            AddTrackPack addProduct = new AddTrackPack(this);
            addProduct.editProduct((TrackPack) product);
            addProduct.setVisible(true);
            this.setVisible(false);
        }
        if (product.getProductType() == ProductType.TRAINSET) {
            AddTrainSet addProduct = new AddTrainSet(this);
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
                addController();
            }
        });

        JButton AddLocomotiveButton = new JButton("Add Locomotive");
        AddLocomotiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLocomotive();
            }
        });

        JButton addRollingStockButton = new JButton("Add Rolling Stock");
        addRollingStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRollingStock();
            }
        });

        JButton AddTrackButton = new JButton("Add Track");
        AddTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrack();
            }
        });

        JButton AddTrackPackButton = new JButton("Add Track Pack");
        AddTrackPackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrackPack();
            }
        });

        JButton AddTrainSetButton = new JButton("Add Train Set");
        AddTrainSetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrainSet();
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

    public void addController(){
        AddController addController = new AddController(this);
        addController.setVisible(true);
        this.setVisible(false);
    }

    public void addLocomotive(){
        AddLocomotive addLocomotive = new AddLocomotive(this);
        addLocomotive.setVisible(true);
        this.setVisible(false);
    }
    
    public void addRollingStock(){
        AddRollingStock addRollingStock = new AddRollingStock(this);
        addRollingStock.setVisible(true);
        this.setVisible(false);       
    }
    
    public void addTrack(){
        AddTrack addTrack = new AddTrack(this);
        addTrack.setVisible(true);
        this.setVisible(false);
    }
    
    public void addTrackPack(){
        AddTrackPack addTrackPack = new AddTrackPack(this);
        addTrackPack.setVisible(true);
        this.setVisible(false);
    }

    public void addTrainSet(){
        AddTrainSet addTrainSet = new AddTrainSet(this);
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