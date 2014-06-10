package magic.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import magic.MagicMain;
import magic.model.MagicCardDefinition;

public enum MagicFormats {

    // add new formats here...
    // @name: display name in UI.
    // @filename: case-sensitive name of file (without extension) in magic/data/formats.

    EXTENDED ("Extended", "extended"),
    STANDARD ("Standard", "standard");

    private final String name;
    private final String filename;

    private static final HashMap<MagicFormats, MagicFormatDefinition> loadedFormats =
            new HashMap<MagicFormats, MagicFormatDefinition>();

    private MagicFormats(final String name, final String filename) {
        this.name = name;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<String>();
        for (MagicFormats f : MagicFormats.values()) {
            values.add(f.getName());
        }
        return values.toArray(new String[values.size()]);
    }

    public static boolean isCardLegal(MagicCardDefinition card, MagicFormats magicFormatType) {
        if (!loadedFormats.containsKey(magicFormatType)) {
            loadedFormats.put(magicFormatType, loadMagicFormatFile(magicFormatType));
        }
        final MagicFormatDefinition magicFormat = loadedFormats.get(magicFormatType);
        return magicFormat.contains(card);
    }

    private static MagicFormatDefinition loadMagicFormatFile(MagicFormats magicFormatType) {

        String content = null;
        final String filename = "/magic/data/formats/" + magicFormatType.getFilename() + ".fmt";
        try {
            content = FileIO.toStr(MagicMain.rootFrame.getClass().getResourceAsStream(filename));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + filename);
            return null;
        }

        final MagicFormatDefinition magicFormat = new MagicFormatDefinition();

        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                final boolean skipLine = (line.startsWith("#") || line.isEmpty());
                if (!skipLine) {
                    switch (line.substring(0, 1)) {
                    case "!":
                        magicFormat.addBannedCardName(line.substring(1));
                        break;
                    case "*":
                        magicFormat.addRestrictedCardName(line.substring(1));
                        break;
                    default:
                        magicFormat.addSetCode(line);
                    }
                }
            }
        }

        return magicFormat;
    }

}
