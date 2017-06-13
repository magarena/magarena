package magic.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import magic.data.GeneralConfig;
import magic.model.IUIGameController;
import magic.translate.MText;
import magic.ui.dialog.button.CancelButton;
import magic.ui.dialog.button.SaveButton;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSidebarLayoutDialog extends MagicDialog {

    // translatable strings
    private static final String _S1 = "Sidebar Layout";
    private static final String _S4 = "Move Up";
    private static final String _S5 = "Move Down";

    private static final Color HIGHLIGHT_BACK = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
    private static final Color HIGHLIGHT_FORE = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_FOREGROUND);

    private boolean isCancelled = false;
    private final JList<String> jlist = new JList<>();
    private DefaultListModel<String> listModel;
    private final IUIGameController controller;
    private final String currentLayout;

    // CTR
    public DuelSidebarLayoutDialog(final IUIGameController controller) {
        super(MText.get(_S1), new Dimension(300, 260));
        this.controller = controller;
        currentLayout = GeneralConfig.getInstance().getDuelSidebarLayout();
        setLookAndFeel();
        refreshLayout();
        refreshContent();
        setVisible(true);
    }

    private void setLookAndFeel() {
        jlist.setOpaque(true);
        jlist.setBackground(Color.WHITE);
        jlist.setForeground(Color.BLACK);
        jlist.setSelectionBackground(HIGHLIGHT_BACK);
        jlist.setSelectionForeground(HIGHLIGHT_FORE);
        jlist.setFocusable(true);
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private String getLayoutString() {
        final StringBuffer sb = new StringBuffer();
        final Enumeration<String> en = listModel.elements();
        while(en.hasMoreElements()) {
            sb.append(en.nextElement()).append(",");
        }
        return sb.toString();
    }

    private void refreshLayout() {
        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new MigLayout("flowy, gap 0"));
        panel.add(getListButtonPanel(), "w 100%, h 30!");
        panel.add(jlist, "w 100%, h 100%, aligny top, gapbottom 6");
        panel.add(getDialogButtonPanel(), "w 100%, h 30!, pushy, aligny bottom");
    }

    private JPanel getListButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, gap 0"));
        buttonPanel.add(getMoveDownButton(), "w 50%");
        buttonPanel.add(getMoveUpButton(), "w 50%");
        return buttonPanel;
    }

    private JPanel getDialogButtonPanel() {
        final JPanel buttonPanel = new JPanel(new MigLayout("insets 0, alignx right"));
        buttonPanel.add(getCancelButton());
        buttonPanel.add(getSaveButton());
        return buttonPanel;
    }

    private JButton getSaveButton() {
        final JButton btn = new SaveButton();
        btn.addActionListener(getSaveAction());
        return btn;
    }

    private JButton getCancelButton() {
        final JButton btn = new CancelButton();
        btn.addActionListener(getCancelAction());
        return btn;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    final public void refreshContent() {
        listModel = new DefaultListModel<>();
        for (String item : currentLayout.split(",")) {
            if (!item.isEmpty()) {
                listModel.addElement(item);
            }
        }
        jlist.setModel(listModel);
        jlist.setSelectedIndex(0);
    }

    private JButton getMoveUpButton() {
        final JButton btn = new JButton(MText.get(_S4));
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jlist.getSelectedIndex() > 0) {
                    int moveMe = jlist.getSelectedIndex();
                    swap(moveMe, moveMe - 1);
                    jlist.setSelectedIndex(moveMe - 1);
                    jlist.ensureIndexIsVisible(moveMe - 1);
                    refreshSidebarLayout(getLayoutString());
                }
            }
        });
        return btn;
    }

    private JButton getMoveDownButton() {
        final JButton btn = new JButton(MText.get(_S5));
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int moveMe = jlist.getSelectedIndex();
                if (moveMe != listModel.getSize() - 1) {
                    swap(moveMe, moveMe + 1);
                }
                jlist.setSelectedIndex(moveMe + 1);
                jlist.ensureIndexIsVisible(moveMe + 1);
                refreshSidebarLayout(getLayoutString());
            }
        });
        return btn;
    }

    private void refreshSidebarLayout(final String newLayout) {
        GeneralConfig.getInstance().setDuelSidebarLayout(newLayout);
        GeneralConfig.getInstance().save();
        controller.refreshSidebarLayout();
    }


    //Swap two elements in the list.
    private void swap(int a, int b) {
        String aObject = listModel.getElementAt(a);
        String bObject = listModel.getElementAt(b);
        listModel.set(a, bObject);
        listModel.set(b, aObject);
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                isCancelled = true;
                refreshSidebarLayout(currentLayout);
                dispose();
            }
        };
    }

    private AbstractAction getSaveAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
    }

}
