package magic.data;

import magic.utility.FileIO;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.utility.MagicSystem;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class CustomFormats {

    private static final String CUBE_FILE_EXTENSION = "_cube.txt";
    private static final FileFilter CUBE_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isFile() && file.getName().endsWith(CUBE_FILE_EXTENSION);
        }
    };

    public static final MagicCustomFormat DEFAULT_CUBE = new MagicCustomFormat("all");

    private static final List<MagicCustomFormat> cubeDefinitions = new ArrayList<>();
    private static MagicCustomFormat currentCube;
    static {
        cubeDefinitions.add(DEFAULT_CUBE);
        currentCube = DEFAULT_CUBE;
    }

    public static MagicCustomFormat[] getCubesArray() {
        return cubeDefinitions.toArray(new MagicCustomFormat[cubeDefinitions.size()]);
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicCustomFormat cube : cubeDefinitions) {
            if (cube != DEFAULT_CUBE) {
                values.add(cube.getLabel());
            }
        }
        return values.toArray(new String[values.size()]);
    }

    private static void loadCubeDefinition(final String name,final File file) {
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
        cubeDefinitions.add(cubeDefinition);
    }

    public static void loadCubeDefinitions() {
        final File[] cubeFiles = MagicFileSystem.getDataPath(DataPath.MODS).toFile().listFiles(CUBE_FILE_FILTER);
        if (cubeFiles!=null) {
            for (final File file : cubeFiles) {
                final String name = file.getName();
                final int index = name.indexOf(CUBE_FILE_EXTENSION);
                loadCubeDefinition(name.substring(0,index),file);
            }
        }

        if (MagicSystem.showStartupStats()) {
            System.err.println(cubeDefinitions.size()+" cube definitions");
            for (final MagicCustomFormat cubeDefinition : cubeDefinitions) {
                System.err.println("Cube " + cubeDefinition);
            }
        }
    }

    public static boolean isCardInCube(final MagicCardDefinition card, final String cubeName) {
        final MagicCustomFormat cube = getCube(cubeName);
        return cube.isCardLegal(card);
    }

    public static MagicCustomFormat getCube(final String cubeLabel) {

        // prior to 1.62 the cube label including card count was saved to the duel
        // config file so for backwards compatibility during import need to check
        // for and remove card count if it exists to isolate just the cube name.
        final String cubeName = getCubeNameWithoutSize(cubeLabel);
        
        if (!currentCube.getName().equals(cubeName)) {
            for (MagicCustomFormat cube : cubeDefinitions) {
                if (cube.getName().equals(cubeName)) {
                    currentCube = cube;
                    break;
                }
            }
        }
        return currentCube;
        
    }

    public static MagicCustomFormat createCube(Collection<MagicCardDefinition> cardPool) {
        final MagicCustomFormat cubeDefinition = new MagicCustomFormat("random");
        for (MagicCardDefinition card : cardPool) {
            cubeDefinition.add(card.getName());
        }
        return cubeDefinition;
    }

    private static String getCubeNameWithoutSize(final String cube) {
        final int toIndex = cube.indexOf("(");
        if (toIndex == -1) {
            return cube;
        } else {
            return cube.substring(0, toIndex).trim();
        }
    }

}
