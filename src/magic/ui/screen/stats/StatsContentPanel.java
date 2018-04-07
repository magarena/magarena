package magic.ui.screen.stats;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import magic.data.stats.h2.H2Database;
import magic.ui.FontsAndBorders;
import magic.ui.mwidgets.MFileLink;
import magic.ui.mwidgets.MScrollPane;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class StatsContentPanel extends TexturedPanel implements IPagination {

    private GameStatsTableModel tm;
    private GameStatsJTable table;
    private final MScrollPane scrollpane = new MScrollPane();
    private TestGameRunner runner;
    private final ActionPanel actionPanel;
    private PaginationPanel paginator;
    private final StatsScreen screen;

    StatsContentPanel(StatsScreen screen) {
        this.screen = screen;
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        setLayout(new MigLayout("flowy", "[fill, grow]"));
        actionPanel = new ActionPanel();
        add(actionPanel);
        refreshStatsTable();
        if (tm != null) {
            table = new GameStatsJTable(tm);
            scrollpane.setViewportView(table);
            scrollpane.setOpaque(false);
            scrollpane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            add(scrollpane.component(), "w 100%, h 100%");
            paginator = new PaginationPanel(this);
            add(paginator);
        }
    }

    void notifyTestGameRunnerProcess(Integer games) {
        System.out.println("Games complete : " + games);
        actionPanel.runButton.setText("Games complete : " + games);
    }

    void setTableModel() {
        if (table != null) {
            table.setModel(tm);
        }
    }

    private int getPrevPageNum() {
        return tm.getPrevPageNum();
    }

    private int getNextPageNum() {
        return tm.getNextPageNum();
    }

    @Override
    public int getTotalPages() {
        return tm.getTotalPages();
    }

    @Override
    public int getPageNum() {
        return tm.getPageNum();
    }

    @Override
    public boolean hasPrevPage() {
        return tm.hasPrevPage();
    }

    @Override
    public boolean hasNextPage() {
        return tm.hasNextPage();
    }

    private final JSpinner gamesSpinner = new JSpinner();

    @Override
    public void displayNextPage() {
        refreshStatsTable(getNextPageNum());
        setTableModel();
    }

    @Override
    public void displayLastPage() {
        refreshStatsTable(getTotalPages());
        setTableModel();
    }

    @Override
    public void displayPreviousPage() {
        refreshStatsTable(getPrevPageNum());
        setTableModel();
    }

    @Override
    public void displayFirstPage() {
        refreshStatsTable(1);
        setTableModel();
    }

    private class ActionPanel extends JPanel {

        private final JButton runButton;

        public ActionPanel() {

            setOpaque(false);
            setLayout(new MigLayout("insets 0"));

            runButton = new JButton("Run test game...");
            runButton.addActionListener(new AbstractAction("Run test game...") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    runButton.setEnabled(false);
                    runButton.setText("Running test games: " + gamesSpinner.getValue());
                    doRunTestGame((int)gamesSpinner.getValue());
                }
            });

            gamesSpinner.setValue(10);

            MFileLink dbLink = new MFileLink();
            dbLink.setFile(new File(H2Database.getDatabaseFile() + ".mv.db"));

            add(runButton, "w 200!");
            add(gamesSpinner, "w 60!");
            add(dbLink.component(), "w 100%");
        }

        private void setRunButtonEnabled(boolean b) {
            runButton.setEnabled(b);
            runButton.setText("Run test game...");
        }
    }

    void refreshStatsTable(int page) {
        tm = new GameStatsTableModel(page);
    }

    private void refreshStatsTable() {
        tm = new GameStatsTableModel();
        screen.setTotalGames(tm.getTotalGames());
    }

    private void doRunTestGame(int totalGames) {
        if (runner != null && !runner.isDone()) {
            runner.cancel(true);
        }
        runner = new TestGameRunner(this, totalGames);
        runner.execute();
    }

    void onTestGameRunnerFinished() {
        System.out.println("=== done ===");
        actionPanel.setRunButtonEnabled(true);
        refreshStatsTable();
        if (table != null) {
            table.setModel(tm);
        }
        paginator.refresh();
    }

    boolean isReadyToClose() {
        return true;
    }

}
