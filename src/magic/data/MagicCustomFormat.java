package magic.data;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.model.MagicCardDefinition;

public class MagicCustomFormat extends MagicFormat {

    private final Set<String> legal = new HashSet<>();
    private final String name;
    private final File file;

    public MagicCustomFormat(final String aName) {
        this(aName, null);
    }

    public MagicCustomFormat(final String aName, final File aFile) {
        name = aName;
        file = aFile;
    }

    private void add(final String name) {
        legal.add(name);
    }

    @Override
    public CardLegality getCardLegality(final MagicCardDefinition card, final int cardCount) {
        if (legal.isEmpty()) {
            load();
        }
        return (legal.contains(card.getName())) ? CardLegality.Legal : CardLegality.Illegal;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMinimumDeckSize() {
        return 40;
    }

    @Override
    public String getLabel() {
        return name;
    }

    private void load() {
        List<String> content = Collections.emptyList();
        try { //load cube
            content = FileIO.toStrList(file);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + name);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        for (final String line: content) {
            final String cardName = line.trim();
            if (!cardName.isEmpty()) {
                legal.add(cardName);
            }
        }
    }

    //
    // static members
    //

    private static final String CUBE_FILE_EXTENSION = "_cube.txt";
    private static final FileFilter CUBE_FILE_FILTER = file -> file.isFile() && file.getName().endsWith(CUBE_FILE_EXTENSION);

    private static List<MagicFormat> customFormats;

    public static void loadCustomFormats() {
        final List<MagicFormat> fmts = new ArrayList<>();
        final File[] cubeFiles = MagicFileSystem.getDataPath(DataPath.MODS).toFile().listFiles(CUBE_FILE_FILTER);
        if (cubeFiles!=null) {
            for (final File file : cubeFiles) {
                final String name = file.getName();
                final int index = name.indexOf(CUBE_FILE_EXTENSION);
                fmts.add(new MagicCustomFormat(name.substring(0,index),file));
            }
        }
        customFormats = Collections.unmodifiableList(fmts);
    }

    static List<MagicFormat> values() {
        return customFormats;
    }

    private static String getNameWithoutSize(final String cube) {
        final int toIndex = cube.indexOf("(");
        if (toIndex == -1) {
            return cube;
        } else {
            return cube.substring(0, toIndex).trim();
        }
    }

    public static MagicFormat get(final String cubeLabel) {

        // prior to 1.62 the cube label including card count was saved to the duel
        // config file so for backwards compatibility during import need to check
        // for and remove card count if it exists to isolate just the cube name.
        final String cubeName = getNameWithoutSize(cubeLabel);

        for (final MagicFormat cube : MagicFormat.getDuelFormats()) {
            if (cube.getName().equals(cubeName)) {
                return cube;
            }
        }

        return MagicFormat.ALL;
    }

    public static MagicFormat create(final Collection<MagicCardDefinition> cardPool) {
        final MagicCustomFormat cubeDefinition = new MagicCustomFormat("random");
        for (MagicCardDefinition card : cardPool) {
            cubeDefinition.add(card.getName());
        }
        return cubeDefinition;
    }
}
