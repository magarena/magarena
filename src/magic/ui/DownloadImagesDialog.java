package magic.ui;

import magic.MagicMain;

import magic.data.CardDefinitions;
import magic.data.DownloadMissingFiles;
import magic.data.History;
import magic.data.IconImages;
import magic.data.WebDownloader;
import magic.data.FileIO;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JFileChooser;

import java.io.File;
import java.io.IOException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Proxy;

import net.miginfocom.swing.MigLayout;

public class DownloadImagesDialog extends JFrame implements Runnable,ActionListener {
	private static final long serialVersionUID = 1L;

	private static final String DOWNLOAD_IMAGES_FILENAME="images.txt";
	
	private final MagicFrame frame;
	private final DownloadMissingFiles files;
	private final JComboBox proxyComboBox;
	private final JTextField addressTextField;
	private final JTextField portTextField;
	private final JProgressBar progressBar;
	private final JLabel downloadLabel;
	private final JLabel downloadProgressLabel;
    private final JLabel dirChosen;
	private final JButton okButton;
	private final JButton cancelButton;
	private final JButton dirButton;
	private final Thread downloader;
    private File oldDataFolder = new File("");
	private Proxy proxy;
    private boolean cancelDownload = false;

	public DownloadImagesDialog(final MagicFrame frame) {
		super("Download card images and text");

        this.frame = frame;
		this.downloader = new Thread(this);
		
        this.setLayout(new MigLayout("ins 20, fillx, wrap 1","[grow]"));
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		add(new JLabel("Import data from previous version"), "gapbottom rel, w 300");
		
        dirButton = new JButton("Select Magarena data folder");
		dirButton.addActionListener(this);
        add(dirButton, "growx, gapbottom rel"); 
        
        add(new JLabel("Selected:"), "split 2");

        dirChosen = new JLabel("None");
        add(dirChosen, "growx, gapbottom unrel");

		add(new JLabel("Proxy configuration"), "gapbottom rel");

        final Proxy.Type[] proxyTypes=Proxy.Type.values();
		final DefaultComboBoxModel proxyModel=new DefaultComboBoxModel(proxyTypes);
		proxyComboBox=new JComboBox(proxyModel);
		proxyComboBox.setFocusable(false);
		proxyComboBox.addActionListener(this);
        add(proxyComboBox, "growx, gapbottom rel");

	    add(new JLabel("URL"), "split 2");
        addressTextField=new JTextField();
        add(addressTextField, "growx, gapbottom rel");

        add(new JLabel("Port"), "split 2");
		portTextField=new JTextField();
        add(portTextField, "growx, gapbottom unrel");
		
        add(new JLabel("Progress"), "gapbottom rel");
		
		progressBar=new JProgressBar();
        add(progressBar, "growx");

		downloadLabel=new JLabel();
		downloadLabel.setText("");
        add(downloadLabel, "growx");

		downloadProgressLabel = new JLabel();
		downloadProgressLabel.setText("");
        add(downloadProgressLabel, "growx");
        
		okButton=new JButton("OK");
		okButton.setFocusable(false);
		okButton.setIcon(IconImages.OK);
		okButton.addActionListener(this);
        add(okButton, "tag ok, split 2");
		
        cancelButton=new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.setIcon(IconImages.CANCEL);
		cancelButton.addActionListener(this);
        add(cancelButton, "tag cancel");

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
        this.pack();
		this.setVisible(true);		
	}
	
	private void updateProxy() {
		final boolean use=proxyComboBox.getSelectedItem()!=Proxy.Type.DIRECT;
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
        

        final File[] oldDirs = {
            new File(oldDataFolder, CardDefinitions.CARD_IMAGE_FOLDER),
            new File(oldDataFolder, CardDefinitions.TOKEN_IMAGE_FOLDER),
            new File(oldDataFolder, CardDefinitions.CARD_TEXT_FOLDER),
            new File(oldDataFolder, "symbols"),
            new File(oldDataFolder, History.HISTORY_FOLDER)
        };
        
        int count=0;

        for (final WebDownloader file : files) {
            final int curr = count + 1;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    downloadProgressLabel.setText(curr+"/"+files.size());
                    downloadLabel.setText(file.getFilename());
                }
            });

            //check if file is in previous version
            for (final File oldDir : oldDirs) {
                final File oldFile = new File(oldDir, file.getFilename());
                if (oldFile.exists()) {
                    try {
                        FileIO.copyFile(oldFile, file.getFile());
                        break;
                    } catch (IOException ex) {
                        System.err.println("Unable to copy " + oldFile);
                    }
                }
            }
            
            if (!file.getFile().exists()) {
                file.download(proxy);
            }

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
            if (proxyType==Proxy.Type.DIRECT) {
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
		} else if (source==dirButton) {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                oldDataFolder = fc.getSelectedFile();
                final File subFolder = new File(oldDataFolder, MagicMain.getGameFolder());
                if (subFolder.exists()) {
                    oldDataFolder = subFolder;
                }
                dirChosen.setText(oldDataFolder.getName());
            }
        }
	}
}
