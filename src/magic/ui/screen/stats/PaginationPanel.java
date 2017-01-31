package magic.ui.screen.stats;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.helpers.MouseHelper;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PaginationPanel extends JPanel {

    private final JButton firstButton;
    private final JButton lastButton;
    private final JButton nextButton;
    private final JButton prevButton;
    private final JLabel pageLabel;

    private final IPagination paginator;

    public PaginationPanel(IPagination paginator) {

        this.paginator = paginator;

        setOpaque(false);
        setLayout(new MigLayout("insets 0, alignx center"));

        firstButton = new JButton("|<");
        firstButton.setEnabled(false);
        firstButton.addActionListener(new AbstractAction("|<") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor();
                firstButton.setEnabled(false);
                paginator.displayFirstPage();
                setButtonsState();
                setPageNum();
                MouseHelper.showDefaultCursor();
            }
        });
        add(firstButton);

        prevButton = new JButton("<");
        prevButton.setEnabled(false);
        prevButton.addActionListener(new AbstractAction("<") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor();
                prevButton.setEnabled(false);
                paginator.displayPreviousPage();
                setButtonsState();
                setPageNum();
                MouseHelper.showDefaultCursor();
            }
        });
        add(prevButton);

        pageLabel = new JLabel();
        setPageNum();
        add(pageLabel);

        nextButton = new JButton(">");
        nextButton.setEnabled(false);
        nextButton.addActionListener(new AbstractAction(">") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor();
                nextButton.setEnabled(false);
                paginator.displayNextPage();
                setButtonsState();
                setPageNum();
                MouseHelper.showDefaultCursor();
            }
        });
        add(nextButton);

        lastButton = new JButton(">|");
        lastButton.setEnabled(false);
        lastButton.addActionListener(new AbstractAction(">|") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor();
                lastButton.setEnabled(false);
                paginator.displayLastPage();
                setButtonsState();
                setPageNum();
                MouseHelper.showDefaultCursor();
            }
        });
        add(lastButton);

        setButtonsState();
    }

    private void setPageNum() {
        pageLabel.setText(paginator.getPageNum() + " of " + paginator.getTotalPages());
    }

    private void setButtonsState() {
        prevButton.setEnabled(paginator.hasPrevPage());
        nextButton.setEnabled(paginator.hasNextPage());
        firstButton.setEnabled(paginator.hasPrevPage());
        lastButton.setEnabled(paginator.hasNextPage());
    }

    public void refresh() {
        setPageNum();
        setButtonsState();
    }
}
