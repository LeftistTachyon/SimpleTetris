/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletetris;

import java.awt.Color;

/**
 *
 * @author l0703663
 */
public class TetO extends Tetromino {
    private Color[][] piece;
    private Color col = Color.yellow;
    @Override
    public Color[][] getUp() {
        return new Color[][]{{col,col},{col,col}};
    }

    @Override
    public Color[][] getLeft() {
        return new Color[][]{{col,col},{col,col}};
    }

    @Override
    public Color[][] getDown() {
        return new Color[][]{{col,col},{col,col}};
    }

    @Override
    public Color[][] getRight() {
        return new Color[][]{{col,col},{col,col}};
    }
    
}
