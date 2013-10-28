def DelayedTrigger = {
    final MagicPermanent source, final MagicPlayer player, final MagicCard card ->
    return new MagicAtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return player.getId() == upkeepPlayer.getId();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicCard mappedCard = card.getOwner().map(game).getExile().getCard(card.getId());
            final MagicCard mappedSource = new MagicCard(
                source.getCardDefinition(), 
                source.getOwner().map(game), 
                source.getCard().getId()
            );
            
            game.addDelayedAction(new MagicRemoveTriggerAction(this));
            
            return mappedCard.isInExile() ?
                new MagicEvent(
                    mappedSource,
                    mappedCard.getOwner(),
                    mappedCard,
                    this,
                    "Return RN to the battlefield under PN's control. It gains haste."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefCard().isInExile()) {
                game.doAction(new MagicRemoveCardAction(
                    event.getRefCard(),
                    MagicLocationType.Exile
                ));
                game.doAction(new MagicPlayCardAction(
                    event.getRefCard(),
                    event.getPlayer(),
                    [MagicPlayMod.HASTE]
                ));
            }
        }
    };
}
[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PM may\$ exile SN. " + 
                    "If you do, return it to the battlefield under its owner's control at the beginning of your next upkeep. " + 
                    "It gains haste."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicRemoveFromPlayAction(
                    event.getPermanent(),
                    MagicLocationType.Exile
                ));
                game.doAction(new MagicAddTriggerAction(DelayedTrigger(
                    event.getPermanent(),
                    event.getPlayer(),
                    event.getPermanent().getCard()
                )));
            }
        }
    }
]
