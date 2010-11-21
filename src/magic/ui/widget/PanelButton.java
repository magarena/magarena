package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

public class PanelButton extends TexturedPanel {

	private static final long serialVersionUID = 1L;

	public PanelButton() {
		
		setLayout(new BorderLayout());
		setBorder(FontsAndBorders.UP_BORDER);
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(final MouseEvent event) {
				
				setBorder(FontsAndBorders.DOWN_BORDER);
			}

			@Override
			public void mouseReleased(final MouseEvent event) {

				setBorder(FontsAndBorders.UP_BORDER);
				if (PanelButton.this.contains(event.getX(),event.getY())) {
					PanelButton.this.mouseClicked();
				}
			}

			@Override
			public void mouseEntered(final MouseEvent event) {
				
				PanelButton.this.mouseEntered();
			}

			@Override
			public void mouseExited(final MouseEvent event) {

				PanelButton.this.mouseExited();
			}
		});
	}
	
	public void setComponent(final JComponent component) {
		
		add(component,BorderLayout.CENTER);
	}
	
	public void mouseClicked() {
		
	}
	
	public void mouseEntered() {
		
	}
	
	public void mouseExited() {
		
	}
}