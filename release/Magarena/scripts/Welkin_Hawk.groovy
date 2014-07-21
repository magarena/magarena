def CARD_NAMED_WELKIN_HAWK = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Welkin Hawk");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {      
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ search his or her library for a card named Welkin Hawk," + 
                "put that card into his or her hand, then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicTargetChoice(
                        CARD_NAMED_WELKIN_HAWK, 
                        "a card named Welkin Hawk"
                    ),
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]
