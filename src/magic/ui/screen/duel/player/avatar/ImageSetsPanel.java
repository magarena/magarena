package magic.ui.screen.duel.player.avatar;

import java.awt.Color;
import java.awt.Cursor;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import magic.ui.FontsAndBorders;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ImageSetsPanel extends TexturedPanel implements IThemeStyle {

    ImageSetsPanel(final AvatarImagesScreen screen) {

        // List of avatar image sets.
        final JList<AvatarImageSet> imageSetsList = new JList<>(getAvatarImageSetsArray());
        imageSetsList.setOpaque(false);
        imageSetsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SwingUtilities.invokeLater(() -> {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    screen.displayImageSetIcons(imageSetsList.getSelectedValue());
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                });
            }
        });
        imageSetsList.setSelectedIndex(0);

        final AvatarListCellRenderer renderer = new AvatarListCellRenderer();
        imageSetsList.setCellRenderer(renderer);

        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(imageSetsList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        setLayout(new MigLayout("insets 0, gap 0, flowy"));
        setBorder(FontsAndBorders.BLACK_BORDER);
        add(scrollPane, "w 100%, h 100%");

        refreshStyle();
    }

    private static class DirectoriesOnlyFilter implements DirectoryStream.Filter<Path> {
        @Override
        public boolean accept(Path entry) {
            return Files.isDirectory(entry);
        }
    }

    private List<Path> getSortedDirectoryPaths(final Path rootDirectory) {
        final List<Path> paths = new ArrayList<>();
        try (DirectoryStream<Path> ds =
                Files.newDirectoryStream(
                        rootDirectory,
                        new DirectoriesOnlyFilter())) {
            for (Path p : ds) {
                paths.add(p);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        paths.sort(Comparator.comparing(Path::getFileName));
        return paths;
    }

    private AvatarImageSet loadImageSet(final Path imageSetDirectory) {
        return new AvatarImageSet(imageSetDirectory);
    }

    private List<AvatarImageSet> getAvatarImageSetsList() {
        final List<AvatarImageSet> imageSets = new ArrayList<>();
        List<Path> directoryPaths = getSortedDirectoryPaths(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.AVATARS));
        for (Path path : directoryPaths) {
            imageSets.add(loadImageSet(path));
        }
        return imageSets;
    }

    private AvatarImageSet[] getAvatarImageSetsArray() {
        final List<AvatarImageSet> imageSetsList = getAvatarImageSetsList();
        return imageSetsList.toArray(new AvatarImageSet[0]);
    }

    @Override
    public final void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 200);
        setBackground(thisBG);
    }

}
