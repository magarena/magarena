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

import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.ui.widget.SliderPanel;

public class PreferencesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final String SKINS[]={"wood","granite","opal","user"};
	
	private final MagicFrame frame;
	private final JComboBox skinComboBox;
	private final JCheckBox skipSingleCheckBox;
	private final JCheckBox alwaysPassCheckBox;
	private final JCheckBox smartTargetCheckBox;
	private final SliderPanel undoLevelsSlider;
	private final JButton okButton;
	private final JButton cancelButton;
	
	public PreferencesDialog(final MagicFrame frame) {

		super(frame,true);
		this.setTitle("Preferences");
		this.setSize(400,290);
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

		final JLabel skinLabel=new JLabel("Skin");
		skinLabel.setBounds(25,20,50,25);
		mainPanel.add(skinLabel);
		final ComboBoxModel skinModel=new DefaultComboBoxModel(SKINS);
		skinComboBox=new JComboBox(skinModel);
		skinComboBox.setFocusable(false);
		skinComboBox.setBounds(75,20,275,25);
		skinComboBox.setSelectedIndex(config.getSkin());
		mainPanel.add(skinComboBox);		
		
		skipSingleCheckBox=new JCheckBox("Skip single option choices when appropriate",config.getSkipSingle());
		skipSingleCheckBox.setBounds(25,65,350,20);
		skipSingleCheckBox.setFocusable(false);
		mainPanel.add(skipSingleCheckBox);

		alwaysPassCheckBox=new JCheckBox("Always pass during draw and begin of combat step",config.getAlwaysPass());
		alwaysPassCheckBox.setBounds(25,95,350,20);
		alwaysPassCheckBox.setFocusable(false);
		mainPanel.add(alwaysPassCheckBox);
		
		smartTargetCheckBox=new JCheckBox("Filter legal targets when appropriate",config.getSmartTarget());
		smartTargetCheckBox.setBounds(25,125,350,20);
		smartTargetCheckBox.setFocusable(false);
		mainPanel.add(smartTargetCheckBox);
				
		undoLevelsSlider=new SliderPanel("Undo",IconImages.UNDO,1,7,1,config.getUndoLevels());
		undoLevelsSlider.setBounds(70,155,250,50);
		mainPanel.add(undoLevelsSlider);
		
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
			config.setSkin(skinComboBox.getSelectedIndex());
			config.setUndoLevels(undoLevelsSlider.getValue());
			config.setSkipSingle(skipSingleCheckBox.isSelected());
			config.setAlwaysPass(alwaysPassCheckBox.isSelected());
			config.setSmartTarget(smartTargetCheckBox.isSelected());
			config.save();
			frame.repaint();
			dispose();
		} else if (source==cancelButton) {
			dispose();
		} 
	}	
}