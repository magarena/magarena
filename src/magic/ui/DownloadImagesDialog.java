package magic.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import magic.data.DownloadImageFile;
import magic.data.DownloadImageFiles;

public class DownloadImagesDialog extends JDialog implements Runnable {

	private static final long serialVersionUID = 1L;

	private final JProgressBar progressBar;
	private final JLabel downloadLabel;
	
	public DownloadImagesDialog(final MagicFrame frame) {

		super(frame,true);
		this.setLayout(null);
		this.setTitle("Downloading images");
		this.setSize(300,150);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		progressBar=new JProgressBar();
		progressBar.setBounds(40,40,220,25);
		add(progressBar);
		downloadLabel=new JLabel();
		downloadLabel.setBounds(40,80,220,25);
		add(downloadLabel);
		
		new Thread(this).start();
		
		setVisible(true);		
	}

	@Override
	public void run() {

		final DownloadImageFiles files=new DownloadImageFiles();
		if (files.isEmpty()) {
			dispose();
			return;
		}		
		progressBar.setMinimum(0);
		progressBar.setMaximum(files.size());
		
		int count=0;
		for (final DownloadImageFile file : files) {

			downloadLabel.setText(file.getFilename());
			file.download();
			progressBar.setValue(++count);
		}
		
		dispose();
		System.exit(0);
	}
}