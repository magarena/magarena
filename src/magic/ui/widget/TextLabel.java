package magic.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import magic.data.TextImages;

public class TextLabel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final TComponent SPACE_LABEL=new EmptyComponent();
	private static final TComponent BREAK_LABEL=new EmptyComponent();

	private static final int SPACE_WIDTH=4;
	private static final int LINE_HEIGHT=16;

	private static final Map<?,?> desktopHintsMap;
	
	private final List<TComponent> components;
	private final int maxWidth;
	private final boolean center;

	static {
		
		final Toolkit tk=Toolkit.getDefaultToolkit();
		desktopHintsMap=(Map<?,?>)(tk.getDesktopProperty("awt.font.desktophints"));
	}
	
	public TextLabel(final String text,final int maxWidth,final boolean center) {
		
		components=new ArrayList<TComponent>();
		this.maxWidth=maxWidth;
		this.center=center;
		setLayout(null);
		setOpaque(false);
		buildComponents(text);
		layoutComponents();
	}
		
	@Override
	public void setForeground(final Color foreground) {
		
		if (components!=null) {
			for (final TComponent component : components) {
	
				component.setForeground(foreground);		
			}
		}
	}
	
	private void addComponent(final TComponent component) {
		
		if (component!=null) {
			components.add(component);
		}
	}
	
	private TComponent buildComponent(final String textPart,final boolean info) {

		if (textPart.isEmpty()) {
			return null;
		}
		final TComponent component;
		if (textPart.charAt(0)=='{') {
			component=new IconComponent(TextImages.getIcon(textPart));
		} else if (info) {
			component=new TextComponent(textPart,this,FontsAndBorders.FONT0,Color.BLUE);
		} else {
			component=new TextComponent(textPart,this,FontsAndBorders.FONT1,null);
		}
		return component;
	}
	
	private void buildComponents(String text) {

		text=text.replaceAll("\\s+"," ").trim()+' ';
		int startIndex=0;
		boolean info=false;
		for (int index=0;index<text.length();index++) {

			final char ch=text.charAt(index);
			if (ch==' ') {
				addComponent(buildComponent(text.substring(startIndex,index),info));
				addComponent(SPACE_LABEL);
				startIndex=index+1;
			} else if (ch=='|') {
				addComponent(buildComponent(text.substring(startIndex,index),info));
				addComponent(BREAK_LABEL);
				startIndex=index+1;				
			} else if (ch=='{') {
				addComponent(buildComponent(text.substring(startIndex,index),info));
				startIndex=index;
			} else if (ch=='}') {
				addComponent(buildComponent(text.substring(startIndex,index+1),info));
				startIndex=index+1;
			} else if (ch=='(') {
				addComponent(buildComponent(text.substring(startIndex,index),info));
				startIndex=index;
				info=true;
			} else if (ch==')') {
				addComponent(buildComponent(text.substring(startIndex,index+1),info));
				startIndex=index+1;
				info=false;	
			} 
		}
	}
	
	private void layoutComponents() {

		int x=0;
		int y=0;
		for (final TComponent component : components) {
			
			if (component==SPACE_LABEL) {
				x+=SPACE_WIDTH;
			} else if (component==BREAK_LABEL) {
				x=0;
				y+=LINE_HEIGHT;
			} else {			
				final Dimension csize=component.getPreferredSize();
				if (component.requiresNewLine()&&x+csize.width>=maxWidth) {	
					x=0;
					y+=LINE_HEIGHT;
				}
				component.setLocation(x,y+(LINE_HEIGHT-csize.height));
				x+=csize.width;
			}
		}
		
		final Insets insets=getInsets();
		setPreferredSize(new Dimension(insets.left+insets.right,y+LINE_HEIGHT+insets.top+insets.bottom));
	}
	
	@Override
	public void paint(final Graphics g) {

		super.paint(g);
		
		if (desktopHintsMap!=null) {
			((Graphics2D)g).addRenderingHints(desktopHintsMap);
		}
		
		final Dimension size=getPreferredSize();
		final int cx=center?(getWidth()-maxWidth)/2:0;
		final int cy=center?(getHeight()-size.height)/2:0;
		
		for (final TComponent component : components) {
			
			component.paint(this,g,cx,cy);			
		}
	}
	
	private static abstract class TComponent {

		protected int lx=0;
		protected int ly=0;
		protected Color foreground=Color.BLACK;
		
		public void setLocation(final int x,final int y) {
			
			this.lx=x;
			this.ly=y;
		}
		
		public void setForeground(final Color foreground) {
			
			this.foreground=foreground;
		}
		
		public abstract boolean requiresNewLine();

		public abstract Dimension getPreferredSize();
		
		public abstract void paint(final JComponent com,final Graphics g,final int x,final int y);
	}
	
	private static class EmptyComponent extends TComponent {

		@Override
		public boolean requiresNewLine() {

			return false;
		}

		@Override
		public Dimension getPreferredSize() {

			return new Dimension(0,0);
		}

		@Override
		public void paint(final JComponent com,final Graphics g,final int x,final int y) {
			
		}		
	}
	
	private static class TextComponent extends TComponent {

		private final String text;
		private final Font font;
		private final FontMetrics metrics;
		private final Color primaryForeground;
		private final boolean newLine;
		
		public TextComponent(final String text,final JComponent component,final Font font,final Color foreground) {

			this.text=text;
			this.font=font;
			this.metrics=component.getFontMetrics(font);
			this.primaryForeground=foreground;
			this.newLine=!(".".equals(text)||",".equals(text));
		}

		@Override
		public boolean requiresNewLine() {

			return newLine;
		}
		
		@Override
		public Dimension getPreferredSize() {
			
			return new Dimension(metrics.stringWidth(text)+1,metrics.getHeight());			
		}

		@Override
		public void paint(final JComponent com,final Graphics g,final int x,final int y) {

			g.setColor(primaryForeground!=null?primaryForeground:foreground);
			g.setFont(font);
			g.drawString(text,lx+x,ly+y+metrics.getAscent());
		}
	}
	
	private static class IconComponent extends TComponent {
		
		private final ImageIcon icon;

		public IconComponent(final ImageIcon icon) {
			
			this.icon=icon;
		}
		
		@Override
		public boolean requiresNewLine() {

			return true;
		}
		
		@Override
		public Dimension getPreferredSize() {

			return new Dimension(icon.getIconWidth()+1,icon.getIconHeight());
		}

		@Override
		public void paint(final JComponent com,final Graphics g,final int x,final int y) {

			icon.paintIcon(com,g,lx+x,ly+y);
		}
	}
}