public class KCluster {
    public RGB colour;
    public int x;
    public int y;
    public KCluster(RGB colour, int x, int y){
        this.colour = colour;
        this.x = x;
        this.y = y;
    }

    public RGB getColour() {
        return colour;
    }

    public void setColour(RGB colour) {
        this.colour = colour;
    }
}
