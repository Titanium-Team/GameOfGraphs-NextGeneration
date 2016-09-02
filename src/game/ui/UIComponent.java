package game.ui;

import de.SweetCode.e.rendering.GameScene;

public abstract class UIComponent<T> extends GameScene {

    private final GameScene gameScene;
    private final Trigger<T> trigger;

    private boolean isEnabled = true;

    public UIComponent(GameScene gameScene, Trigger<T> trigger) {
        this.gameScene = gameScene;
        this.trigger = trigger;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Trigger<T> getTrigger() {
        return trigger;
    }

    public GameScene getGameScene() {
        return gameScene;
    }

}
