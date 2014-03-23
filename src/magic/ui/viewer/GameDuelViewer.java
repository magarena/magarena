package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicGame;
import magic.ui.GameController;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class GameDuelViewer extends TexturedPanel implements ChangeListener {

    private final GameViewer gameViewer;
    private final TitleBar titleBar;
    private final PhaseStepViewer phaseStepViewer;
    private final JLabel playerAvatar = new JLabel();
    private final MagicGame game;
    private final JLabel turnLabel = new JLabel();
    private final JLabel playerLabel = new JLabel();

    public GameDuelViewer(final MagicGame game0,final GameController controller) {

        this.game = game0;

        gameViewer=new GameViewer(game0,controller);
        gameViewer.setOpaque(false);

        phaseStepViewer = new PhaseStepViewer();
        phaseStepViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        phaseStepViewer.setOpaque(false);

        setLayout(new BorderLayout());

        titleBar=new TitleBar("");

        turnLabel.setForeground(Color.WHITE);
        playerLabel.setForeground(Color.WHITE);

        add(gameViewer, BorderLayout.CENTER);

        JPanel mainTitlePanel = new JPanel(new MigLayout("insets 0, gap 0, flowy"));
        mainTitlePanel.setOpaque(false);

        JPanel titlePanel = new JPanel(new MigLayout("insets 0, gap 0"));
        titlePanel.setOpaque(true);
        titlePanel.setBackground(titleBar.getBackground());
        titlePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        titlePanel.add(playerAvatar, "w 54px!, h 54px!, cell 1 1 1 3, gapright 4");
        titlePanel.add(getGameCaption(), "w 100%, h 17px!, cell 2 1");
        titlePanel.add(playerLabel, "w 100%, h 18px!, cell 2 2");
        titlePanel.add(turnLabel, "w 100%, h 17px!, cell 2 3, top");
        titlePanel.add(getOptionsIconButton(), "w 32!, h 32!, cell 3 1 1 3, gapright 10");

        mainTitlePanel.add(titlePanel);
        mainTitlePanel.add(phaseStepViewer);

        add(mainTitlePanel, BorderLayout.NORTH);

    }

    private JLabel getGameCaption() {
        final JLabel lbl = new JLabel();
        lbl.setText(
                "Game " + game.getDuel().getGameNr() +
                " of " + game.getDuel().getConfiguration().getNrOfGames());
        lbl.setForeground(Color.WHITE);
        lbl.setOpaque(true);
        lbl.setBackground(titleBar.getBackground());
        return lbl;
    }

    public GameViewer getGameViewer() {
        return gameViewer;
    }

    public void update() {
        gameViewer.setTitle(titleBar);
        turnLabel.setText(gameViewer.getTurnCaption());
        playerLabel.setText(game.getTurnPlayer().getName());
        phaseStepViewer.setPhaseStep(gameViewer.getMagicPhaseType());
        playerAvatar.setIcon(gameViewer.getTurnSizedPlayerAvatar());
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        update();
    }

    private final static ImageIcon optionsIcon = IconImages.OPTIONS_ICON;

    private JButton getOptionsIconButton() {
        JButton btn = new JButton(optionsIcon);
        btn.setHorizontalAlignment(SwingConstants.RIGHT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Options [ESC]");
        setButtonTransparent(btn);
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOptionsMenu();
            }
        });
        return btn;
    }

    private void showOptionsMenu() {
        try {
            Robot robot;
            robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (AWTException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void setButtonTransparent(final JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
    }

}
