def filter = new MagicCardFilterImpl() {
    @Override
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equalsIgnoreCase("Gideon, Martial Paragon");
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
                new MagicMayChoice("Search for Gideon, Martial Paragon?"),
                this,
                "PN may\$ search PN's library and/or graveyard for a card named Gideon, Martial Paragon, reveal it, and put it into PN's hand. If PN search PN's library this way, shuffle it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardFilterChoice(
                    filter,
                    1,
                    false,
                    "a card named Gideon, Martial Paragon from your library and/or graveyard"
                ),
                MagicLocationType.OwnersHand
            ));
        }
    }
]

