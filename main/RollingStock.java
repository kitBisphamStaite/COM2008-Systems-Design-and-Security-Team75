public class RollingStock extends Product{
    private String eraCode;

    
    public RollingStock(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, String eraCode){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale);
        this.eraCode = eraCode;
    }
    public String getEraCode() {
        return eraCode;
    }
    public void setEraCode(String eraCode) {
        this.eraCode = eraCode;
    }
}