def CREATURE_CMC_6_OR_MORE = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return (target.hasType(MagicType.Creature) && target.getConvertedCost() > 5);
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
                "PN may search his or her library for a creture card with converted mana cost 6 or greater, reveal it, " +
                "put it into his or her hand, and shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                new MagicMayChoice(
                    "Search for a creture card?",
                    new MagicTargetChoice(CREATURE_CMC_6_OR_MORE, "a creature with converted mana cost 6 or greater")
                )
            ));
        }
    }
]
