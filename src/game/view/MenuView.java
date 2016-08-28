package game.view;

import javax.swing.*;
import java.awt.*;

public abstract class MenuView extends JPanel {

    public MenuView() {
        this.setMaximumSize(new Dimension(1280, 180));
        this.setMinimumSize(new Dimension(1280, 180));
        this.setPreferredSize(new Dimension(1280, 180));
    }



}