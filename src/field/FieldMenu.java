package field;

import game.view.MenuView;

import java.awt.*;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldMenu extends MenuView{

    private Field field;

    public FieldMenu(Field pField){

        field = pField;

        this.setLayout(null);
        this.addButtons();


    }

    @Override
    public void paintComponent(Graphics graphics) {

        Graphics2D g2d = (Graphics2D) graphics;

        g2d.setColor(Color.GREEN);
        //g2d.drawString("Population: " + field.getPopulation(), 100, 50);
        g2d.setColor(Color.GRAY);
        g2d.drawString("Ressources: " + field.getResources(), 300, 50);

    }

    private void addButtons() {



    }
}
