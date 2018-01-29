def A_CARD_NAMED_NICOL_BOLAS_THE_DECEIVER_FROM_YOUR_LIBRARY_OR_GRAVEYARD = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("Nicol Bolas, the Deceiver");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Library || targetType == MagicTargetType.Graveyard;
    }
}

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ search PN's library and/or graveyard for a card named Nicol Bolas, the Deceiver, reveal it, and put it into PN's hand. If PN searches PN's library this way, shuffle it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        A_CARD_NAMED_NICOL_BOLAS_THE_DECEIVER_FROM_YOUR_LIBRARY_OR_GRAVEYARD,
                        1,
                        false,
                        "a card named Nicol Bolas, the Deceiver from your library or graveyard"
                    )
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]

