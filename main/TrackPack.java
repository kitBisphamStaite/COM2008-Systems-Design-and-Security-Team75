import java.util.ArrayList;

public class TrackPack extends Product {
    private ArrayList<ProductPair> tracks;

    public TrackPack(String productCode, String productName, String manufacturerName,int retailPrice, int stock, 
                    Gauge gauge, Scale scale, ArrayList<ProductPair> tracks){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, ProductType.TRACKPACK);
        this.tracks = tracks;
    }

    public ArrayList<ProductPair> getTracks() {
        return tracks;
    }
    public void setTracks(ArrayList<ProductPair> tracks) {
        this.tracks = tracks;
    }

    public void addToTracks(Track track, int quantity){
        boolean inList = false;
        for (ProductPair productPair : tracks) {
            if (productPair.getProduct() == track) {
                inList = true;
                productPair.addQuantity(quantity);
            }
        }
        if (!inList) {
            tracks.add(new ProductPair(track, quantity));
        }
    }

    @Override
    public String getProductDetails(){
        System.out.println("Not implemented yet: Track Pack");
        return null;
    }

}
