package magic.awt;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import magic.utility.MagicResources;

public enum MagicFont {
    
    MPlantin("/cardbuilder/fonts/MPlantin.ttf"),
    MPlantinBold("/cardbuilder/fonts/MPlantin-Bold.ttf"),
    MPlantinItalic("/cardbuilder/fonts/MPlantin-Italic.ttf"),
    BelerenBold("/cardbuilder/fonts/Beleren-Bold.ttf"),
    BelerenSmallCaps("/cardbuilder/fonts/Beleren Small Caps.ttf"),
    JaceBelerenBold("/cardbuilder/fonts/JaceBeleren-Bold.ttf");

    private static final Font FALLBACK_FONT = new Font("serif", Font.PLAIN, 24);

    private static final Map<TextAttribute, Object> FONT_MAP = new HashMap<>();
    static {
        FONT_MAP.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
    }

    private final String ttfFile;
    private Font font;

    private MagicFont(String ttf) {
        this.ttfFile = ttf;
    }

    public Font get() {
        if (font == null) {
            tryLoadFont();
        }
        return font;
    }

    private void tryLoadFont() {
        try (final InputStream is = MagicResources.getJarResourceStream(ttfFile)) {
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(FONT_MAP);
            // register font so it works with html text.
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (Exception ex) {
            System.err.println(ttfFile + " not loaded (using default) : " + ex);
            font = FALLBACK_FONT;
        }
    }
}
