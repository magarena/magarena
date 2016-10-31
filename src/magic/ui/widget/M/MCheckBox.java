package magic.ui.widget.M;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;

@SuppressWarnings("serial")
public class MCheckBox extends MWidget {

    private static final ImageIcon OFF_ICON =
            MagicImages.getIcon(MagicIcon.CHECKBOX_OFF);

    private static final ImageIcon ON_ICON =
            MagicImages.getIcon(MagicIcon.CHECKBOX_ON);

    private static final Icon DISABLED_OFF_ICON =
            ImageHelper.getRecoloredIcon(OFF_ICON, Color.BLACK, Color.GRAY);

    private static final Icon DISABLED_ON_ICON =
            ImageHelper.getRecoloredIcon(ON_ICON, Color.BLACK, Color.GRAY);

    private final JCheckBox cb = new JCheckBox();

    public MCheckBox(String text, boolean selected) {
        cb.setText(text);
        cb.setSelected(selected);
        setIcons();
    }

    public MCheckBox(String text) {
        this(text, false);
    }

    public MCheckBox() {
        setIcons();
    }

    @Override
    public JComponent component() {
        return cb;
    }

    private void setIcons() {
        cb.setIcon(OFF_ICON);
        cb.setSelectedIcon(ON_ICON);
        cb.setDisabledIcon(DISABLED_OFF_ICON);
        cb.setDisabledSelectedIcon(DISABLED_ON_ICON);
        cb.setPressedIcon(cb.getIcon());
        cb.setRolloverIcon(OFF_ICON);
        cb.setIconTextGap(6);
    }

    
    //
    // JCheckBox delegates
    //
    public void addItemListener(ItemListener l) {
        cb.addItemListener(l);
    }

    public boolean isSelected() {
        return cb.isSelected();
    }

    public void setSelected(boolean b) {
        cb.setSelected(b);
    }

    public void setVerticalTextPosition(int i) {
        cb.setVerticalTextPosition(i);
    }

    public void addChangeListener(ChangeListener l) {
        cb.addChangeListener(l);
    }

    public void addActionListener(ActionListener l) {
        cb.addActionListener(l);
    }

    public String getText() {
        return cb.getText();
    }

    public void setActionCommand(String s) {
        cb.setActionCommand(s);
    }

    public void doClick() {
        cb.doClick();
    }
}
