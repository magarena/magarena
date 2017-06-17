package magic.ui.screen.duel.game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;
import magic.ui.widget.ZoneBackgroundLabel;
import magic.ui.widget.card.AnnotatedCardPanel;
import magic.ui.widget.duel.DuelDialogPanel;
import magic.ui.widget.duel.animation.AnimationPanel;

@SuppressWarnings("serial")
class DuelLayeredPane extends JLayeredPane {

    private final DuelPanel duelPanel;
    private final ZoneBackgroundLabel backgroundLabel;
    private final AnimationPanel animationPanel;
    private final AnnotatedCardPanel imageCardViewer;
    private final DuelDialogPanel dialogPanel;

    DuelLayeredPane() {

        this.duelPanel = new DuelPanel();

        backgroundLabel = new ZoneBackgroundLabel();
        duelPanel.setBackgroundLabel(backgroundLabel);

        animationPanel = new AnimationPanel();
        dialogPanel = new DuelDialogPanel();
        imageCardViewer = new AnnotatedCardPanel();

        updateLayout();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                resizeComponents();
            }
        });

        setVisible(false);

    }

    private void updateLayout() {
        removeAll();
        // game board layers from bottom to top.
        add(backgroundLabel, new Integer(0));    // background image.
        add(duelPanel, new Integer(1));          // sidebar and battlefield.
        add(animationPanel, new Integer(2));     // animations.
        add(imageCardViewer, new Integer(3));    // card popup.
        add(dialogPanel, new Integer(4));        // dialogs.
        revalidate();
    }

    private void resizeComponents() {
        final Dimension size = getSize();
        for (Component component : getComponents()) {
            component.setSize(size);
        }
    }

    DuelPanel getDuelPanel() {
        return duelPanel;
    }

    void updateView() {
//        backgroundLabel.setImage(GeneralConfig.getInstance().getTextView() == false);
        duelPanel.updateView();
//        setVisible(true);
    }

    @Override
    public void requestFocus() {
        if (duelPanel != null) {
            duelPanel.requestFocus();
        }
    }

    @Override
    public boolean requestFocusInWindow() {
        return duelPanel.requestFocusInWindow();
    }

    DuelDialogPanel getDialogPanel() {
        return dialogPanel;
    }

    AnimationPanel getAnimationPanel() {
        return animationPanel;
    }

    AnnotatedCardPanel getCardViewer() {
        return imageCardViewer;
    }

}
