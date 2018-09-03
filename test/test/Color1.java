package test;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Color1 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        while(true) {
            String s = input.nextLine();
            if(s.equalsIgnoreCase("EXIT")) {
                System.exit(0);
            } else {
                BufferedImage image;
                try {
                    image = ImageIO.read(new File(s));
                } catch (IOException ex) {
                    System.err.println("Invalid image adress");
                    continue;
                }
                Raster r = image.getData();
                double red = 0, green = 0, blue = 0;
                for(int x = 0; x < r.getHeight(); x++) {
                    for(int y = 0; y < r.getWidth(); y++) {
                        red += r.getSample(x, y, 0);
                        green += r.getSample(x, y, 1);
                        blue += r.getSample(x, y, 2);
                    }
                }
                double totalPixels = r.getHeight() * r.getWidth();
                red /= totalPixels;
                blue /= totalPixels;
                green /= totalPixels;
                
                /*System.out.println("new Color(" + red + ", " + blue + ", " + 
                        green + ")");*/
                System.out.printf("new Color(%.1f, %.1f, %.1f)%n", red, green, blue);
            }
        }
    }
}