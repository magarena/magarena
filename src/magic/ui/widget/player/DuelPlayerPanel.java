package magic.ui.widget.player;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;
import magic.MagicUtility;
import magic.data.DuelConfig;
import magic.model.player.HumanPlayer;
import magic.model.player.PlayerProfile;
import magic.ui.MagicFrame;
import magic.ui.screen.interfaces.IPlayerProfileConsumer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class DuelPlayerPanel
    extends TexturedPanel
    implements IPlayerProfileConsumer {

    private final MagicFrame frame;
    private PlayerProfilePanel playerProfilePanel;
    private PlayerProfile playerProfile;

    // CTR
    public DuelPlayerPanel(final MagicFrame frame, final PlayerProfile playerProfile) {
        this.frame = frame;
        this.playerProfile = playerProfile;
        this.playerProfilePanel = getPlayerProfilePanel();
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
        doMigLayout();
        addMouseListener(getMouseAdapter());
    }

    private MouseAdapter getMouseAdapter() {
       return new MouseAdapter() {
           @Override
           public void mouseReleased(MouseEvent e) {
               MagicUtility.setBusyMouseCursor(true);
               selectNewProfile();
               mouseExited(e);
               MagicUtility.setBusyMouseCursor(false);
           }
           @Override
           public void mouseEntered(MouseEvent e) {
               setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
               setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
               setBackground(FontsAndBorders.MENUPANEL_COLOR);
           }
           @Override
           public void mouseExited(MouseEvent e) {
               setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
               setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
               setBackground(FontsAndBorders.MAGSCREEN_BAR_COLOR);
           }
       };
    }

    private void doMigLayout() {
        removeAll();
        setLayout(new MigLayout("flowy, gapy 10"));
        add(playerProfilePanel, "w 100%, h 70!");
        add(getStatisticsPanel(), "w 100%, h 100%");
    }

    private JPanel getStatisticsPanel() {
        final JPanel panel = new JPanel(new MigLayout("insets 0 6 0 0"));
        panel.setOpaque(false);
        final JTextArea textArea = new JTextArea(playerProfile.getStats().toString());
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setTabSize(16);
        textArea.setBackground(new Color(0,0,0,1));
        textArea.setBorder(null);
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
            @Override
            public void mouseEntered(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
            @Override
            public void mouseExited(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
        });
        panel.add(textArea, "w 100%, h 100%");
        return panel;
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public void selectNewProfile() {
        if (playerProfile instanceof HumanPlayer) {
            frame.showSelectHumanPlayerScreen(this, playerProfile);
        } else {
            frame.showSelectAiProfileScreen(this, playerProfile);
        }
    }

    /* (non-Javadoc)
     * @see magic.ui.screen.duel.IPlayerProfileConsumer#setPlayerProfile(magic.model.player.PlayerProfile)
     */
    @Override
    public void setPlayerProfile(PlayerProfile selectedPlayerProfile) {
        if (selectedPlayerProfile != null) {
            playerProfile = selectedPlayerProfile;
            if (selectedPlayerProfile instanceof HumanPlayer) {
                DuelConfig.getInstance().setPlayerProfile(0, selectedPlayerProfile);
            } else {
                DuelConfig.getInstance().setPlayerProfile(1, selectedPlayerProfile);
            }
            DuelConfig.getInstance().save();
        }
        playerProfilePanel = new PlayerProfilePanel(playerProfile);
        doMigLayout();
    }

    private PlayerProfilePanel getPlayerProfilePanel() {
        final PlayerProfilePanel panel = new PlayerProfilePanel(playerProfile);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
            @Override
            public void mouseEntered(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
            @Override
            public void mouseExited(MouseEvent e) { DuelPlayerPanel.this.dispatchEvent(e); }
        });
        return panel;
    }

}
