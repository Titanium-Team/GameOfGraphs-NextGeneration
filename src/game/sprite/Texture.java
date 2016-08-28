package game.sprite;

import game.loading.Loadable;

import java.awt.image.BufferedImage;

public interface Texture extends Loadable {

    /**
     * Gibt das Image zurueck.
     * @return
     */
    BufferedImage getImage();

}
