public class Product {
    private String productCode;
    private String productName;
    private String manufacturerName;
    private int retailPrice;
    private int stock;
    private Gauge gauge;
    private Scale scale;
    private ProductType productType;


    // Track codes start with R, controller codes with C, locomotives with L, rolling stock with S, train sets with M, and track packs with P

    public Product(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, ProductType productType){
        this.productCode = productCode;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.retailPrice = retailPrice;
        this.stock = stock;
        this.gauge = gauge;
        this.scale = scale;
        this.productType = productType;
    }
    public String getProductCode() {
        return productCode;
    }
    public String getProductName() {
        return productName;
    }
    public String getManufacturerName() {
        return manufacturerName;
    }
    public int getRetailPrice() {
        return retailPrice;
    }
    public int getStock() {
        return stock;
    }
    public Gauge getGauge() {
        return gauge;
    }
    public Scale getScale() {
        return scale;
    }
    public ProductType getProductType() {
        return productType;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }
    public void setRetailPrice(int retailPrice) {
        this.retailPrice = retailPrice;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setGauge(Gauge gauge) {
        this.gauge = gauge;
    }
    public void setScale(Scale scale) {
        this.scale = scale;
    }
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public boolean inStock(){
        if (stock > 0) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean inPriceRange(int maxPrice){
        if (maxPrice >= this.retailPrice) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString() {
        return (productName + ", £" + retailPrice + ", " + stock + ", " + gauge + ", " + scale);
    }

    public String getProductDetails(){
        return null;
    }

}



