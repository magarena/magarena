def CARD_NAMED_SCREAMING_SEAHAWK = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Screaming Seahawk");
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
                new MagicMayChoice(),
                this,
                "PN may\$ search his or her library for a card named Screaming Seahawk, " + 
                "reveal it, and put it into PN's hand. If PN does, shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicTargetChoice(
                        CARD_NAMED_SCREAMING_SEAHAWK, 
                        "a card named Screaming Seahawk"
                    ),
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]
