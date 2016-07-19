package magic.ui.duel.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.duel.SwingGameController;
import magic.ui.message.TextLabel;

@SuppressWarnings("serial")
public class UserActionPanel extends JPanel implements ActionListener {

    public static final int TEXT_WIDTH=230;

    private final SwingGameController controller;
    private final JButton actionButton;
    private final JButton undoButton;
    private final JPanel actionPanel;
    private final CardLayout actionCardLayout;
    private final JPanel contentPanel;
    private boolean actionEnabled;

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

        final JLabel busyLabel=new JLabel(MagicImages.getIcon(MagicIcon.BUSY));
        busyLabel.setHorizontalAlignment(JLabel.CENTER);
        actionPanel.add(busyLabel,"1");

        actionButton=new JButton();
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

    public void setContentPanel(final JComponent newContent) {
        clearContentPanel();
        contentPanel.add(newContent,BorderLayout.CENTER);
        revalidate();
        repaint();
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
        final int undoPoints=controller.getViewerInfo().getUndoPoints();
        final boolean allowUndo=undoPoints>0&&!thinking;
        undoButton.setEnabled(allowUndo);
    }

    public void enableButton(final ImageIcon icon) {
        actionEnabled=true;
        enableUndoButton(false);
        actionButton.setIcon(icon);
        actionCardLayout.show(actionPanel,"2");
        repaint();
    }

    public void disableButton(final boolean thinking) {
        actionEnabled=false;
        enableUndoButton(thinking);
        actionCardLayout.show(actionPanel,thinking?"1":"0");
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

    /**
     * Gets the avatar portrait of the current player sized specifically
     * for use with the GameStatusPanel component.
     */
    public ImageIcon getTurnSizedPlayerAvatar() {
        return controller.getViewerInfo().getPriorityPlayer().getAvatar();
    }

}
