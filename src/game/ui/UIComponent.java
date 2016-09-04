package game.ui;

import de.SweetCode.e.rendering.GameScene;

public abstract class UIComponent<T> extends GameScene {

    private final GameScene gameScene;
    private final Trigger<T> trigger;

    private boolean isEnabled = true;

    /**
     * Der Constructor.
     * @param gameScene Die GameScene in der es gerendert werden soll.
     * @param trigger Der Trigger der aufgerufen werden soll.
     */
    public UIComponent(GameScene gameScene, Trigger<T> trigger) {
        this.gameScene = gameScene;
        this.trigger = trigger;
    }

    /**
     * Gibt zurück ob es aktuell aktiv ist.
     * @return
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Es aktiv ( true ) oder inaktiv ( false ) setzeb.
     * @param isEnabled
     */
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * Gibt den Trigger zurück.
     * @return
     */
    public Trigger<T> getTrigger() {
        return trigger;
    }

    /**
     * Gibt die GameSCene zurück.
     * @return
     */
    public GameScene getGameScene() {
        return gameScene;
    }

}
