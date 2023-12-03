import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ProductValidator {
    private static ProductValidator INSTANCE;
    
    private ProductValidator() {        
    }
    
    public static ProductValidator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ProductValidator();
        }
        return INSTANCE;
    }

    public boolean validProductCode(String productCode, ProductType productType){
        if (productCode != null && productCode != "") {
            if (Inventory.getInstance().isNotProduct(productCode)) {
                if (productCode.length() > 3 && productCode.length() < 6) {
                    if (productType == ProductType.CONTROLLER && productCode.startsWith("C")) {
                        return true;                        
                    } else if (productType == ProductType.LOCOMOTIVE && productCode.startsWith("L")) {
                        return true;                        
                    } else if (productType == ProductType.ROLLINGSTOCK && productCode.startsWith("S")) {
                        return true;
                    } else if (productType == ProductType.TRACK && productCode.startsWith("R")) {
                        return true;
                    } else if (productType == ProductType.TRACKPACK && productCode.startsWith("P")) {
                        return true;
                    } else if (productType == ProductType.TRAINSET && productCode.startsWith("M")) {
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(null,"Incorrect Product Type.","Invalid Product code",JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"Incorrect Product Code Length.","Invalid Product code",JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Product Code already exists.","Invalid Product code",JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,"Invalid String input.","Invalid Product code",JOptionPane.WARNING_MESSAGE);

        }
        return false;
    }

    public boolean validProductName(String productname){
        if (productname != null && productname !="") {
            return true;
        }
        return false;
    }

    public boolean validManufacturerName(String manufacturerName){
        if (manufacturerName != null && manufacturerName !="") {
            return true;
        }
        return false;    
    }

    public boolean validRetailPrice(String retailPrice){
        if (retailPrice != null && retailPrice !="") {
            //if the retailPrice is a number
            if (Integer.parseInt(retailPrice) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean validStock(String stock){
        if (stock != null && stock !="") {
            //if the stock is a number
            if (Integer.parseInt(stock) >= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean validQuantity(String quantity){
        if (quantity != null && quantity !="") {
            if (Integer.parseInt(quantity) > 0){
                return true;
            }
        }
        return false;
    }

    public boolean validGauge(Gauge gauge){
        return gauge != Gauge.ALL;
    }

    public boolean validScale(Scale scale){
        return scale != Scale.ALL;
    }

    public boolean validChipType(ChipType chipType){
        return chipType != ChipType.ALL;
    }

    public boolean validEraCode(String eraCode){
        if (eraCode != null && eraCode !="") {
            return true;
        }
        return false;    
    }

    public boolean validControlType(ControlType controlType){
        return controlType != ControlType.ALL;
    }

    
    public boolean validCurveRadius(CurveRadius curveRadius){
        return curveRadius != CurveRadius.ALL;
    }
    
    public boolean validTrackType(TrackType trackType){
        return trackType != TrackType.ALL;
    }

    public boolean validProduct(String productCode, ProductType productType){
        for (Product product : Inventory.getInstance().getProducts()) {
            if (product.getProductType() == productType && product.getProductCode().equals(productCode)) {
                return true;
            }
        }
        return false;
    }

    public boolean validProductList(ArrayList<ProductPair> productList, ProductType productType, int minimumListLength){
        for (ProductPair productPair : productList) {
            if (productPair.getProduct().getProductType() != productType) {
                return false;
            }
        }
        if (productList.size() < minimumListLength) {
            return false;
        }
        return true;
    }

    public boolean validProductListAdd(ArrayList<ProductPair> productList, ProductType productType, ProductPair product){
        for (ProductPair productPair : productList) {
            if (productPair.getProduct().getProductType() != productType) {
                return false;
            }
            if (productPair.getProduct().getProductCode().equals(product.getProduct().getProductCode())){
                return false;
            }
        }
        return true;
    }
}
