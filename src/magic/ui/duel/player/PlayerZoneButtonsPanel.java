package magic.ui.duel.player;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.model.MagicPlayerZone;
import magic.ui.duel.viewer.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerZoneButtonsPanel extends JPanel {

    private static ButtonGroup buttonGroup = new ButtonGroup();

    private final ZoneToggleButton libraryZoneButton;
    private final ZoneToggleButton handZoneButton;
    private final ZoneToggleButton graveyardZoneButton;
    private final ZoneToggleButton exileZoneButton;
    private final List<IZoneButtonListener> zoneButtonlisteners = new ArrayList<>();
    private PlayerViewerInfo playerInfo;

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo) {

        this.playerInfo = playerInfo;

        setLayout(new MigLayout("insets 0 2 0 0"));

        libraryZoneButton = getZoneToggleButton(
                MagicPlayerZone.LIBRARY, MagicIcon.LIBRARY_ZONE, playerInfo.library.size(), "Library", false, true);
        handZoneButton = getZoneToggleButton(
                MagicPlayerZone.HAND, MagicIcon.HAND_ZONE, playerInfo.hand.size(), "Hand", false, true);
        graveyardZoneButton = getZoneToggleButton(
                MagicPlayerZone.GRAVEYARD, MagicIcon.GRAVEYARD_ZONE, playerInfo.graveyard.size(), "Graveyard", false, true);
        exileZoneButton = getZoneToggleButton(
                MagicPlayerZone.EXILE, MagicIcon.EXILE_ZONE, playerInfo.exile.size(), "Exile", false, true);

        add(libraryZoneButton);
        add(handZoneButton);
        add(graveyardZoneButton);
        add(exileZoneButton);
//        add(new JLabel(IconImages.getIcon(MagicIcon.WAITING)));

        if (buttonGroup.getButtonCount() > 0) {
            buttonGroup.getElements().nextElement().setSelected(true);
        }

        setOpaque(false);

    }

    public synchronized void addZoneButtonListener(final IZoneButtonListener listener) {
        zoneButtonlisteners.add(listener);
    }

    public synchronized void removeZoneButtonListener(final IZoneButtonListener listener) {
        zoneButtonlisteners.remove(listener);
    }

    private ZoneToggleButton getZoneToggleButton(
            final MagicPlayerZone zone,
            final MagicIcon icon,
            final int cardCount,
            final String tooltip,
            final boolean isActive,
            final boolean isAnimated) {

        final ZoneToggleButton btn = new ZoneToggleButton(zone, icon, cardCount, isActive, isAnimated);
        btn.setToolTipText(tooltip);
        if (isActive) {
            buttonGroup.add(btn);
        } else {
            btn.setEnabled(false);
        }
//        btn.addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent ev) {
//                final JToggleButton btn = (JToggleButton) ev.getSource();
//                System.out.println("isSelected=" + btn.isSelected());
//            }
//        });
        btn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ev) {
                if (ev.getStateChange() == ItemEvent.SELECTED) {
                    final ZoneToggleButton b = (ZoneToggleButton) ev.getSource();
                    notifyToggleButtonSelected(b.getPlayerZone());
                }
            }
        });
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent ev) {
                final JToggleButton btn = (JToggleButton) ev.getSource();
                if (btn.isEnabled()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mousePressed(MouseEvent ev) {
                final ZoneToggleButton b = (ZoneToggleButton) ev.getSource();
                if (b.isSelected()) {
                    notifyToggleButtonSelectedClicked(b.getPlayerZone());
                }
            }
        });
        return btn;
    }

    private void notifyToggleButtonSelectedClicked(final MagicPlayerZone zone) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (final IZoneButtonListener listener : zoneButtonlisteners) {
                    listener.playerZoneSelectedClicked(playerInfo, zone);
                }
            }
        });
    }

    private void notifyToggleButtonSelected(final MagicPlayerZone zone) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                for (final IZoneButtonListener listener : zoneButtonlisteners) {
                    listener.playerZoneSelected(playerInfo, zone);
                }
            }
        });
    }

    void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.playerInfo = playerInfo;
        if (playerInfo != null) {
            handZoneButton.setNumberOfCardsInZone(playerInfo.hand.size());
            libraryZoneButton.setNumberOfCardsInZone(playerInfo.library.size());
            graveyardZoneButton.setNumberOfCardsInZone(playerInfo.graveyard.size());
            exileZoneButton.setNumberOfCardsInZone(playerInfo.exile.size());
        }
    }

    public static void clearButtonGroup() {
        buttonGroup = new ButtonGroup();
    }

}
