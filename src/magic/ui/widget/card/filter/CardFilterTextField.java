package magic.ui.widget.card.filter;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import magic.translate.MText;
import magic.ui.widget.TextPrompt;
import magic.ui.widget.TextPrompt.Show;
import magic.ui.widget.card.filter.button.FilterPanel;

@SuppressWarnings("serial")
public class CardFilterTextField extends JTextField
        implements DocumentListener {

    // translatable strings
    private static final String _S1 = "Search text...";

    private static final int SEARCH_TIMER_DELAY = 500; //msecs

    private Timer searchTextTimer;
    private final Font defaultFont;
    private final Font searchingFont;
    private final FilterPanel fbp;

    private final List<String> searchTerms = new ArrayList<>();

    public CardFilterTextField(final FilterPanel fbp) {

        this.fbp = fbp;
        defaultFont = getFont();
        searchingFont = defaultFont.deriveFont(Font.BOLD);

        setLookAndFeel();

        getDocument().addDocumentListener(this);
    }

    private void setLookAndFeel() {
        final TextPrompt promptField = new TextPrompt(MText.get(_S1), this);
        promptField.setShow(Show.FOCUS_LOST);
        promptField.changeStyle(Font.ITALIC);
        promptField.setForeground(Color.GRAY);
        promptField.setFocusable(false);
    }

    @Override
    public void changedUpdate(DocumentEvent e) { }

    @Override
    public void insertUpdate(DocumentEvent e) {
        startSearchTextTimer();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        startSearchTextTimer();
    }

    private void createSearchTextTimer() {
        if (searchTextTimer == null) {
            searchTextTimer = new Timer(SEARCH_TIMER_DELAY, (e) -> {
                searchTextTimer.stop();
                fbp.filterChanged();
                setFont(defaultFont);
            });
        }
    }

    private void startSearchTextTimer() {
        setListOfSearchTerms();
        createSearchTextTimer();
        searchTextTimer.setInitialDelay(SEARCH_TIMER_DELAY);
        searchTextTimer.restart();
        setFont(searchingFont);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        if (t.isEmpty()) {
            searchTerms.clear();
        }
    }

    private void setListOfSearchTerms() {
        searchTerms.clear();
        if (!getText().isEmpty()) {
            // extract words or phrases (delimited by double-quotes).
            Pattern regex = Pattern.compile("\"([^\"]+)\"|(\\S+)");
            Matcher matcher = regex.matcher(getText());
            while (matcher.find()) {
                // remove delimiting quotes from any phrase.
                final String matched = matcher.group().replaceAll("^\"|\"$", "");
                searchTerms.add(matched);
//                System.out.println("matched = \"" + matched + "\"");
            }
        }
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

}
