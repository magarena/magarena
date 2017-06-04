package magic.ui.widget.duel.animation;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

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
        getActionMap().put("CancelAction", getCancelAction());
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "CancelAction");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CancelAction");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "CancelAction");
    }

    private AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                animation.doCancelAction();
            }
        };
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
