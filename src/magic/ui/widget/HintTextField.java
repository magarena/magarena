package magic.ui.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    private boolean showingHint;
    private final Font hintFont;
    private final Font normalFont;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showingHint = true;
        this.hintFont = new Font(getFont().getFontName(), Font.ITALIC, getFont().getSize());
        this.normalFont = new Font(getFont().getFontName(), Font.PLAIN, getFont().getSize());
        super.addFocusListener(this);
        super.setForeground(Color.GRAY);
        super.setFont(hintFont);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
            super.setFont(normalFont);
            super.setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            setText(hint);
            showingHint = true;
            super.setFont(hintFont);
            super.setForeground(Color.GRAY);
        }
    }

    @Override
    public String getText() {
//        return showingHint ? "" : super.getText();
        String typed = super.getText();
        return typed.equals(hint) ? "" : typed;
    }

    @Override
    public void setText(String t) {
        if (t.trim().isEmpty()) {
            showingHint = true;
            t = hint;
            super.setFont(hintFont);
            super.setForeground(Color.GRAY);
        }
        super.setText(t);
    }

    public boolean isHintVisible() {
        return showingHint;
    }

}
