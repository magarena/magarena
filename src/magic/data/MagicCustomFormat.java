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
import magic.utility.MagicSystem;
import magic.model.MagicCardDefinition;

public class MagicCustomFormat extends MagicFormat {
    
    private final Set<String> legal = new HashSet<>();
    private final String name;

    public MagicCustomFormat(final String name) {
        this.name=name;
    }

    public void add(final String name) {
        legal.add(name);
    }

    @Override
    public CardLegality getCardLegality(final MagicCardDefinition card, final int cardCount) {
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

    public String getLabel() {
        return name + " (" + legal.size() + " cards)";
    }

    @Override
    public String toString() {
        return getLabel();
    }
    
    // static members
    
    private static final String CUBE_FILE_EXTENSION = "_cube.txt";
    private static final FileFilter CUBE_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isFile() && file.getName().endsWith(CUBE_FILE_EXTENSION);
        }
    };

    public static final MagicCustomFormat DEFAULT_CUBE = new MagicCustomFormat("all");
    private static List<MagicCustomFormat> values = Collections.emptyList();
    
    public static List<MagicCustomFormat> values() {
        return values;
    }
    
    public static MagicCustomFormat[] valuesArray() {
        return values.toArray(new MagicCustomFormat[0]);
    }
    
    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (final MagicCustomFormat cube : values()) {
            values.add(cube.getLabel());
        }
        return values.toArray(new String[0]);
    }
    
    private static void loadCustomFormat(final String name, final File file, final List<MagicCustomFormat> fmts) {
        List<String> content = Collections.emptyList();
        try { //load cube
            content = FileIO.toStrList(file);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + name);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        final MagicCustomFormat cubeDefinition = new MagicCustomFormat(name);
        for (final String line: content) {
            final String cardName = line.trim();
            if (!cardName.isEmpty()) {
                cubeDefinition.add(cardName);
            }
        }
        fmts.add(cubeDefinition);
    }
    
    public static void loadCustomFormats() {
        final List<MagicCustomFormat> fmts = new ArrayList<>();
        fmts.add(DEFAULT_CUBE);

        final File[] cubeFiles = MagicFileSystem.getDataPath(DataPath.MODS).toFile().listFiles(CUBE_FILE_FILTER);
        if (cubeFiles!=null) {
            for (final File file : cubeFiles) {
                final String name = file.getName();
                final int index = name.indexOf(CUBE_FILE_EXTENSION);
                loadCustomFormat(name.substring(0,index),file,fmts);
            }
        }

        if (MagicSystem.showStartupStats()) {
            System.err.println(fmts.size()+" cube definitions");
            for (final MagicCustomFormat cubeDefinition : fmts) {
                System.err.println("Cube " + cubeDefinition);
            }
        }

        values = Collections.unmodifiableList(fmts);
    }
    
    private static String getNameWithoutSize(final String cube) {
        final int toIndex = cube.indexOf("(");
        if (toIndex == -1) {
            return cube;
        } else {
            return cube.substring(0, toIndex).trim();
        }
    }
    
    public static MagicCustomFormat get(final String cubeLabel) {

        // prior to 1.62 the cube label including card count was saved to the duel
        // config file so for backwards compatibility during import need to check
        // for and remove card count if it exists to isolate just the cube name.
        final String cubeName = getNameWithoutSize(cubeLabel);
        
        for (final MagicCustomFormat cube : values) {
            if (cube.getName().equals(cubeName)) {
                return cube;
            }
        }
        
        return DEFAULT_CUBE;
    }
    
    public static MagicCustomFormat create(final Collection<MagicCardDefinition> cardPool) {
        final MagicCustomFormat cubeDefinition = new MagicCustomFormat("random");
        for (MagicCardDefinition card : cardPool) {
            cubeDefinition.add(card.getName());
        }
        return cubeDefinition;
    }
}
