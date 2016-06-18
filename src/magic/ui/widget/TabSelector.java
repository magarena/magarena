package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class TabSelector extends JPanel implements ActionListener {

    private static final Dimension VERTICAL_BUTTON_DIMENSION=new Dimension(24,24);

    private final JPanel buttonPanel;
    private final List<JToggleButton> buttons;
    private final ChangeListener listener;
    private int selectedTab;
    private final Dimension buttonDimension;
    private final Color backgroundColor;
    private boolean isUserClick = false;

    public TabSelector(final ChangeListener listener, final Color backgroundColor0) {

        this.listener=listener;
        this.backgroundColor = (backgroundColor0 == null ? getBackground() : backgroundColor0);
        selectedTab=0;

        setOpaque(false);
        setLayout(new BorderLayout());

        buttonPanel = new TexturedPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        add(buttonPanel, BorderLayout.NORTH);
        buttonDimension = VERTICAL_BUTTON_DIMENSION;

        buttons=new ArrayList<>();

    }

    public TabSelector(final ChangeListener listener) {
        this(listener, null);
    }

    public int getSelectedTab() {

        return selectedTab;
    }

    public void setSelectedTab(final int selectedTab, final boolean showFullScreen) {
        this.selectedTab=selectedTab;
        showTab(buttons.get(selectedTab), showFullScreen);
    }

    public void setSelectedTab(final int selectedTab) {
        setSelectedTab(selectedTab, false);
    }

    public void addTab(final ImageIcon icon,final String toolTip) {

        final JToggleButton button=new JToggleButton(icon);
        button.setToolTipText(null);
        button.setBackground(this.backgroundColor);
        button.setFocusable(false);
        button.setPreferredSize(buttonDimension);
        button.setActionCommand(Integer.toString(buttons.size()));
        button.addActionListener(this);
        buttons.add(button);
        buttonPanel.add(button);

        if (buttons.size()==1) {
            showTab(button);
        }
    }

    private void showTab(final JToggleButton selectedButton) {
        showTab(selectedButton, false);
    }
    private void showTab(final JToggleButton selectedButton, final boolean userClick) {
        this.isUserClick = userClick;
        for (final JToggleButton button : buttons) {
            button.setSelected(button==selectedButton);
        }

        selectedTab=Integer.parseInt(selectedButton.getActionCommand());
        listener.stateChanged(new ChangeEvent(selectedButton));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        showTab((JToggleButton)event.getSource(), true);
    }

    public boolean isUserClick() {
        return isUserClick;
    }

    public void setIsUserClick(final boolean b) {
        this.isUserClick = b;
    }

}
