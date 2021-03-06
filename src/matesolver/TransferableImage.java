/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matesolver;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 *
 * @author streblow
 */
public class TransferableImage implements Transferable {

    private Image image;

    public TransferableImage (Image image)
    {
        this.image = image;
    }

    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException
    {
        if (isDataFlavorSupported(flavor))
        {
            return image;
        }
        else
        {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    public boolean isDataFlavorSupported (DataFlavor flavor)
    {
        return flavor == DataFlavor.imageFlavor;
    }

    public DataFlavor[] getTransferDataFlavors ()
    {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

}
