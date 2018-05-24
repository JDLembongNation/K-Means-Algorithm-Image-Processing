import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * The main class of the K Means Image Algorithm.
 */
public class KMeansAlgorithm {
    private static String fileName;
    /**
     * The main class of the K means algorithm.
     * This includes inputting the image file and then obtaining the RGB bitmap later to be processed by the kMeans method.
     * @param args The user arguments, should really be empty
     */
    public static void main(String[]args) {
        System.out.println("How many colours?");
        Scanner sc = new Scanner(System.in);
        int kClusters = sc.nextInt();
        BufferedImage image = null;
        try{
            File imageFiles = new File("./images");
            File[] imageList = imageFiles.listFiles();
            for(File f: imageList){
                fileName = f.getName();
                image = ImageIO.read(f);
                int height = image.getHeight();
                int width = image.getWidth();
                int rgb;
                int red;
                int green;
                int blue;

                RGB[][] pixelRGB = new RGB[width][height];
                for(int h = 1; h < height; h++){
                    for(int w = 1; w<width; w++){
                        rgb = image.getRGB(w,h);
                        red = (rgb >> 16) & 0x000000FF;
                        green = (rgb >> 8) & 0x000000FF;
                        blue = (rgb) & 0x000000FF;
                        pixelRGB[w][h] = new RGB(red,green,blue);
                    }
                }
                kMeans(pixelRGB, kClusters);
            }
        }catch(FileNotFoundException e) {
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * The actual K Means algorithm.
     * This algorithm will calculate the three most common colours and then will use those colours to refill the image
     * @param pixels The bitmap containing all the pixels in their RGB c;ass
     * @param kvalue The number of different colours the user wants on the image
     * @throws IOException Exceptions when writing to output file.
     */
    private static void kMeans(RGB[][] pixels, int kvalue) throws IOException{
        int kClusters = kvalue;
        KCluster[] centroids = new KCluster[kClusters];
        //Randomly assign 3 clusters
        for(int i = 0; i < kClusters; i++){
            int widthPos = (int)(Math.random() * pixels.length);
            int heightPos = (int) (Math.random() * pixels[0].length);
            centroids[i] = new KCluster(pixels[widthPos][heightPos], widthPos, heightPos);
        }
        for(int i = 1; i < pixels.length; i++){
            for(int j = 1; j < pixels[0].length; j++){
                computingColour(centroids, pixels[i][j]);
            }
        }
        //set centroid mean colour to each of the values in the pixels
        for(int i = 0; i < centroids.length; i++){
            int meanRed = 0;
            int meanGreen = 0;
            int meanBlue = 0;
            int totalCentroids = 0;
            for(int j = 1; j < pixels.length; j++){
                for(int k = 1; k < pixels[0].length;k++){
                    if(pixels[j][k].centroidNumber == i+1){
                        meanRed += pixels[j][k].red;
                        meanGreen += pixels[j][k].green;
                        meanBlue += pixels[j][k].blue;
                        totalCentroids++;
                    }
                }
            }
            centroids[i].setColour(new RGB(meanRed/(totalCentroids), meanGreen/(totalCentroids), meanBlue/(totalCentroids)));
        }
        BufferedImage newImage = new BufferedImage(pixels.length,pixels[0].length, BufferedImage.TYPE_INT_ARGB);
        for(int i = 1; i < pixels.length; i++){
            for(int j = 1; j < pixels[0].length; j++){
               // System.out.println(resultant);
                newImage.setRGB(i,j,new Color(centroids[pixels[i][j].centroidNumber - 1].getColour().red,centroids[pixels[i][j].centroidNumber - 1].getColour().green,centroids[pixels[i][j].centroidNumber - 1].getColour().blue).getRGB());
            }
        }
        File outputFile = new File("./output/"+fileName);
        ImageIO.write(newImage,"jpeg", outputFile);

    }

    /**
     * This method will assign each pixel in the bitmap to each centroid.
     * @param cetnroidValues The RGB sum of all the centroid values, stored in a KCluster Array.
     * @param currentVectorSpace The RGB object that that particular colour space.
     */
    private static void computingColour(KCluster[]cetnroidValues, RGB currentVectorSpace){
        //assign ecah to colour, to compute we calculate distance betwen colours...
        int closestDistance = Math.abs(cetnroidValues[0].colour.rgbSum - currentVectorSpace.rgbSum);
        currentVectorSpace.setCentroidNumber(1);
        for(int i = 1; i < cetnroidValues.length; i++){
            int distance = Math.abs(cetnroidValues[i].colour.rgbSum - currentVectorSpace.rgbSum);
            if(distance < closestDistance){
                closestDistance = distance;
                currentVectorSpace.setCentroidNumber(i+1);
            }
        }
    }
}
