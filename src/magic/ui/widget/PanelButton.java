package magic.ui.widget;

import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PanelButton extends TexturedPanel {

	private static final long serialVersionUID = 1L;

	private boolean valid=false;
	
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
	
	public void setValid(final boolean valid) {
		this.valid=valid;
		repaint();
	}
	
	public void setComponent(final JComponent component) {
		add(component,BorderLayout.CENTER);
	}
	
	public void mouseClicked() {}
	
	public void mouseEntered() {}
	
	public void mouseExited() {}
	
	public abstract Color getValidColor();

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		final Dimension size=getSize();
		if (valid) {
			final Graphics2D g2d=(Graphics2D)g;
			g2d.setPaint(getValidColor());
			g2d.fillRect(0,0,10+size.width,10+size.height);
		}
	}
}
