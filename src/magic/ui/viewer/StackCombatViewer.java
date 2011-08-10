package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class StackCombatViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final StackViewer stackViewer;
	private final CombatViewer combatViewer;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;
	
	public StackCombatViewer(final ViewerInfo viewerInfo,final GameController controller) {
		
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		
		stackViewer=new StackViewer(viewerInfo,controller,false);
		combatViewer=new CombatViewer(viewerInfo,controller);		

		setOpaque(false);
		setLayout(new BorderLayout());

		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.setOpaque(false);
		cardPanel.add(stackViewer,"0");
		cardPanel.add(combatViewer,"1");
		add(cardPanel,BorderLayout.CENTER);
		
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
		
		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_STACK),"Stack");
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_COMBAT),"Combat");
		titleBar.add(tabSelector,BorderLayout.EAST);
	}
	
	public void update() {
		
		stackViewer.update();
		combatViewer.update();

		// Switch to used tab.
		switch (tabSelector.getSelectedTab()) {
			case 0:
				if (stackViewer.isEmpty()&&!combatViewer.isEmpty()) {
					tabSelector.setSelectedTab(1);
				}
				break;
			case 1:
				if (combatViewer.isEmpty()&&!stackViewer.isEmpty()) {
					tabSelector.setSelectedTab(0);
				}
				break;
		}
	}
	
	public void setSelectedTab(final int selectedTab) {
		
		if (selectedTab>=0) {
			tabSelector.setSelectedTab(selectedTab);
		}
	}

	@Override
	public void stateChanged(final ChangeEvent event) {

		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,""+selectedTab);
		switch (selectedTab) {
			case 0:
				titleBar.setText(stackViewer.getTitle());
				break;
			case 1:
				titleBar.setText(combatViewer.getTitle());
				break;
		}
	}
}