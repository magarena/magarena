package magic.ui.widget.duel.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
    private boolean isMouseOver = false;

    public UserActionPanel(final SwingGameController controller) {

        this.controller=controller;

        setMinimumSize(new Dimension(0, 114));
        setOpaque(false);

        final JLabel emptyLabel=new JLabel("");

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        busyItem = new ImageThrobber.Builder(BUSY_IMAGE)
            .antiAlias(true)
            .spinPeriod(3000)
            .build();

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

        undoButton=new JButton(MagicImages.getIcon(MagicIcon.UNDO));
        undoButton.setMargin(new Insets(1,1,1,1));
        undoButton.setIconTextGap(2);
        undoButton.setEnabled(false);
        undoButton.setFocusable(false);
        undoButton.addActionListener(this);

        actionPanel=new JPanel();
        actionCardLayout=new CardLayout();
        actionPanel.setLayout(actionCardLayout);
        actionPanel.setOpaque(false);

        actionPanel.add(emptyLabel,"0");
        actionPanel.add(imageLabel, "4");
        actionPanel.add(busyItem,"1");
        actionPanel.add(actionButton,"2");

        final JPanel rightPanel=new JPanel(new GridLayout(2,1,0,2));
        rightPanel.setPreferredSize(new Dimension(60,0));
        rightPanel.add(actionPanel);
        rightPanel.add(undoButton);
        rightPanel.setOpaque(false);

        contentPanel=new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);

        final JScrollPane scroller = new JScrollPane();
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getViewport().setOpaque(false);
        scroller.getViewport().add(contentPanel);

        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
        add(rightPanel,BorderLayout.EAST);

        disableButton(false);

        // add mouse over listeners used to display the card image
        // associated with the current choice if applicable.
        setCardPopupOnMouseOverListener();
        setComponentOnMouseOverListener(scroller);
        setComponentOnMouseOverListener(rightPanel);
        setComponentOnMouseOverListener(actionButton);
        setComponentOnMouseOverListener(undoButton);
    }

    private void setCardPopupOnMouseOverListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isMouseOver) {
                    isMouseOver = true;
                    controller.showChoiceCardPopup();
                    controller.getPlayerZoneViewer().showChoiceViewerIfActive();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Component c = SwingUtilities.getDeepestComponentAt(
                    e.getComponent(),
                    e.getX(),
                    e.getY()
                );
                if (c == null) {
                    controller.hideInfoNoDelay();
                    isMouseOver = false;
                }
            }
        });
    }

    private void setComponentOnMouseOverListener(JComponent c) {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                UserActionPanel.this.dispatchEvent(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                UserActionPanel.this.dispatchEvent(e);
            }
        });
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
