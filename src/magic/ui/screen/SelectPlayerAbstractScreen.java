package magic.ui.screen;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import magic.MagicMain;
import magic.model.player.AiPlayer;
import magic.model.player.HumanPlayer;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.ui.screen.interfaces.IAvatarImageConsumer;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class SelectPlayerAbstractScreen
    extends AbstractScreen
    implements IAvatarImageConsumer {

    protected final Path playersPath;
    protected HashMap<String, PlayerProfile> profilesMap = new HashMap<String, PlayerProfile>();

    protected abstract JPanel getProfilesListPanel();
    protected abstract String getPlayerType();
    protected abstract void createDefaultPlayerProfiles() throws IOException;
    protected abstract void doNextAction();
    protected abstract int getPreferredWidth();
    protected abstract JList<? extends PlayerProfile> getProfilesJList();

    // CTR
    protected SelectPlayerAbstractScreen() {
        this.playersPath = Paths.get(MagicMain.getPlayerProfilesPath()).resolve(getPlayerType());
        setContent(new ScreenContent());
        setEnterKeyInputMap();
    }

    private void setEnterKeyInputMap() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "EnterAction");
        getActionMap().put("EnterAction", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doNextAction();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    protected void setFocusInProfilesJList(final JList<? extends PlayerProfile> profilesJList) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                profilesJList.requestFocusInWindow();
            }
        });
    }

    protected void updateAvatarImage(final Path imagePath, final PlayerProfile playerProfile) {
        final Path targetPath = playerProfile.getProfilePath().resolve("player.avatar");
        try {
            Files.copy(imagePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Deletes all directory contents and then directory itself.
     */
    protected void deleteDirectory(final Path root) {
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if(exc == null){
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                    throw exc;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected PlayerProfile getSelectedPlayer() {
        return getProfilesJList().getSelectedValue();
    }

    protected boolean deleteSelectedPlayerProfile(final PlayerProfile playerProfile) {
        boolean isInvalidAction = false;
        if (playerProfile instanceof HumanPlayer) {
            isInvalidAction = (PlayerProfiles.getHumanPlayerProfiles().size() <= 1);
        } else if (playerProfile instanceof AiPlayer) {
            isInvalidAction = (PlayerProfiles.getAiPlayerProfiles().size() <= 1);
        }
        if (isInvalidAction) {
            JOptionPane.showMessageDialog(this, "There must be at least one player.", "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (playerProfile instanceof AiPlayer) {
            if (PlayerProfiles.getAiPlayerProfiles().size() <= 1) {
                JOptionPane.showMessageDialog(this, "There must be at least one player.");
                return false;
            }
        }
        final int action = JOptionPane.showOptionDialog(
                this,
                "<html>This will delete the <b>" + playerProfile.getPlayerName() + "</b> player profile.<br>" +
                "All associated information such as player stats will also be removed.<br><br>" +
                "<b>This action cannot be undone!</b></html>",
                "Delete Player Profile?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] {"Delete", "Cancel"}, "Cancel");
        if (action == JOptionPane.YES_OPTION) {
            final Path profilePath = playersPath.resolve(playerProfile.getId());
            deleteDirectory(profilePath);
            return true;
        } else {
            return false;
        }
    }

    protected List<Path> getDirectoryPaths(final Path rootPath) {
        final List<Path> paths = new ArrayList<Path>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath, new DirectoriesFilter())) {
            for (Path p : ds) {
                paths.add(p.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }
    private static class DirectoriesFilter implements Filter<Path> {
        @Override
        public boolean accept(Path entry) throws IOException {
            return Files.isDirectory(entry);
        }
    }

    protected MenuButton getAvatarActionButton() {
        return new MenuButton("Avatar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFrame().showAvatarImagesScreen(SelectPlayerAbstractScreen.this);
            }
        }, "Update avatar image of selected player profile.");

    }

    private class ScreenContent extends JPanel {
        public ScreenContent() {
            setOpaque(false);
            setLayout(new MigLayout("insets 2, center, center"));
            add(getProfilesListPanel(), "w " + getPreferredWidth() + "!, h 80%");
        }
    }

    protected class ContainerPanel extends TexturedPanel {

        public ContainerPanel(final JList<? extends PlayerProfile> profilesJList) {
            profilesJList.setOpaque(false);
            setBorder(FontsAndBorders.BLACK_BORDER);
            setBackground(FontsAndBorders.MENUPANEL_COLOR);
            setLayout(new MigLayout("insets 0, gap 0, flowy"));
            add(new ScrollPane(profilesJList), "w 100%, h 100%");
        }

        private class ScrollPane extends JScrollPane {
            public ScrollPane(final JList<? extends PlayerProfile> profilesJList) {
                setViewportView(profilesJList);
                setBorder(BorderFactory.createEmptyBorder());
                setOpaque(false);
                getViewport().setOpaque(false);
            }
        }

    }

}
