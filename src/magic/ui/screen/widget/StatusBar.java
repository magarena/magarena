package magic.ui.screen.widget;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import magic.data.IconImages;
import magic.ui.MagicFrame;
import magic.ui.screen.AbstractScreen;
import magic.ui.screen.interfaces.IOptionsMenu;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class StatusBar extends TexturedPanel {

    private final static ImageIcon optionsIcon = new ImageIcon(IconImages.OPTIONS_ICON);

    private final AbstractScreen magScreen;

    public StatusBar(final AbstractScreen screen0, final MagicFrame frame0) {
        this.magScreen = screen0;
        setMinimumSize(new Dimension(getPreferredSize().width, 50));
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        layoutMagStatusBar();
    }

    private void layoutMagStatusBar() {
        removeAll();
        setLayout(new MigLayout("insets 0 0 0 6, gap 6, flowx, aligny 50%", "[grow, fill][][]"));
        if (magScreen != null) {
            final IStatusBar screen = (IStatusBar)magScreen;
            add(new CaptionPanel(screen.getScreenCaption()));
            if (magScreen.hasOptionsMenu()) {
                add(getOptionsIconButton((IOptionsMenu)magScreen));
            }
        }
        revalidate();
    }

    private JButton getOptionsIconButton(final IOptionsMenu provider) {
        JButton btn = new JButton(optionsIcon);
        btn.setHorizontalAlignment(SwingConstants.RIGHT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Options [ESC]");
        setButtonTransparent(btn);
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                provider.showOptionsMenuOverlay();
            }
        });
        return btn;
    }

    private void setButtonTransparent(final JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
    }

}
