package game.view;

import javax.swing.*;
import java.awt.*;

public abstract class View extends JPanel  {

    private MenuView viewMenu = null;

    public View(MenuView viewMenu) {
        this.viewMenu = viewMenu;

        this.setMaximumSize(new Dimension(1280, 540));
        this.setMinimumSize(new Dimension(1280, 540));
        this.setPreferredSize(new Dimension(1280, 540));

    }

    /**
     * Gibt die zughoerige MenuView zurueck
     * @return
     */
    public MenuView getMenuView() {
        return this.viewMenu;
    }


    /**
     * Veraendert das aktuelle Menue der View.
     * @param menuView
     */
    public void changeMenu(MenuView menuView) {
        this.viewMenu = menuView;
    }


}