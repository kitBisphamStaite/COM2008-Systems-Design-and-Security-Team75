import java.util.ArrayList;

public class Inventory {
    private static Inventory INSTANCE;
    private ArrayList<Product> products = new ArrayList<Product>();
    private Inventory() {        
    }
    
    public static Inventory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        INSTANCE.createDefaultProductList();
        return INSTANCE;
    }

    // Track codes start with R, controller codes with C, locomotives with L, rolling stock with S, train sets with M, and track packs with P
    public void createDefaultProductList(){

        products.clear();
        products.add(new Product("C001", "Controller 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.CONTROLLER));
        products.add(new Product("C002", "Controller 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.CONTROLLER));

        products.add(new Product("R001", "Track 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.TRACK));
        products.add(new Product("R002", "Track 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.TRACK));
    
        products.add(new Product("L002", "Locomotive 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.LOCOMOTIVE));
        products.add(new Product("L002", "Locomotive 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.LOCOMOTIVE));
        
        products.add(new Product("S002", "Rolling 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.ROLLINGSTOCK));
        products.add(new Product("S002", "Rolling 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.ROLLINGSTOCK));
        
        products.add(new Product("M002", "Train Set 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.TRAINSET));
        products.add(new Product("M002", "Train Set 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.TRAINSET));
        
        products.add(new Product("P002", "Track Pack 1", "John", 5, 1, Gauge.ALL, Scale.ALL, ProductType.TRACKPACK));
        products.add(new Product("P002", "Track Pack 2", "John", 10, 1, Gauge.ALL, Scale.ALL, ProductType.TRACKPACK));

    }

    public ArrayList<Product> getProducts() {
        return products;
    }

}
