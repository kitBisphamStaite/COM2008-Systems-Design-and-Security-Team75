public class Locomotive extends Product {
    private String eraCode;
    private ControlType controlType;
    
    public Locomotive(String productCode, String productName, String manufacturerName,int retailPrice, int stock, 
                    Gauge gauge, Scale scale, String eraCode, ControlType controlType){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, ProductType.LOCOMOTIVE);
        this.eraCode = eraCode;
        this.controlType = controlType;
    }

    public String getEraCode() {
        return eraCode;
    }
    public ControlType getControlType() {
        return controlType;
    }
    public void setEraCode(String eraCode) {
        this.eraCode = eraCode;
    }
    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    @Override
    public String getProductDetails(){
        System.out.println("Not implemented yet: Locomotive");
        return null;
    }
}