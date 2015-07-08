package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SliderPanel extends JPanel implements ChangeListener {

    private final JSlider slider;
    private final JLabel titleLabel;
    private final JLabel valueLabel;

    public SliderPanel(final String title,final ImageIcon icon,final int min,final int max,final int spacing,final int value,final boolean snapToTick) {
        
        titleLabel=new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(title.isEmpty() ? 0 : 90, 0));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));

        valueLabel=new JLabel();
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setPreferredSize(new Dimension(30,0));

        slider=new JSlider();
        slider.setOpaque(false);
        slider.addChangeListener(this);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setValue(value);
        slider.setPaintTicks(false);
        slider.setSnapToTicks(snapToTick);
        slider.setMajorTickSpacing(spacing);

        setLayout(new MigLayout("insets 0"));
        add(titleLabel);
        add(slider, "w 100%");
        add(valueLabel);

    }

    public SliderPanel(final String title,final ImageIcon icon,final int min,final int max,final int spacing,final int value) {
        this(title, icon, min, max, spacing, value, true);
    }

    public void setTextColor(final Color color) {
        titleLabel.setForeground(color);
        valueLabel.setForeground(color);
    }

    public int getValue() {
        return slider.getValue();
    }

    public void addChangeListener(final ChangeListener listener) {
        slider.addChangeListener(listener);
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        valueLabel.setText(Integer.toString(slider.getValue()));
    }

    public void setPaintTicks(final boolean b) {
        slider.setPaintTicks(b);
    } 
}
