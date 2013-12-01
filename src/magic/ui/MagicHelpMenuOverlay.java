package magic.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MagicHelpMenuOverlay extends TexturedPanel implements IMagicHelpOverlay {

    public MagicHelpMenuOverlay(final MagicFrame frame0) {

        setLayout(new MigLayout("insets 0, gap 0, flowy, center, center"));
        setBackground(new Color(0,0,0,150));

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        final MagicHelpMenu optionsPanel = new MagicHelpMenu(frame0, this, true);
        optionsPanel.setBackground(new Color(0, 0, 0, 240));
        add(optionsPanel);

        frame0.setGlassPane(this);
        setVisible(true);

    }

    /* (non-Javadoc)
     * @see magic.ui.IMagicHelpOverlay#actionPerformed()
     */
    @Override
    public void actionPerformed() {
        setVisible(false);
    }

}
