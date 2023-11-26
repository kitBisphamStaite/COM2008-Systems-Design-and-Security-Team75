public class Track extends Product{
    private CurveRadius curveRadius;
    private TrackType trackType;
    
    public Track(String productCode, String productName, String manufacturerName,int retailPrice, int stock, Gauge gauge, Scale scale, CurveRadius curveRadius, TrackType trackType){
        super(productCode, productName, manufacturerName, retailPrice, stock, gauge, scale, ProductType.TRACK);
        this.trackType = trackType;
        this.curveRadius = curveRadius;
    }

    public CurveRadius getCurveRadius() {
        return curveRadius;
    }
    public TrackType getTrackType() {
        return trackType;
    }
    public void setCurveRadius(CurveRadius curveRadius) {
        this.curveRadius = curveRadius;
    }
    public void setTrackType(TrackType trackType) {
        this.trackType = trackType;
    }
}
