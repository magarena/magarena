package magic.ui.widget.firemind;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Proxy;
import java.nio.file.Path;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import magic.FiremindQueueWorker;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FiremindWorkerPanel extends JPanel {

    protected final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final JTextField accessKeyTextField = new JTextField();
    private final MigLayout migLayout = new MigLayout();
    protected final JLabel captionLabel = getCaptionLabel(getProgressCaption());
    private final JButton runButton = new JButton();
    private final JButton cancelButton = new JButton("Cancel");

    
    @SuppressWarnings("unused")
	private boolean isCancelled = false;
    private SwingWorker<String, Void> firemindWorker;
    private boolean isRunning = false;
    protected SwingWorker<String, Void> getFiremindWorker(final Proxy proxy) {
        return new FiremindWorkerRunner(); //TODO (downloadList, CONFIG.getProxy());
    }
    protected String getProgressCaption(){
    	if(isRunning){
    		return "Running";
    	}else{
    		return "Not running";
    	}
    	
    }
    

    protected String getLogFilename() {
        return "fireindWorker.log";
    }
    
    protected String getStartButtonCaption() {
        return "Start Firemind Worker";
    }

    public FiremindWorkerPanel() {
    	CONFIG.load();
        setLookAndFeel();
        refreshLayout();
        setActions();
//        buildDownloadImagesList();
    }

    public boolean isRunning() {
        return this.isRunning;
    }
    
    protected void saveDownloadLog(final List<String> downloadLog) {
        final Path logPath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve(getLogFilename());
        System.out.println("saving log : " + logPath);
        try (final PrintWriter writer = new PrintWriter(logPath.toFile())) {
            for (String cardName : downloadLog) {
                writer.println(cardName);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setActions() {
        runButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CONFIG.setFiremindAccessToken(accessKeyTextField.getText().trim());
                CONFIG.save();
                
                setRunningState();
                notifyStatusChanged(true);
                firemindWorker = getFiremindWorker(CONFIG.getProxy());
                firemindWorker.execute();
            }
        });
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doCancelFiremindWorker();
            }
        });
    }
    
    public void doCancel() {
        isCancelled = true;
        doCancelFiremindWorker();
    }

    private void doCancelFiremindWorker() {
        if (firemindWorker != null && !firemindWorker.isCancelled() & !firemindWorker.isDone()) {
        	firemindWorker.cancel(true);
            setButtonState(false);
        }
    }

//    protected final void buildDownloadImagesList() {
//        if (!isCancelled) {
//            captionLabel.setIcon(IconImages.BUSY16);
//            captionLabel.setText(getProgressCaption());
//            imagesScanner = new ImagesScanner();
//            imagesScanner.execute();
//            downloadButton.setEnabled(false);
//            notifyStatusChanged(DownloaderState.SCANNING);
//        }
//    }    


    protected void setButtonState(final boolean isRunning) {
        runButton.setVisible(!isRunning);
        cancelButton.setVisible(isRunning);
        refreshLayout();
    }

    private void setRunningState() {
        setButtonState(true);
        captionLabel.setIcon(IconImages.BUSY16);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 2, gapy 0");
        accessKeyTextField.setText(CONFIG.getFiremindAccessToken());
        add(accessKeyTextField, "w 100%");
        add(runButton.isVisible() ? runButton : cancelButton, "w 100%");
    }

    private void setLookAndFeel() {
        // Layout manager.
        setLayout(migLayout);
        // download button
        runButton.setEnabled(true);
        runButton.setText(getStartButtonCaption());
    }

    private JLabel getCaptionLabel(final String text) {
        final ImageIcon ii = IconImages.BUSY16;
        final JLabel lbl = new JLabel(ii);
        lbl.setText(text);
        lbl.setHorizontalAlignment(SwingConstants.LEFT);
        lbl.setHorizontalTextPosition(SwingConstants.LEADING);
        lbl.setOpaque(false);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        return lbl;
    }

    protected void notifyStatusChanged(boolean newValue) {
        assert SwingUtilities.isEventDispatchThread();
        final boolean oldState = this.isRunning;
        this.isRunning = newValue;
        firePropertyChange("isRunning", oldState, newValue);
    }

    private class FiremindWorkerRunner extends SwingWorker<String, Void> {
        @Override
        protected String doInBackground() throws Exception {
        	String[] arguments = new String[]{""};
            FiremindQueueWorker.main(arguments);
            return "finished";
        }
        @Override
        protected void done() {
//            try {
//                files = get();
//            } catch (InterruptedException | ExecutionException ex) {
//                throw new RuntimeException(ex);
//            } catch (CancellationException ex) {
////                System.out.println("ImagesScanner cancelled by user!");
//            }
//            if (!isCancelled) {
//                downloadButton.setEnabled(files.size() > 0);
//                captionLabel.setIcon(null);
//                captionLabel.setText(getProgressCaption() + files.size());
//            }
//            notifyStatusChanged(DownloaderState.STOPPED);
        }
    }

}