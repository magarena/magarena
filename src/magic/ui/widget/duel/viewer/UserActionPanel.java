package magic.ui.widget.duel.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.message.TextLabel;
import magic.ui.widget.throbber.AbstractThrobber;
import magic.ui.widget.throbber.ImageThrobber;

@SuppressWarnings("serial")
public class UserActionPanel extends JPanel implements ActionListener {

    private static final BufferedImage BUSY_IMAGE = ImageHelper.getBufferedImage(
        ImageHelper.getRecoloredIcon(MagicIcon.AI_THINKING, Color.BLACK, Color.GRAY)
    );

    public static final int TEXT_WIDTH=230;

    private final SwingGameController controller;
    private final JButton actionButton;
    private final JButton undoButton;
    private final JPanel actionPanel;
    private final CardLayout actionCardLayout;
    private final JPanel contentPanel;
    private boolean actionEnabled;
    private final AbstractThrobber busyItem;
    private final JLabel imageLabel = new JLabel();
    private String actionPanelId = "0";

    public UserActionPanel(final SwingGameController controller) {

        this.controller=controller;

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(0, 114));
        setOpaque(false);

        actionPanel=new JPanel();
        actionPanel.setOpaque(false);
        actionCardLayout=new CardLayout();
        actionPanel.setLayout(actionCardLayout);

        final JLabel emptyLabel=new JLabel("");
        actionPanel.add(emptyLabel,"0");

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(imageLabel, "4");

        busyItem = new ImageThrobber.Builder(BUSY_IMAGE)
            .antiAlias(true)
            .spinPeriod(3000)
            .build();
        actionPanel.add(busyItem,"1");

        actionButton=new JButton();
        actionButton.setIcon(MagicImages.getIcon(MagicIcon.FORWARD));
        actionButton.setFocusable(false);
        actionButton.addActionListener(this);
        actionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    controller.passKeyPressed();
                }
            }
        });
        actionPanel.add(actionButton,"2");

        undoButton=new JButton(MagicImages.getIcon(MagicIcon.UNDO));
        undoButton.setMargin(new Insets(1,1,1,1));
        undoButton.setIconTextGap(2);
        undoButton.setEnabled(false);
        undoButton.setFocusable(false);
        undoButton.addActionListener(this);

        final JPanel rightPanel=new JPanel(new GridLayout(2,1,0,2));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(60,0));
        rightPanel.add(actionPanel);
        rightPanel.add(undoButton);
        add(rightPanel,BorderLayout.EAST);

        contentPanel=new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                controller.showChoiceCardPopup();
                controller.getPlayerZoneViewer().showChoiceViewerIfActive();
            }
        });

        final JScrollPane scroller = new JScrollPane();
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getViewport().setOpaque(false);
        scroller.getViewport().add(contentPanel);
        add(scroller, BorderLayout.CENTER);

        disableButton(false);

        // Suppress mouse events reaching parent container.
        // This will apply to any child components which have not
        // registered their own mouse listener.
        // Effectively prevents the card popup from being closed
        // (by DuelPanel) while mouse cursor is inside UserActionPanel.
        addMouseListener(new MouseAdapter(){});

    }

    public void clearContentPanel() {
        contentPanel.removeAll();
    }

    private void setActionPanel(String id) {
        this.actionPanelId = id;
        actionCardLayout.show(actionPanel, id);
    }

    public void setContentPanel(final JComponent newContent) {
        clearContentPanel();
        contentPanel.add(newContent,BorderLayout.CENTER);
        revalidate();
        repaint();
        if (controller.getSourceCardDefinition() != MagicCardDefinition.UNKNOWN) {
            BufferedImage image = MagicImages.getCardImage(controller.getSourceCardDefinition());
            ImageIcon icon = new ImageIcon(ImageHelper.scale(image, 30, 48));
            imageLabel.setIcon(icon);
            setActionPanel("4");
        } else {
            setActionPanel(actionPanelId);
        }
    }

    public void showMessage(final String message) {
        final TextLabel messageLabel=new TextLabel(message,TEXT_WIDTH,true);
        setContentPanel(messageLabel);
    }

    public boolean isActionEnabled() {
        return actionEnabled;
    }

    public boolean isUndoEnabled() {
        return undoButton.isEnabled();
    }

    public void enableUndoButton(final boolean thinking) {
        final int undoPoints=controller.getGameViewerInfo().getUndoPoints();
        final boolean allowUndo=undoPoints>0&&!thinking;
        undoButton.setEnabled(allowUndo);
    }

    public void enableButton() {
        actionEnabled=true;
        enableUndoButton(false);
        setActionPanel("2");
        repaint();
    }

    public void disableButton(final boolean thinking) {
        actionEnabled=false;
        enableUndoButton(thinking);
        setActionPanel(thinking ? "1" : "0");
        repaint();
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==actionButton) {
            final boolean isShiftClick = (event.getModifiers() & InputEvent.SHIFT_MASK) != 0;
            if (isShiftClick) {
                controller.passKeyPressed();
            } else {
                controller.actionClicked();
            }
        } else if (source==undoButton) {
            controller.undoClicked();
        }
    }

}
