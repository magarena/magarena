package magic.ui;

import magic.ui.utility.GraphicsUtils;
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

    @Override
    public void reportException(final ExceptionReport report) {
        super.reportException(report);
        if (ScreenController.getMainFrame() != null) {
            doScreenShot(ScreenController.getMainFrame().getContentPane());
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
            final JFrame frame = new JFrame("Fatal Error");
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            String prompt = "An unexpected error has occurred and Magarena will need to close.";
            if (Desktop.isDesktopSupported()) {
                prompt +=
                    "\n\nPlease consider submitting a crash report so that the development team can investigate.\n" +
                    "Would you like to open the crash logs directory in file explorer?";
                final int action = JOptionPane.showConfirmDialog(frame, prompt, "Fatal Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null);
                if (action == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).toFile());
                }
            } else {
                JOptionPane.showMessageDialog(frame, prompt, "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            // do nothing - crash report has already been generated and app is about to exit anyway.
        }
    }

    private static void doScreenShot(final Component container) {
        if (container != null) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        final Path filePath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).resolve("crash.png");
                        GraphicsUtils.doScreenshotToFile(container, filePath);
                     } catch (Exception e) {
                        System.err.println("ScreenShot failed : " + e.toString());
                    }
                }
            });
        }
    }


}
