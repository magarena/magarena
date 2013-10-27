package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicGame;
import magic.ui.GameController;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

public class GameDuelViewer extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final GameViewer gameViewer;
    private final DuelViewer duelViewer;
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final TitleBar titleBar;
    private final TabSelector tabSelector;
    private final PhaseStepViewer phaseStepViewer;

    public GameDuelViewer(final MagicGame game,final GameController controller) {
        gameViewer=new GameViewer(game,controller);
        duelViewer=new DuelViewer(game.getDuel());

        phaseStepViewer = new PhaseStepViewer();
        phaseStepViewer.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK));
        phaseStepViewer.setOpaque(false);

        setSize(320,125);
        setLayout(new BorderLayout());

        titleBar=new TitleBar("");
        add(titleBar,BorderLayout.NORTH);

        cardLayout=new CardLayout();
        cardPanel=new JPanel(cardLayout);
        cardPanel.add(gameViewer,"0");
        cardPanel.add(duelViewer,"1");
        add(cardPanel,BorderLayout.CENTER);

        tabSelector=new TabSelector(this,false);
        tabSelector.addTab(IconImages.MESSAGE,"Message");
        tabSelector.addTab(IconImages.PROGRESS,"Progress");
        tabSelector.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
        titleBar.add(tabSelector,BorderLayout.EAST);
        titleBar.add(phaseStepViewer,BorderLayout.SOUTH);

        //add(phaseStepViewer, BorderLayout.SOUTH);
    }

    public GameViewer getGameViewer() {
        return gameViewer;
    }

    public void update() {
        switch (tabSelector.getSelectedTab()) {
            case 0:
                gameViewer.setTitle(titleBar);
                phaseStepViewer.setPhaseStep(gameViewer.getMagicPhaseType());
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
