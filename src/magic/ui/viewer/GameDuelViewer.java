package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicGame;
import magic.ui.GameController;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

public class GameDuelViewer extends TexturedPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final GameViewer gameViewer;
    private final DuelViewer duelViewer;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;
    private final PhaseStepViewer phaseStepViewer;
    private final JLabel playerAvatar = new JLabel();

    public GameDuelViewer(final MagicGame game,final GameController controller) {

        gameViewer=new GameViewer(game,controller);
        duelViewer=new DuelViewer(game.getDuel());
        gameViewer.setOpaque(false);
        duelViewer.setOpaque(false);

        phaseStepViewer = new PhaseStepViewer();
        phaseStepViewer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        phaseStepViewer.setOpaque(false);

        playerAvatar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));

        //setSize(320,125);
        setLayout(new BorderLayout());

        titleBar=new TitleBar("");

        cardLayout=new CardLayout();
        cardPanel=new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(gameViewer,"0");
        cardPanel.add(duelViewer,"1");
        add(cardPanel,BorderLayout.CENTER);

        tabSelector=new TabSelector(this, false, titleBar.getBackground());
        tabSelector.addTab(IconImages.MESSAGE,"Message");
        tabSelector.addTab(IconImages.PROGRESS,"Progress");
        titleBar.add(tabSelector,BorderLayout.EAST);

        JPanel titlePanel = new JPanel(new MigLayout("insets 0, gap 0"));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
        titlePanel.add(playerAvatar, "w 40px!, h 40px!, cell 1 1 1 2");
        titlePanel.add(titleBar, "w 100%, h 20px!, cell 2 1");
        titlePanel.add(phaseStepViewer, "w 100%, h 20px!, cell 2 2");

        add(titlePanel, BorderLayout.NORTH);

    }



    public GameViewer getGameViewer() {
        return gameViewer;
    }

    public void update() {
        switch (tabSelector.getSelectedTab()) {
            case 0:
                gameViewer.setTitle(titleBar);
                phaseStepViewer.setPhaseStep(gameViewer.getMagicPhaseType());
                playerAvatar.setIcon(gameViewer.getTurnSizedPlayerAvatar());
                break;
            case 1:
                DuelViewer.setTitle(titleBar);
                break;
        }
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        final int selectedTab=tabSelector.getSelectedTab();
        cardLayout.show(cardPanel,Integer.toString(selectedTab));
        update();
    }
}
