package magic.ui.widget.duel.animation;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.ui.helpers.KeyEventAction;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel {

    private MagicAnimation animation = null;

    public AnimationPanel() {

        assert SwingUtilities.isEventDispatchThread();

        setOpaque(false);
        setLayout(null);
        setVisible(false);

        setCancelPreviewOnLeftClick();
        setCancelOnKeyPress();
        setCancelOnResize();
    }

    private void setCancelOnResize() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (animation != null) {
                    animation.cancel();
                }
            }
        });
    }

    private void setCancelPreviewOnLeftClick() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    animation.doCancelAction();
                }
            }
        });
    }

    private void setCancelOnKeyPress() {
        KeyEventAction.doAction(this, () -> animation.doCancelAction())
            .onFocus(0, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE, KeyEvent.VK_RIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        animation.render(g);
    }

    public void playAnimation(MagicAnimation animation) {
        assert SwingUtilities.isEventDispatchThread();
        this.animation = animation;
        animation.setCanvas(this);
        animation.play();
        setVisible(true);
        // IMPORTANT do not remove - SPACE key action will not work otherwise.
        requestFocusInWindow();
    }

}
