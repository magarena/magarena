package magic.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class MagicMenuScreen extends TexturedPanel {

    public MagicMenuScreen(final MagicFrame frame0, final String title) {

        setOpaque(false);
        setLayout(new MigLayout("insets 0, gap 0, center, center"));

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        MenuPanel menuPanel = new MenuPanel("Testing");
        add(menuPanel);

        frame0.setGlassPane(this);
        setVisible(true);

    }

    /* (non-Javadoc)
     * @see magic.ui.widget.TexturedPanel#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setForeground(Color.WHITE);
        Font f = new Font("Dialog", Font.ITALIC, 22);
        g.setFont(f);
        g.drawString("MagicMenuScreen", 25, 25);
    }

}
