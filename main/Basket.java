import java.util.ArrayList;

public class Basket {
    public static Basket basket;
    private ArrayList<Product> products = new ArrayList<Product>();
    private int retailPrice;

    private Basket() {        
    }
    
    public static Basket getInstance() {
        if(basket == null) {
            basket = new Basket();
        }
        
        return basket;
    }

    // getters and setters
    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getRetailPrice(){
        return retailPrice;
    }

    public void addProduct(Product product, int quantity){
        if (product.getStock() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                products.add(product);
                retailPrice += product.getRetailPrice();
            }
            System.out.println("Product Added");
        }
        else{
            System.out.println("No enought in stock"); //probably should error in a better way
        }
    }

    public void removeProduct(Product product){
        products.remove(product);
        retailPrice -= product.getRetailPrice();
    }
    
//put it into the DB

}
