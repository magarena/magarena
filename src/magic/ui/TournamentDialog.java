package magic.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import magic.data.CubeDefinitions;
import magic.data.IconImages;
import magic.data.TournamentConfig;
import magic.model.MagicColor;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.SliderPanel;

public class TournamentDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final MagicFrame frame;
	private final AvatarPanel avatarPanel;
	private final JTextField nameTextField;
	private final SliderPanel lifeSlider;
	private final SliderPanel handSlider;
	private final SliderPanel gameSlider;
	private final ColorsChooser playerColorsChooser;
	private final ColorsChooser opponentColorsChooser;
	private final JComboBox cubeComboBox;
	private final JButton okButton;
	private final JButton cancelButton;
	private final Theme theme;
	
	public TournamentDialog(final MagicFrame frame) {
		
		super(frame,true);
		this.frame=frame;
		this.setTitle("New duel");
		this.setSize(500,450);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		theme=ThemeFactory.getInstance().getCurrentTheme();
		
		final TournamentConfig config=TournamentConfig.getInstance();
		config.load();		
		
		final JPanel buttonPanel=new JPanel();
		buttonPanel.setPreferredSize(new Dimension(0,45));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,15,0));
		okButton=new JButton("OK");
		okButton.setFocusable(false);
		okButton.setIcon(IconImages.OK);
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		cancelButton=new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.setIcon(IconImages.CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		final JPanel mainPanel=new JPanel();
		mainPanel.setLayout(null);

		nameTextField=new JTextField(config.getName());
		nameTextField.setPreferredSize(new Dimension(0,25));
		nameTextField.setBounds(35,20,120,25);
		mainPanel.add(nameTextField);
		
		avatarPanel=new AvatarPanel(config.getAvatar());
		avatarPanel.setBounds(35,50,120,180);
		mainPanel.add(avatarPanel);
		
		lifeSlider=new SliderPanel("Life",theme.getIcon(Theme.ICON_LIFE),15,30,5,config.getStartLife());
		lifeSlider.setBounds(190,25,270,50);
		mainPanel.add(lifeSlider);
		
		handSlider=new SliderPanel("Hand",theme.getIcon(Theme.ICON_HAND),6,8,1,config.getHandSize());
		handSlider.setBounds(190,95,270,50);
		mainPanel.add(handSlider);

		gameSlider=new SliderPanel("Games",IconImages.GAME,3,11,2,config.getNrOfGames());
		gameSlider.setBounds(190,165,270,50);
		mainPanel.add(gameSlider);
		
		playerColorsChooser=new ColorsChooser(config.getPlayerColors());
		playerColorsChooser.setBounds(55,255,130,50);
		mainPanel.add(playerColorsChooser);
		
		final JLabel versusLabel=new JLabel("versus");
		versusLabel.setHorizontalAlignment(JLabel.CENTER);
		versusLabel.setFont(FontsAndBorders.FONT4);
		versusLabel.setBounds(185,255,120,50);
		mainPanel.add(versusLabel);
		
		opponentColorsChooser=new ColorsChooser(config.getOpponentColors());
		opponentColorsChooser.setBounds(305,255,130,50);
		mainPanel.add(opponentColorsChooser);

		final JLabel cubeLabel=new JLabel("Cube");
		cubeLabel.setIcon(IconImages.CUBE);
		cubeLabel.setBounds(55,330,80,25);
		mainPanel.add(cubeLabel);
		
		final ComboBoxModel cubeModel=new DefaultComboBoxModel(CubeDefinitions.getInstance().getCubeNames());
		cubeComboBox=new JComboBox(cubeModel);
		cubeComboBox.setFocusable(false);
		cubeComboBox.setBounds(135,330,300,25);
		cubeComboBox.setSelectedItem(config.getCube());
		mainPanel.add(cubeComboBox);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel,BorderLayout.CENTER);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {

		final Object source=event.getSource();
		if (source==okButton) {			
			final TournamentConfig config=TournamentConfig.getInstance();
			final String playerColors=(String)playerColorsChooser.getSelectedItem();
			final String opponentColors=(String)opponentColorsChooser.getSelectedItem();
			config.setAvatar(avatarPanel.getAvatar());
			config.setName(nameTextField.getText());
			config.setStartLife(lifeSlider.getValue());
			config.setHandSize(handSlider.getValue());
			config.setNrOfGames(gameSlider.getValue());
			config.setPlayerColors(playerColors);
			config.setOpponentColors(opponentColors);
			config.setCube((String)cubeComboBox.getSelectedItem());
			config.save();
			frame.newTournament(config);
			dispose();
		} else if (source==cancelButton) {
			dispose();
		} 
	}
	
	private class AvatarPanel extends JPanel implements ActionListener {
	
		private static final long serialVersionUID = 1L;

		private final JLabel avatarLabel;
		private final JButton leftButton;
		private final JButton rightButton;
		private int avatar;
		
		public AvatarPanel(final int avatar) {

			this.avatar=avatar;
			
			setLayout(new BorderLayout(0,5));
						
			avatarLabel=new JLabel();
			avatarLabel.setIcon(theme.getAvatarIcon(avatar,3));
			add(avatarLabel,BorderLayout.CENTER);

			final JPanel buttonPanel=new JPanel();
			buttonPanel.setLayout(new GridLayout(1,2,10,0));
			add(buttonPanel,BorderLayout.SOUTH);
			
			leftButton=new JButton(IconImages.LEFT);
			leftButton.setFocusable(false);
			leftButton.addActionListener(this);
			buttonPanel.add(leftButton,BorderLayout.WEST);
			
			rightButton=new JButton(IconImages.RIGHT);
			rightButton.setFocusable(false);
			rightButton.addActionListener(this);		
			buttonPanel.add(rightButton,BorderLayout.EAST);			
		}
		
		public int getAvatar() {
			
			return avatar;
		}
		
		public void actionPerformed(ActionEvent event) {
		
			final Object source=event.getSource();
			if (source==leftButton) {
				avatar--;
				if (avatar<0) {
					avatar=theme.getNumberOfAvatars()-1;
				}
			} else {
				avatar++;
				if (avatar==theme.getNumberOfAvatars()) {
					avatar=0;
				}
			}
			avatarLabel.setIcon(theme.getAvatarIcon(avatar,3));
		}
	}
		
	private static class ColorsChooser extends JComboBox implements ListCellRenderer {

		private static final long serialVersionUID = 1L;
		
		public ColorsChooser(final String colors) {
			
			this.setRenderer(this);
			
			final DefaultComboBoxModel model=new DefaultComboBoxModel();
			model.addElement("bug");
			model.addElement("bur");
			model.addElement("buw");
			model.addElement("bgr");
			model.addElement("bgw");
			model.addElement("brw");
			model.addElement("ugw");
			model.addElement("ugr");
			model.addElement("urw");
			model.addElement("grw");
			model.addElement("***");
			model.addElement("bu");
			model.addElement("bg");
			model.addElement("br");
			model.addElement("bw");
			model.addElement("ug");
			model.addElement("ur");
			model.addElement("uw");
			model.addElement("gr");
			model.addElement("gw");
			model.addElement("rw");
			model.addElement("**");
			setModel(model);
			setSelectedItem(colors);
			this.setFocusable(false);
		}

		@Override
		public Component getListCellRendererComponent(
				final JList list,final Object value,final int index,final boolean isSelected,final boolean cellHasFocus) {

			final String colors=(String)value;
			final JPanel panel=new JPanel(new GridLayout(1,3));
			for (int i=0;i<colors.length();i++) {
				
				final char ch=colors.charAt(i);
				final ImageIcon icon=ch=='*'?IconImages.ANY:MagicColor.getColor(ch).getIcon();
				panel.add(new JLabel(icon));
			}
			panel.setBorder(FontsAndBorders.EMPTY_BORDER);
			if (isSelected) {
				panel.setBackground(Color.LIGHT_GRAY);
			}
			return panel;
		}
	}
}