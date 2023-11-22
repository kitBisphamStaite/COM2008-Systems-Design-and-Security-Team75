public class TrackPack extends Product {
    private Track[] tracks;

    public TrackPack(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, Track[] tracks){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale);
        this.tracks = tracks;
    }

    public Track[] getTracks() {
        return tracks;
    }
    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }
}
