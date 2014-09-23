package magic.ui.widget;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class SliderPanel extends JPanel implements ChangeListener {

    private static final long serialVersionUID = 1L;

    private final JSlider slider;
    private final JLabel titleLabel;
    private final JLabel valueLabel;

    public SliderPanel(final String title,final ImageIcon icon,final int min,final int max,final int spacing,final int value,final boolean snapToTick) {

        setLayout(new BorderLayout(5,0));
        
        titleLabel=new JLabel(title);
        titleLabel.setPreferredSize(new Dimension(title.isEmpty() ? 0 : 90, 0));
        titleLabel.setIcon(icon);
        add(titleLabel,BorderLayout.WEST);

        valueLabel=new JLabel();
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setPreferredSize(new Dimension(30,0));
        add(valueLabel,BorderLayout.EAST);

        slider=new JSlider();
        slider.setOpaque(false);
        slider.addChangeListener(this);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setValue(value);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(snapToTick);
        slider.setMajorTickSpacing(spacing);
        add(slider,BorderLayout.CENTER);

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
