package magic.ui;

import magic.translate.MText;
import magic.ui.helpers.ImageHelper;
import java.awt.Component;
import java.awt.Desktop;
import java.nio.file.Path;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.exception.handler.FileExceptionHandler;
import magic.exception.handler.ExceptionReport;
import magic.utility.MagicFileSystem;

public class UiExceptionHandler extends FileExceptionHandler {

    // translatable strings.
    private static final String _S1 = "Fatal Error";
    private static final String _S2 = "An unexpected error has occurred and Magarena will need to close.";
    private static final String _S3 = "Please consider submitting a crash report so that the development team can investigate.";
    private static final String _S4 = "Would you like to open the crash logs directory in file explorer?";

    @Override
    public void reportException(final ExceptionReport report) {
        super.reportException(report);
        if (ScreenController.getFrame() != null) {
            doScreenShot(ScreenController.getFrame().getContentPane());
        }
        doNotifyUser();
    }

    /**
     * Displays a message to user in the event an unexpected exception occurs.
     * User can open logs folder and/or Issue tracker directly from this dialog.
     */
    private static void doNotifyUser() {
        try {

            // By specifying a frame the JOptionPane will be shown in the taskbar.
            // Otherwise if the dialog is hidden it is easy to forget it is still open.
            final JFrame frame = new JFrame(MText.get(_S1));
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            String prompt = MText.get(_S2);
            if (Desktop.isDesktopSupported()) {
                prompt += String.format("\n\n%s\n%s", MText.get(_S3), MText.get(_S4));
                final int action = JOptionPane.showConfirmDialog(frame, prompt, MText.get(_S1), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null);
                if (action == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).toFile());
                }
            } else {
                JOptionPane.showMessageDialog(frame, prompt, MText.get(_S1), JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            // do nothing - crash report has already been generated and app is about to exit anyway.
        }
    }

    private static void doScreenShot(final Component container) {
        if (container != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    final Path filePath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).resolve("crash.png");
                    ImageHelper.doScreenshotToFile(container, filePath);
                } catch (Exception e) {
                    System.err.println("ScreenShot failed : " + e.toString());
                }
            });
        }
    }


}
