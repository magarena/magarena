package magic.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import magic.data.DownloadImageFile;
import magic.data.DownloadImageFiles;
import magic.data.IconImages;

public class DownloadImagesDialog extends JDialog implements Runnable,ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String DOWNLOAD_IMAGES_FILENAME="images.txt";
	
	private final DownloadImageFiles files;
	private final JComboBox proxyComboBox;
	private final JTextField addressTextField;
	private final JTextField portTextField;
	private final JProgressBar progressBar;
	private final JLabel downloadLabel;
	private final JLabel downloadProgressLabel;
	private final JButton okButton;
	private final JButton cancelButton;
	private Thread downloader=null;
	private Proxy proxy=null;
	long startDownload ;
	public DownloadImagesDialog(final MagicFrame frame) {

		super(frame,true);
		this.setLayout(new BorderLayout());
		this.setTitle("Download images");
		this.setSize(300,405);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		final JPanel downloadPanel=new JPanel();
		downloadPanel.setLayout(null);
		add(downloadPanel,BorderLayout.CENTER);
		
		final Proxy.Type[] proxyTypes=Proxy.Type.values();
		final DefaultComboBoxModel proxyModel=new DefaultComboBoxModel(proxyTypes);
		proxyComboBox=new JComboBox(proxyModel);
		proxyComboBox.setBounds(10,25,220,25);
		proxyComboBox.setFocusable(false);
		proxyComboBox.addActionListener(this);
		final JLabel addressLabel=new JLabel("Address");
		addressLabel.setBounds(10,55,220,25);
		addressTextField=new JTextField();
		addressTextField.setBounds(10,80,220,25);
		final JLabel portLabel=new JLabel("Port");
		portLabel.setBounds(10,110,220,25);
		portTextField=new JTextField();
		portTextField.setBounds(10,135,220,25);
		
		final JPanel proxyPanel=new JPanel();
		proxyPanel.setBorder(BorderFactory.createTitledBorder("Proxy"));
		proxyPanel.setBounds(25,20,240,175);
		proxyPanel.setLayout(null);
		proxyPanel.add(proxyComboBox);
		proxyPanel.add(addressLabel);
		proxyPanel.add(addressTextField);
		proxyPanel.add(portLabel);
		proxyPanel.add(portTextField);
		downloadPanel.add(proxyPanel);
		
		progressBar=new JProgressBar();
		progressBar.setBounds(10,30,220,25);
		downloadLabel=new JLabel();
		downloadLabel.setBounds(10,60,220,25);
		downloadProgressLabel = new JLabel();
		downloadProgressLabel.setBounds(10,80,220,25);
		downloadProgressLabel.setText("");
		final JPanel progressPanel=new JPanel();
		progressPanel.setBorder(BorderFactory.createTitledBorder("Progress"));
		progressPanel.setBounds(25,200,240,120);
		progressPanel.setLayout(null);
		progressPanel.add(progressBar);
		progressPanel.add(downloadLabel);
		progressPanel.add(downloadProgressLabel);
		downloadPanel.add(progressPanel);

		okButton=new JButton("OK");
		okButton.setFocusable(false);
		okButton.setIcon(IconImages.OK);
		okButton.addActionListener(this);
		cancelButton=new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.setIcon(IconImages.CANCEL);
		cancelButton.addActionListener(this);

		final JPanel buttonPanel=new JPanel();
		buttonPanel.setPreferredSize(new Dimension(0,45));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT,15,0));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel,BorderLayout.SOUTH);

		files=new DownloadImageFiles(DOWNLOAD_IMAGES_FILENAME);
		if (files.isEmpty()) {
			okButton.setEnabled(false);
			progressBar.setMaximum(1);
			progressBar.setValue(1);
			downloadLabel.setText("All images are present.");
		} else {
			downloadLabel.setText("Press OK to begin or Cancel.");
		}

		updateProxy();
		setVisible(true);		
	}
	
	private void updateProxy() {
		
		final boolean use=proxyComboBox.getSelectedItem()!=Type.DIRECT;
		addressTextField.setEnabled(use);
		portTextField.setEnabled(use);
		if (use) {
			addressTextField.requestFocus();
		}		
	}

	/*
	 * Checks if the thread is still uninterrupted. If it is, sleep  (not sure if return would be better). If not continue and update the labels, then repaint.
	 */
	@Override
	public void run() {	
		if(downloader==null){
			try{
			Thread.sleep(10);
			}catch(InterruptedException ex){
				System.out.println("RunningThread InterruptedException");
				System.out.println("running time in millis: "
						+ (System.currentTimeMillis() - startDownload));
				Thread.currentThread().interrupt();

			}
		}else{
		progressBar.setMinimum(0);
		progressBar.setMaximum(files.size());
		
		int count=0;
		for (final DownloadImageFile file : files) {
			downloadProgressLabel.setText((count+1)+"/"+(files.size()+1));
			downloadLabel.setText(file.getFilename());
			file.download(proxy);
			progressBar.setValue(++count);
		}
		
		dispose();
        IconImages.reloadSymbols();
		}
		//System.exit(0);
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {

		final Object source=event.getSource();
		if (source==okButton) {
			try {				
				final Proxy.Type proxyType=(Proxy.Type)proxyComboBox.getSelectedItem();
				if (proxyType==Type.DIRECT) {
					proxy=Proxy.NO_PROXY;
				} else {
					final String address=addressTextField.getText();
					final int port=Integer.parseInt(portTextField.getText());
					proxy=new Proxy(proxyType,new InetSocketAddress(address,port));
				}
			} catch (final Exception ex) {
				return;
			}
			
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			proxyComboBox.setEnabled(false);
			addressTextField.setEnabled(false);
			portTextField.setEnabled(false);
			okButton.setEnabled(false);
			//cancelButton.setEnabled(false);
			startDownload = System.currentTimeMillis();
			downloader = new Thread(this);
			downloader.start();	
		} else if (source==cancelButton) {
			if(downloader!=null){
				try{
				Thread tmpThread = downloader;
				downloader = null;
					if(tmpThread!=null){
						tmpThread.interrupt();
					}
				}
				catch(Exception ex) {
					System.out.println(ex);
				}
			}
			dispose();
		} else if (source==proxyComboBox) {
			updateProxy();
		}
	}
}
