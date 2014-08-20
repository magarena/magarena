package magic.ui.viewer;

import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.phase.MagicPhaseType;
import magic.ui.GameController;
import magic.ui.widget.TextLabel;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameViewer extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final int TEXT_WIDTH=230;

    private final MagicGame game;
    private final GameController controller;
    private final JButton actionButton;
    private final JButton undoButton;
    private final JPanel actionPanel;
    private final CardLayout actionCardLayout;
    private final JPanel contentPanel;
    private boolean actionEnabled;

    public GameViewer(final MagicGame game,final GameController controller) {

        this.game=game;
        this.controller=controller;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK));

        actionPanel=new JPanel();
        actionPanel.setOpaque(false);
        actionCardLayout=new CardLayout();
        actionPanel.setLayout(actionCardLayout);

        final JLabel emptyLabel=new JLabel("");
        actionPanel.add(emptyLabel,"0");

        final JLabel busyLabel=new JLabel(IconImages.BUSY);
        busyLabel.setHorizontalAlignment(JLabel.CENTER);
        actionPanel.add(busyLabel,"1");

        actionButton=new JButton();
        actionButton.setFocusable(false);
        actionButton.addActionListener(this);
        actionPanel.add(actionButton,"2");

        undoButton=new JButton(IconImages.UNDO);
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

        final JScrollPane scroller = new JScrollPane();
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.getViewport().setOpaque(false);
        scroller.getViewport().add(contentPanel);
        add(scroller, BorderLayout.CENTER);

        disableButton(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent event) {
                final MagicCardDefinition cardDefinition=controller.getSourceCardDefinition();
                if (cardDefinition!=MagicCardDefinition.UNKNOWN) {
                    if (!GeneralConfig.getInstance().getTextView()) {
                        final Point point=getLocationOnScreen();
                        controller.viewInfoAbove(cardDefinition,0,new Rectangle(point.x,point.y-20,getWidth(),getHeight()));
                    }
                }
            }
            @Override
            public void mouseExited(final MouseEvent event) {
                controller.hideInfo();
            }
        });
    }

    public void setTitle(final TitleBar titleBar) {
        titleBar.setText("Turn " + game.getTurn() + " : " + game.getPhase().getType().getName());
    }
    public String getTurnCaption() {
        return "Turn " + game.getTurn() + " : " + game.getPhase().getType().getName();
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

    public MagicGame getGame() {
        return game;
    }

    public boolean isActionEnabled() {
        return actionEnabled;
    }

    public boolean isUndoEnabled() {
        return undoButton.isEnabled();
    }

    public void enableUndoButton(final boolean thinking) {
        final int undoPoints=game.getNrOfUndoPoints();
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
            controller.actionClicked();
        } else if (source==undoButton) {
            controller.undoClicked();
        }
    }

    public MagicPhaseType getMagicPhaseType() {
        return game.getPhase().getType();
    }

    /**
     * Gets the avatar portrait of the current player sized specifically
     * for use with the GameDuelViewer component.
     */
    public ImageIcon getTurnSizedPlayerAvatar() {
        return game.getTurnPlayer().getPlayerDefinition().getAvatar().getIcon(4);
    }

}
