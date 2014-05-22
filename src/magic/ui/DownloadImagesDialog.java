package magic.ui;

import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.DownloadImageFile;
import magic.data.DownloadMissingFiles;
import magic.data.FileIO;
import magic.data.GeneralConfig;
import magic.data.History;
import magic.data.IconImages;
import magic.data.WebDownloader;
import magic.model.MagicCardDefinition;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DownloadImagesDialog extends JFrame implements Runnable,ActionListener {
    private static final long serialVersionUID = 1L;

    private static final int GAP = 20;
    private static final int RGAP = 10;

    private final MagicFrame frame;
    private final DownloadMissingFiles files;
    private final JComboBox<Proxy.Type> proxyComboBox;
    private final JTextField addressTextField;
    private final JTextField portTextField;
    private final JProgressBar progressBar;
    private final JLabel downloadLabel;
    private final JLabel dirChosen;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JButton dirButton;
    private final Thread downloader;
    private File oldDataFolder = new File("");
    private Proxy proxy;
    private boolean cancelDownload;
    private static List<String> newCards = null;

    public DownloadImagesDialog(final MagicFrame frame) {
        super("Download card images and text");

        this.frame = frame;
        this.downloader = new Thread(this);

        final SpringLayout springLayout = new SpringLayout();
        this.setLayout(springLayout);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(frame);

        final Container contentPane = this.getContentPane();

        final JLabel dirLabel = new JLabel("Import data from previous version");
        add(dirLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, dirLabel,
                GAP,
                SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(
                SpringLayout.WEST, dirLabel,
                GAP,
                SpringLayout.WEST, contentPane);
        dirLabel.setVisible(false);

        dirButton = new JButton("Select Magarena data folder");
        dirButton.addActionListener(this);
        final Dimension d = dirButton.getPreferredSize();
        dirButton.setPreferredSize(new Dimension(300, d.height));
        add(dirButton);
        springLayout.putConstraint(
                SpringLayout.NORTH, dirButton,
                RGAP,
                SpringLayout.SOUTH, dirLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, dirButton,
                GAP,
                SpringLayout.WEST, contentPane);
        dirButton.setVisible(false);

        final JLabel dirStatus = new JLabel("Selected:");
        add(dirStatus);
        springLayout.putConstraint(
                SpringLayout.NORTH, dirStatus,
                RGAP,
                SpringLayout.SOUTH, dirButton);
        springLayout.putConstraint(
                SpringLayout.WEST, dirStatus,
                GAP,
                SpringLayout.WEST, contentPane);
        dirStatus.setVisible(false);

        dirChosen = new JLabel("None");
        add(dirChosen);
        springLayout.putConstraint(
                SpringLayout.NORTH, dirChosen,
                RGAP,
                SpringLayout.SOUTH, dirButton);
        springLayout.putConstraint(
                SpringLayout.WEST, dirChosen,
                RGAP,
                SpringLayout.EAST, dirStatus);
        dirChosen.setVisible(false);

        final JLabel proxyLabel = new JLabel("Proxy configuration");
        add(proxyLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, proxyLabel,
                GAP,
                SpringLayout.SOUTH, dirChosen);
        springLayout.putConstraint(
                SpringLayout.WEST, proxyLabel,
                GAP,
                SpringLayout.WEST, contentPane);

        final Proxy.Type[] proxyTypes=Proxy.Type.values();
        final ComboBoxModel<Proxy.Type> proxyModel=new DefaultComboBoxModel<Proxy.Type>(proxyTypes);
        proxyComboBox=new JComboBox<Proxy.Type>(proxyModel);
        proxyComboBox.setFocusable(false);
        proxyComboBox.addActionListener(this);
        add(proxyComboBox);
        springLayout.putConstraint(
                SpringLayout.NORTH, proxyComboBox,
                RGAP,
                SpringLayout.SOUTH, proxyLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, proxyComboBox,
                GAP,
                SpringLayout.WEST, contentPane);
        springLayout.putConstraint(
                SpringLayout.EAST, proxyComboBox,
                -GAP,
                SpringLayout.EAST, contentPane);

        final JLabel urlLabel = new JLabel("URL");
        add(urlLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, urlLabel,
                RGAP,
                SpringLayout.SOUTH, proxyComboBox);
        springLayout.putConstraint(
                SpringLayout.WEST, urlLabel,
                GAP,
                SpringLayout.WEST, contentPane);

        addressTextField=new JTextField();
        add(addressTextField);
        springLayout.putConstraint(
                SpringLayout.BASELINE, addressTextField,
                0,
                SpringLayout.BASELINE, urlLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, addressTextField,
                RGAP,
                SpringLayout.EAST, urlLabel);
        springLayout.putConstraint(
                SpringLayout.EAST, addressTextField,
                -GAP,
                SpringLayout.EAST, contentPane);


        final JLabel portLabel = new JLabel("Port");
        add(portLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, portLabel,
                RGAP,
                SpringLayout.SOUTH, urlLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, portLabel,
                GAP,
                SpringLayout.WEST, contentPane);

        portTextField=new JTextField();
        add(portTextField);
        springLayout.putConstraint(
                SpringLayout.BASELINE, portTextField,
                0,
                SpringLayout.BASELINE, portLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, portTextField,
                RGAP,
                SpringLayout.EAST, urlLabel);
        springLayout.putConstraint(
                SpringLayout.EAST, portTextField,
                -GAP,
                SpringLayout.EAST, contentPane);

        final JLabel progressLabel = new JLabel("Progress");
        add(progressLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, progressLabel,
                GAP,
                SpringLayout.SOUTH, portLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, progressLabel,
                GAP,
                SpringLayout.WEST, contentPane);

        progressBar=new JProgressBar();
        add(progressBar);
        springLayout.putConstraint(
                SpringLayout.NORTH, progressBar,
                RGAP,
                SpringLayout.SOUTH, progressLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, progressBar,
                GAP,
                SpringLayout.WEST, contentPane);
        springLayout.putConstraint(
                SpringLayout.EAST, progressBar,
                -GAP,
                SpringLayout.EAST, contentPane);

        downloadLabel=new JLabel();
        downloadLabel.setText("");
        add(downloadLabel);
        springLayout.putConstraint(
                SpringLayout.NORTH, downloadLabel ,
                RGAP,
                SpringLayout.SOUTH, progressBar);
        springLayout.putConstraint(
                SpringLayout.WEST, downloadLabel,
                GAP,
                SpringLayout.WEST, contentPane);


        cancelButton=new JButton("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.setIcon(IconImages.CANCEL);
        cancelButton.addActionListener(this);
        add(cancelButton);
        springLayout.putConstraint(
                SpringLayout.NORTH, cancelButton,
                GAP,
                SpringLayout.SOUTH, downloadLabel);
        springLayout.putConstraint(
                SpringLayout.EAST, cancelButton,
                -GAP,
                SpringLayout.EAST, contentPane);

        okButton=new JButton("OK");
        okButton.setFocusable(false);
        okButton.setIcon(IconImages.OK);
        okButton.addActionListener(this);
        add(okButton);
        springLayout.putConstraint(
                SpringLayout.NORTH, okButton,
                GAP,
                SpringLayout.SOUTH, downloadLabel);
        springLayout.putConstraint(
                SpringLayout.EAST, okButton,
                -RGAP,
                SpringLayout.WEST, cancelButton);


        files=new DownloadMissingFiles();
        if (files.isEmpty()) {
            okButton.setEnabled(false);
            progressBar.setMaximum(1);
            progressBar.setValue(1);
            downloadLabel.setText("All images and text are present.");
        } else {
            progressBar.setString("0/" + files.size());
            progressBar.setStringPainted(true);
            downloadLabel.setText("Press OK to begin or Cancel.");
        }

        updateProxy();

        springLayout.putConstraint(
                SpringLayout.EAST, contentPane,
                GAP,
                SpringLayout.EAST, dirButton);
        springLayout.putConstraint(
                SpringLayout.SOUTH, contentPane,
                GAP,
                SpringLayout.SOUTH, cancelButton);

        this.pack();
        this.setLocationRelativeTo(frame);
        this.setVisible(true);
    }

    private void updateProxy() {
        final boolean use = proxyComboBox.getSelectedItem() != Proxy.Type.DIRECT;
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
            new File(oldDataFolder, History.HISTORY_FOLDER)
        };

        int count=0;
        final List<String> downloadedImages = new ArrayList<String>();

        for (final WebDownloader file : files) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
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

            if (!file.exists()) {
                file.download(proxy);
                if (file instanceof DownloadImageFile) {
                    downloadedImages.add(((DownloadImageFile)file).getCardName());
                }
            }

            count++;

            if (cancelDownload) {
                break;
            }

            final int fcount = count;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setString(fcount+"/"+files.size());
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

        saveDownloadLog(downloadedImages);

        if (!cancelDownload) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GeneralConfig.getInstance().setIsMissingFiles(false);
                    dispose();
                }
            });
        }
    }

    private void saveDownloadLog(final List<String> downloadLog) {
        final Path logPath = Paths.get(MagicMain.getLogsPath()).resolve("downloads.log");
        try (final PrintWriter writer = new PrintWriter(logPath.toFile())) {
            for (String cardName : downloadLog) {
                writer.println(cardName);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        };
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source=event.getSource();
        if (source==okButton) {
            final Proxy.Type proxyType = proxyComboBox.getItemAt(proxyComboBox.getSelectedIndex());
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
            final int returnVal = fc.showOpenDialog(this);
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

    public static boolean isCardInDownloadsLog(MagicCardDefinition card) {
        if (newCards == null) {
            newCards = getCardNamesFromDownloadLog();
        }
        return newCards.contains(card.getName());
    }

    private static List<String> getCardNamesFromDownloadLog() {
        final List<String> cardNames = new ArrayList<String>();
        Path logPath = Paths.get(MagicMain.getLogsPath()).resolve("downloads.log");
        if (logPath.toFile().exists()) {
            try {
                for (final String cardName : Files.readAllLines(logPath, Charset.defaultCharset())) {
                    cardNames.add(cardName.trim());
                }
            } catch (final IOException ex) {
               throw new RuntimeException(ex);
            }
        }
        return cardNames;
    }

    public static void clearLoadedLogs() {
        if (newCards != null) {
            newCards.clear();
            newCards = null;
        }
    }

}
