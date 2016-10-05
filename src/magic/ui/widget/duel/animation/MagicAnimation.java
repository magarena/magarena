package magic.ui.widget.duel.animation;

import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JComponent;

public abstract class MagicAnimation {

    public final AtomicBoolean isRunning = new AtomicBoolean(false);

    private JComponent canvas;

    protected abstract void render(Graphics g);
    protected abstract void play();
    protected abstract void cancel();
    protected abstract void doCancelAction();

    protected void setCanvas(JComponent aCanvas) {
        this.canvas = aCanvas;
    }

    protected JComponent getCanvas() {
        return canvas;
    }

}
