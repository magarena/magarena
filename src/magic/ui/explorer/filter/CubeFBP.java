package magic.ui.explorer.filter;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.translate.UiString;

@SuppressWarnings("serial")
class CubeFBP extends FilterButtonPanel {

    // translatable strings
    private static final String _S6 = "Cube";

    CubeFBP(ActionListener aListener) {
        super(UiString.get(_S6));
        setPopupContent(
            MagicFormat.getCubeFilterLabels(),
            new JCheckBox[MagicFormat.getCubeFilterFormats().size()],
            new JRadioButton[FILTER_CHOICES.length],
            false,
            aListener
        );
    }

    @Override
    protected boolean isCardValid(final MagicCardDefinition card, final int i) {
        final MagicFormat fmt = MagicFormat.getCubeFilterFormats().get(i);
        return fmt.isCardLegal(card);
    }

}
