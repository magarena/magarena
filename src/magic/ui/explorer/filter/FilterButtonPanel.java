package magic.ui.explorer.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
abstract class FilterButtonPanel extends JPanel implements ActionListener {

    // translatable strings
    private static final String _S1 = "Match any selected";
    private static final String _S2 = "Match all selected";
    private static final String _S3 = "Exclude selected";

    static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    private static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);

    protected final String[] FILTER_CHOICES = {
        UiString.get(_S1),
        UiString.get(_S2),
        UiString.get(_S3)
    };

    private final JButton filterButton;
    protected final JDialog dialog;
    private boolean popupJustToggled = false;

    protected JCheckBox[] checkOptions;
    protected JRadioButton[] radioOptions;

    protected abstract boolean isCardValid(final MagicCardDefinition card, final int i);

    FilterButtonPanel(String title, String tooltip) {

        this.filterButton = new FilterButton(title, this);
        this.filterButton.setToolTipText(tooltip);

        this.dialog = new PopupDialog();
        this.dialog.addWindowFocusListener(new WindowAdapter() {
            private final Timer timer = new Timer();
            @Override
            public void windowLostFocus(WindowEvent e) {
                popupJustToggled = true;
                // don't allow clicks on hide button to reshow popup
                // immediately by disabling response for < 1 s
                timer.schedule(new ResponsePreventer(), 300);
                if (dialog.isVisible()) {
                    dialog.setVisible(false);
                }
            }
        });
        setEscapeKeyAction();

        setLayout(new MigLayout("insets 0, fill", "fill", "fill"));
        add(filterButton);

        setOpaque(false);
    }

    FilterButtonPanel(String title) {
        this(title, null);
    }

    private void setEscapeKeyAction() {
        JRootPane root = dialog.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");

        @SuppressWarnings("serial")
        final AbstractAction closeAction = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dialog.setVisible(false);
            }
        };
        root.getActionMap().put("closeDialog", closeAction);
    }

    private void showPopup() {
        // set location relative to button
        final Point location = this.getLocation();
        SwingUtilities.convertPointToScreen(location, this.getParent());
        location.translate(0, this.getHeight());
        dialog.setLocation(location);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (popupJustToggled) {
            // focus event just hid popup -> this event is probably from clicking on the hide button -> don't do anything
            return;
        }
        // set popup visibility
        if (!dialog.isVisible()) {
            showPopup();
        } else {
            // hide - taken care of by focusLost
            dialog.setVisible(false);
        }
    }

    JCheckBox[] getCheckboxes() {
        return checkOptions;
    }

    JRadioButton[] getFilterChoices() {
        return radioOptions;
    }

    void reset() {
        dialog.setVisible(false);
        for (JCheckBox checkbox : checkOptions) {
            checkbox.setSelected(false);
        }
        radioOptions[0].setSelected(true);
    }

    private boolean containsCard(MagicCardDefinition cardDef) {

        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i=0; i < getCheckboxes().length; i++) {
            if (getCheckboxes()[i].isSelected()) {
                somethingSelected = true;
                if (isCardValid(cardDef, i)) {
                    resultOR = true;
                } else {
                    resultAND = false;
                }
            }
        }
        if (getFilterChoices()[2].isSelected()) {
            // exclude selected
            return !resultOR;
        }
        // otherwise return OR or AND result
        return !somethingSelected
            || ((getFilterChoices()[0].isSelected() && resultOR)
            || (getFilterChoices()[1].isSelected() && resultAND));

    }

    boolean doesNotInclude(MagicCardDefinition cardDefinition) {
        return !containsCard(cardDefinition);
    }

    class FilterButton extends JButton {
        public FilterButton(String title, ActionListener aListener) {
            super(title);
            setFont(FontsAndBorders.FONT1);
            setMinimumSize(new Dimension(66, 36));
            addActionListener(aListener);
        }
    }

    class PopupDialog extends JDialog {

        private static final int STARTING_HEIGHT = 300;
        private static final int STARTING_WIDTH = 260;

        public PopupDialog() {
            super(ScreenController.getMainFrame());
            setUndecorated(true);
            setSize(STARTING_WIDTH, STARTING_HEIGHT);
        }
    }

    class DialogContentPanel extends TexturedPanel {
        public DialogContentPanel() {
            setBorder(FontsAndBorders.UP_BORDER);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
    }

    private class ResponsePreventer extends TimerTask {
        @Override
        public void run() {
            popupJustToggled = false;
        }
    }

    protected void setPopupContent(
        final String migLayout,
        final Object[] checkboxValues,
        final JCheckBox[] newCheckboxes,
        final JRadioButton[] newFilterButtons,
        final boolean hideAND,
        final ActionListener aListener) {

        this.checkOptions = newCheckboxes;
        this.radioOptions = newFilterButtons;

        final JPanel dialogPanel = new DialogContentPanel();

        final JPanel checkboxesPanel = new JPanel(new MigLayout(migLayout));
        checkboxesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkboxesPanel.setOpaque(false);

        for (int i=0;i<checkboxValues.length;i++) {
            newCheckboxes[i]=new JCheckBox(checkboxValues[i].toString().replace('_', ' '));
            newCheckboxes[i].addActionListener(aListener);
            newCheckboxes[i].setOpaque(false);
            newCheckboxes[i].setForeground(TEXT_COLOR);
            newCheckboxes[i].setFocusPainted(true);
            newCheckboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            checkboxesPanel.add(newCheckboxes[i]);
        }

        final JScrollPane scrollPane = new JScrollPane(checkboxesPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(FontsAndBorders.DOWN_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(POPUP_CHECKBOXES_SIZE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        dialogPanel.add(scrollPane);

        final ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            newFilterButtons[i] = new JRadioButton(FILTER_CHOICES[i]);
            newFilterButtons[i].addActionListener(aListener);
            newFilterButtons[i].setOpaque(false);
            newFilterButtons[i].setForeground(TEXT_COLOR);
            newFilterButtons[i].setFocusPainted(true);
            newFilterButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                newFilterButtons[0].setSelected(true);
            } else if (i == 1) {
                newFilterButtons[1].setVisible(!hideAND);
            }
            bg.add(newFilterButtons[i]);
            dialogPanel.add(newFilterButtons[i]);
        }

        this.dialog.add(dialogPanel);
    }

    protected void setPopupContent(
        Object[] filterValues,
        JCheckBox[] jCheckBox,
        JRadioButton[] jRadioButton,
        boolean b,
        ActionListener aListener)
    {
        setPopupContent("flowy, insets 2", filterValues, jCheckBox, jRadioButton, b, aListener);
    }

}
