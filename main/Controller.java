public class Controller extends Product{
    private ChipType chipType;

    public Controller(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, ChipType chipType){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale);
        this.chipType = chipType;
    }
    
    public void SetChipType(ChipType chipType){
        this.chipType = chipType;
    }

    public ChipType GetChipType(){
        return this.chipType;
    }

}