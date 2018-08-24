/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletetris;

import java.awt.Color;

/**
 *
 * @author t0704007
 */
public class TetJ extends Tetromino{

    Color b = Color.BLUE;
   
    @Override
    public Color[][] getUp() {
        return new Color[][]{{b}, {b, b,b},{}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{b}, {b, b,b},{}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{null,null,null}, {b, b,b},{null,null,b}};
           }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{null, b, b}, {null, b,null},{null,b,null}};
    }
    
    
}
