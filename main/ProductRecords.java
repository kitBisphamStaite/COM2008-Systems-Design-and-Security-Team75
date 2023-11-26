import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public ProductRecords() {
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
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProducts();
            }
        });
        
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
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addProductButton);
        bottomPanel.add(editProductButton);
        bottomPanel.add(returnHomeButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(productListUI), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Set up the frame
        setTitle("Product Records");
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
        System.out.println("Return Home");
    }

    private void addProductDetails(){
        AddProduct detailsScreen = new AddProduct(this);
        detailsScreen.setVisible(true);
        this.setVisible(false);
    }

    private void searchProducts() {
        String searchTerm = searchField.getText().toLowerCase();
        int productPrice = Integer.parseInt(priceField.getText());

        ProductType selectedProductType = (ProductType) productTypeComboBox.getSelectedItem();
        System.out.println(selectedProductType);
        Gauge selectedGauge = (Gauge) gaugeComboBox.getSelectedItem();
        System.out.println(selectedGauge);
        Scale selectedScale = (Scale) scaleComboBox.getSelectedItem();
        System.out.println(selectedScale);

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
        new ProductRecords();
    }
}