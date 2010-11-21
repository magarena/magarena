package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderPanel extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final JSlider slider;
	private final JLabel valueLabel;
	
	public SliderPanel(final String title,final ImageIcon icon,final int min,final int max,final int spacing,final int value) {
		
		setLayout(new BorderLayout(5,0));
		final JLabel titleLabel=new JLabel(title);
		titleLabel.setPreferredSize(new Dimension(80,0));
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
		slider.setSnapToTicks(true);
		slider.setMajorTickSpacing(spacing);
		add(slider,BorderLayout.CENTER);
	}

	public int getValue() {
		
		return slider.getValue();
	}
	
	public void addChangeListener(final ChangeListener listener) {
		
		slider.addChangeListener(listener);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {

		valueLabel.setText(""+slider.getValue());
	}
}