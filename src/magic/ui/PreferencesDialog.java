package magic.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.AvatarImages;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.SliderPanel;

public class PreferencesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final MagicFrame frame;
	private final JComboBox themeComboBox;
	private final JComboBox avatarComboBox;
	private final JCheckBox soundCheckBox;
	private final JCheckBox highQualityCheckBox;
	private final JCheckBox skipSingleCheckBox;
	private final JCheckBox alwaysPassCheckBox;
	private final JCheckBox smartTargetCheckBox;
	private final SliderPanel popupDelaySlider;
	private final JButton okButton;
	private final JButton cancelButton;
	
	public PreferencesDialog(final MagicFrame frame) {

		super(frame,true);
		this.setTitle("Preferences");
		this.setSize(400,400);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.frame=frame;
		
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
		
		final GeneralConfig config=GeneralConfig.getInstance();
       
        int Y=20;
        final int X=28, W=70, H=25;
        final int X2=100, W2=255;
        final int X3=25, H3=20, W3=350;
		
		final JLabel themeLabel=new JLabel("Theme");
		themeLabel.setBounds(X,Y,W,H);
		themeLabel.setIcon(IconImages.PICTURE);
		mainPanel.add(themeLabel);
		final ComboBoxModel themeModel=new DefaultComboBoxModel(
                ThemeFactory.getInstance().getThemeNames());
		themeComboBox=new JComboBox(themeModel);
		themeComboBox.setFocusable(false);
		themeComboBox.setBounds(X2,Y,W2,H);
		themeComboBox.setSelectedItem(config.getTheme());
		mainPanel.add(themeComboBox);
	

        Y += 35;
		final JLabel avatarLabel=new JLabel("Avatar");
		avatarLabel.setBounds(X,Y,W,H);
		avatarLabel.setIcon(IconImages.AVATAR);
		mainPanel.add(avatarLabel);
		final ComboBoxModel avatarModel=new DefaultComboBoxModel(AvatarImages.getInstance().getNames());
		avatarComboBox=new JComboBox(avatarModel);
		avatarComboBox.setFocusable(false);
		avatarComboBox.setBounds(X2,Y,W2,H);
		avatarComboBox.setSelectedItem(config.getAvatar());
		mainPanel.add(avatarComboBox);

        Y += 35;
		soundCheckBox=new JCheckBox("Enable sound effects",config.isSound());
		soundCheckBox.setBounds(X3,Y,W3,H3);
		soundCheckBox.setFocusable(false);
		mainPanel.add(soundCheckBox);
	
        Y += 30;
		highQualityCheckBox=new JCheckBox("Show card images in original size",config.isHighQuality());
		highQualityCheckBox.setBounds(X3,Y,W3,H3);
		highQualityCheckBox.setFocusable(false);
		mainPanel.add(highQualityCheckBox);
		
        Y += 30;
		skipSingleCheckBox=new JCheckBox("Skip single option choices when appropriate",config.getSkipSingle());
		skipSingleCheckBox.setBounds(X3,Y,W3,H3);
		skipSingleCheckBox.setFocusable(false);
		mainPanel.add(skipSingleCheckBox);

        Y += 30;
		alwaysPassCheckBox=new JCheckBox("Always pass during draw and begin of combat step",config.getAlwaysPass());
		alwaysPassCheckBox.setBounds(X3,Y,W3,H3);
		alwaysPassCheckBox.setFocusable(false);
		mainPanel.add(alwaysPassCheckBox);
	
        Y += 30;
		smartTargetCheckBox=new JCheckBox("Remove unusual choices",config.getSmartTarget());
		smartTargetCheckBox.setBounds(X3,Y,W3,H3);
		smartTargetCheckBox.setFocusable(false);
		mainPanel.add(smartTargetCheckBox);
				
        Y += 30;
		popupDelaySlider=new SliderPanel("Popup",IconImages.DELAY,0,500,50,config.getPopupDelay());
		popupDelaySlider.setBounds(60,Y,270,50);
		mainPanel.add(popupDelaySlider);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel,BorderLayout.CENTER);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);

		setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {

		final Object source=event.getSource();
		if (source==okButton) {
			final GeneralConfig config=GeneralConfig.getInstance();
			config.setTheme((String)themeComboBox.getSelectedItem());
			config.setAvatar((String)avatarComboBox.getSelectedItem());
			config.setSound(soundCheckBox.isSelected());
			config.setHighQuality(highQualityCheckBox.isSelected());
			config.setSkipSingle(skipSingleCheckBox.isSelected());
			config.setAlwaysPass(alwaysPassCheckBox.isSelected());
			config.setSmartTarget(smartTargetCheckBox.isSelected());
			config.setPopupDelay(popupDelaySlider.getValue());
			config.save();
			ThemeFactory.getInstance().setCurrentTheme(config.getTheme());
			frame.repaint();
			dispose();
		} else if (source==cancelButton) {
			dispose();
		} 
	}	
}
