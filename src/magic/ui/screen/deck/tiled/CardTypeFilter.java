package magic.ui.screen.deck.tiled;

import magic.data.MagicIcon;
import magic.model.MagicType;
import magic.translate.MText;

// translatable strings
class Txt {
    static final String _S5 = "Creatures";
    static final String _S7 = "Lands";
    static final String _S9 = "Artifacts";
    static final String _S13 = "Enchantments";
    static final String _S15 = "Instants";
    static final String _S17 = "Sorceries";
    static final String _S19 = "Planeswalkers";
    static final String _S3 = "All";
}

enum CardTypeFilter {
    
    ALL(Txt._S3, null, null),
    CREATURES(Txt._S5, MagicType.Creature, MagicIcon.CREATURES),
    LANDS(Txt._S7, MagicType.Land, MagicIcon.LANDS),
    ARTIFACTS(Txt._S9, MagicType.Artifact, MagicIcon.ARTIFACTS),
    ENCHANTMENTS(Txt._S13, MagicType.Enchantment, MagicIcon.ENCHANTMENTS),
    INSTANTS(Txt._S15, MagicType.Instant, MagicIcon.INSTANTS),
    SORCERIES(Txt._S17, MagicType.Sorcery, MagicIcon.SORCERIES),
    PLANESWALKERS(Txt._S19, MagicType.Planeswalker, MagicIcon.PLANESWALKERS);

    private final String title;
    private final MagicType mtype;
    private final MagicIcon icon;

    private CardTypeFilter(String title, MagicType aType, MagicIcon icon) {
        this.title = MText.get(title);
        this.mtype = aType;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return title;
    }

    MagicType getMagicType() {
        return mtype;
    }

    MagicIcon getIcon() {
        return icon;
    }

    String getTitle() {
        return title;
    }
}
