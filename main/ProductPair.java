public class ProductPair {
    private Product product;
    private int quantity;

    ProductPair(Product productInit, int quantityInit){
        setProduct(productInit);
        setQuantity(quantityInit);
    }

    public Product getProduct() {
        return product;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity){
        this.quantity =+ quantity;
    }

    @Override
    public String toString() {
        return product.getProductName() + ": " + quantity;
    }
}
