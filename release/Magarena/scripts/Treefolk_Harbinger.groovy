def TREEFOLK_OR_FOREST_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Forest) || target.hasSubType(MagicSubType.Treefolk);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

[
    new MagicWhenComesIntoPlayTrigger() {
       @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for a Treefolk or Forest card, reveal it, then shuffle his or her library and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoLibraryEvent(
                event,
                new MagicMayChoice(
                    "Search for a Treefolk or Forest card?",
                    new MagicTargetChoice(TREEFOLK_OR_FOREST_CARD_FROM_LIBRARY, "a Treefolk or Forest card from your library")
                )
            ));
        }
    }
]
