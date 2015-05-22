package magic.ui.deck.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckCardPoolActionBar extends TexturedPanel {

    public static final String CP_OPTIONBAR = "optionBar";

    private final MigLayout migLayout = new MigLayout();
    private final DeckCardPoolActionPanel actionPanel;
    private final ActionBarButton optionBarButton;

    public DeckCardPoolActionBar(final AbstractAction plusButtonAction, final AbstractAction minusButtonAction) {
        
        final Color refBG = Color.DARK_GRAY;
        final Color thisBG = new Color(refBG.getRed(), refBG.getGreen(), refBG.getBlue(), 230);
        setBackground(thisBG);

        actionPanel = new DeckCardPoolActionPanel(plusButtonAction, minusButtonAction);
        optionBarButton = getOptionBarButton();

        setLayout(migLayout);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, flowy, center, center");
        add(optionBarButton, "pos 5 6, hidemode 3");
        add(actionPanel, "w 100%");
        revalidate();
    }

    private ActionBarButton getOptionBarButton() {
        final ActionBarButton btn = new ActionBarButton(
                IconImages.getIcon(MagicIcon.OPTIONBAR_ICON),
                "Options", "Toggle visibility of options bar.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DeckCardPoolActionBar.this.firePropertyChange(CP_OPTIONBAR, false, true);
                    }
                });
        btn.setPreferredSize(new Dimension(28, 28));
        btn.setMaximumSize(btn.getPreferredSize());
        return btn;
    }

    public int getQuantity() {
        return actionPanel.getQuantity();
    }

    public void setOptionBarVisible(final boolean b) {
        optionBarButton.setVisible(b);
        refreshLayout();
    }

}
