public class Product {
    private String productCode;
    private String productName;
    private String manufacturerName;
    private int retailPrice;
    private int stock;
    private Gauge gauge;
    private Scale scale;

    public Product(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale){
        this.productCode = productCode;
        this.productName = productName;
        this.manufacturerName = manufacturerName;
        this.retailPrice = retailPrice;
        this.stock = stock;
        this.gauge = gauge;
        this.scale = scale;
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
}



