package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static java.lang.Math.*;
import static java.lang.StrictMath.max;

public class SampleController {
    private int ALPHA = 24;
    private int RED = 16;
    private int GREEN = 8;
    private int BLUE = 0;
    private int x,y;

    public Label helloWorld;
    @FXML
    public ImageView iv;

    @FXML
    public Button boutton1;

    BufferedImage monImage;
    BufferedImage transformedImage;


    public void afficherBuffer(BufferedImage image) {
        iv.setImage(SwingFXUtils.toFXImage(image, null));
    }

    public void ouvrirDefault(){
        try{
            monImage = ImageIO.read(new File("/home/anas/Images/Pie_Man_Epic_Statue_Menu.png"));
            x = monImage.getHeight();
            y = monImage.getWidth();
            afficherBuffer(monImage); } catch (IOException erreur) { }

    }
    @FXML
    public void ouvrir(ActionEvent e){

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("/home/anas/Images/"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null){
            try{
                monImage = ImageIO.read(new File(selectedFile.getAbsolutePath()));
                x = monImage.getHeight();
                y = monImage.getWidth();
                afficherBuffer(monImage);
            } catch (IOException erreur) {

            }
        }
        else
            System.out.println("Please choose a file");
    }

    @FXML
    public void gradientRed(ActionEvent e){
        afficherTableau(gradient(RED),x,y);
    }

    public void gradientGreen(ActionEvent e){
        afficherTableau(gradient(GREEN),x,y);
    }

    public void gradientBlue(ActionEvent e){
        afficherTableau(gradient(BLUE),x,y);
    }

    public int[][] gradient(int colour){

        int[][] gradientImage = new int[y][x];
        int i0; int i1; int intensity;
        // suivant x
        for (int i : IntStream.range(0,y).toArray()){
            for(int j: IntStream.range(0,x-1).toArray()){
                i0 = extract(monImage.getRGB(i,j),colour);
                i1 = extract(monImage.getRGB(i,j+1),colour);
                intensity = (int) Math.pow((i0-i1),2);
                gradientImage[i][j] = intensity;
            }
        }

        //suivant y
        for (int j : IntStream.range(0,x).toArray()){
            for(int i: IntStream.range(0,y-1).toArray()){
                i0 = extract(monImage.getRGB(i,j),colour);
                i1 = extract(monImage.getRGB(i+1,j),colour);
                intensity = (int) Math.pow((i0-i1),2);
                gradientImage[i][j] += intensity;
            }
        }

        //norme du gradient
        for (int j : IntStream.range(0,x).toArray()){
            for(int i: IntStream.range(0,y).toArray()){
                intensity = (int) sqrt(gradientImage[i][j]/2);
                gradientImage[i][j] = rgb(255,intensity,intensity,intensity);
            }
        }

        return gradientImage;
    }

    public void original(ActionEvent e){
        afficherBuffer(monImage);
    }

    public void afficherTableau(int[][] image,int height,int width){

        transformedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for (int i : IntStream.range(0,width).toArray()){
            for(int j: IntStream.range(0,height).toArray()){
                transformedImage.setRGB(i,j,image[i][j]);
            }
        }
        afficherBuffer(transformedImage);

    }

    private int extract(int p,int INT_SHIFT){ return (p>>INT_SHIFT) & 0xff;}
    private int rgb(int a, int r, int g, int b){ return (a<<24) | (r<<16) | (g<<8) | b;}
}

