package magic.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class DeckFileVisitor extends SimpleFileVisitor<Path> {

    private static final PathMatcher matcher =
        FileSystems.getDefault().getPathMatcher("glob:**.dec");

    private final List<File> files = new ArrayList<>();

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return DeckUtils.isValidDeckFolder(dir)
            ? FileVisitResult.CONTINUE
            : FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (matcher.matches(file) && attrs.isRegularFile()) {
            files.add(file.toFile());
        }
        return FileVisitResult.CONTINUE;
    }

    List<File> getFiles() {
        return files;
    }

}
