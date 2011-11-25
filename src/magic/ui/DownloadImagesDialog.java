package magic.ui;

import magic.data.CardDefinitions;
import magic.data.DownloadImageFile;
import magic.data.DownloadMissingFiles;
import magic.data.IconImages;
import magic.data.WebDownloader;

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
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

public class DownloadImagesDialog extends JDialog implements Runnable,ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String DOWNLOAD_IMAGES_FILENAME="images.txt";
	
	private final MagicFrame frame;
	private final DownloadMissingFiles files;
	private final JComboBox proxyComboBox;
	private final JTextField addressTextField;
	//private final JTextField locationTextField;
	private final JTextField portTextField;
	private final JProgressBar progressBar;
	private final JLabel downloadLabel;
	private final JLabel downloadProgressLabel;
	private final JButton okButton;
	private final JButton cancelButton;
	private final Thread downloader;
	private Proxy proxy;
    private boolean cancelDownload = false;

	public DownloadImagesDialog(final MagicFrame frame) {
		super(frame,true);
		this.frame = frame;
		this.setLayout(new BorderLayout());
		this.setTitle("Download card images and text");
		this.setSize(300,405);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.downloader = new Thread(this);

		final JPanel downloadPanel=new JPanel();
		downloadPanel.setLayout(null);
		add(downloadPanel,BorderLayout.CENTER);
		
        int y = 25;
        int x = 10;
        final int WIDTH = 220;
        final int HEIGHT = 25;
        final int VSPACE = 5;
	
        /*
        final JLabel locationLabel=new JLabel("Location");
		locationLabel.setBounds(x,y,WIDTH,HEIGHT);
		
        y += HEIGHT;
        
        locationTextField=new JTextField();
		locationTextField.setBounds(x,y,WIDTH,HEIGHT);
		
        final JPanel locationPanel=new JPanel();
		locationPanel.setBorder(BorderFactory.createTitledBorder("Location"));
		locationPanel.setBounds(25,20,240,60);
		locationPanel.setLayout(null);
        locationPanel.add(locationLabel);
        locationPanel.add(locationTextField);
		downloadPanel.add(locationPanel);

        y = 25;
        */

		final Proxy.Type[] proxyTypes=Proxy.Type.values();
		final DefaultComboBoxModel proxyModel=new DefaultComboBoxModel(proxyTypes);
		proxyComboBox=new JComboBox(proxyModel);
		proxyComboBox.setBounds(x,y,WIDTH,HEIGHT);
		proxyComboBox.setFocusable(false);
		proxyComboBox.addActionListener(this);

        y += HEIGHT + VSPACE; 

		final JLabel addressLabel=new JLabel("Address");
		addressLabel.setBounds(x,y,WIDTH,HEIGHT);
		
        y += HEIGHT;
        
        addressTextField=new JTextField();
		addressTextField.setBounds(x,y,WIDTH,HEIGHT);

        y += HEIGHT + VSPACE;
        
        final JLabel portLabel=new JLabel("Port");
		portLabel.setBounds(x,y,WIDTH,HEIGHT);

        y += HEIGHT;

		portTextField=new JTextField();
		portTextField.setBounds(x,y,WIDTH,HEIGHT);
		
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
		progressBar.setBounds(x,30,WIDTH,HEIGHT);
		downloadLabel=new JLabel();
		downloadLabel.setBounds(x,60,WIDTH,HEIGHT);
		downloadProgressLabel = new JLabel();
		downloadProgressLabel.setBounds(x,80,WIDTH,HEIGHT);
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

		files=new DownloadMissingFiles(DOWNLOAD_IMAGES_FILENAME);
		if (files.isEmpty()) {
			okButton.setEnabled(false);
			progressBar.setMaximum(1);
			progressBar.setValue(1);
			downloadLabel.setText("All images and text are present.");
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

    @Override
    public void run() {
        //NOT run by EDT

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setMinimum(0);
                progressBar.setMaximum(files.size());
            }
        });
        
        int count=0;
        for (final WebDownloader file : files) {
            final int curr = count + 1;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    downloadProgressLabel.setText(curr+"/"+files.size());
                    downloadLabel.setText(file.getFilename());
                }
            });

            file.download(proxy);
            count++;

            if (cancelDownload) {
                break;
            }
           
            final int fcount = count;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setValue(fcount);
                }
            });
        }
			
		// clear images that are set to "missing image" in cache
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                magic.data.HighQualityCardImagesProvider.getInstance().clearCache();
                frame.updateGameView();
            }
        });
		
		// reload text
		CardDefinitions.loadCardTexts();
                
        IconImages.reloadSymbols();
      
        if (!cancelDownload) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    dispose();
                }
            });
        }
    }
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source=event.getSource();
		if (source==okButton) {
            final Proxy.Type proxyType=(Proxy.Type)proxyComboBox.getSelectedItem();
            if (proxyType==Type.DIRECT) {
                proxy=Proxy.NO_PROXY;
            } else {
                final String address=addressTextField.getText();
                try { //parse proxy port number	
                    final int port=Integer.parseInt(portTextField.getText());
                    proxy=new Proxy(proxyType,new InetSocketAddress(address,port));
                } catch (final NumberFormatException ex) {
                    return;
                }
            }
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			proxyComboBox.setEnabled(false);
			addressTextField.setEnabled(false);
			portTextField.setEnabled(false);
			okButton.setEnabled(false);
			downloader.start();	
		} else if (source==cancelButton) {
            cancelDownload = true;
			dispose();
		} else if (source==proxyComboBox) {
			updateProxy();
		}
	}
}
