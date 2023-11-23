import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelectedCategoryScreen extends JFrame {
    private List<Product> productList;
    private DefaultListModel<Product> listModel;
    private JList<Product> productListUI;
    private JTextField searchField;
    private JTextField priceField;
    private JComboBox<ProductType> productTypeComboBox;
    private JComboBox<Gauge> gaugeComboBox;
    private JComboBox<Scale> scaleComboBox;

    /*
     * getProduct(productCode) // gets details for the product
     * AddToOrder(productCode, orderID, quantity)
     * 
     * Would be nice if the product type specific details could be searched
     */

    public SelectedCategoryScreen() {
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

        JButton testButton = new JButton("Add product to order");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(productListUI.getSelectedValue());
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
        topPanel.add(testButton);


        add(topPanel, BorderLayout.NORTH);

        add(new JScrollPane(productListUI), BorderLayout.CENTER);

        // Set up the frame
        setTitle("Selected Category Screen");
        setSize(1600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
                System.out.println(product.toString());
            }
        }
    }

    public static void main(String[] args) {
        new SelectedCategoryScreen();
    }
}