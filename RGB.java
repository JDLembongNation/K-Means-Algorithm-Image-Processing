public class RGB {
    public int red;
    public int green;
    public int blue;
    public int centroidNumber;
    public int rgbSum;
    public RGB(int red, int green, int blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
        rgbSum = red+green+blue;
    }
    public void setCentroidNumber(int centroidNumber){
        this.centroidNumber = centroidNumber;
    }
    public int getCentroidNumber(){
        return centroidNumber;
    }
}
