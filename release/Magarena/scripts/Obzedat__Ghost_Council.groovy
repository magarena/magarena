def DelayedTrigger = {
    final MagicCard staleCard, final MagicPlayer stalePlayer ->
    return new MagicAtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return stalePlayer.getId() == upkeepPlayer.getId();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            game.addDelayedAction(new MagicRemoveTriggerAction(this));
            
            final MagicCard mappedCard = staleCard.getOwner().map(game).getExile().getCard(staleCard.getId());
            
            return mappedCard.isInExile() ?
                new MagicEvent(
                    mappedCard,
                    this,
                    "Return SN to the battlefield under PN's control. It gains haste."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            if (card.isInExile()) {
                game.doAction(new RemoveCardAction(
                    card,
                    MagicLocationType.Exile
                ));
                game.doAction(new PlayCardAction(
                    card,
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
                    "PN may\$ exile SN. " + 
                    "If you do, return it to the battlefield under its owner's control at the beginning of your next upkeep. " + 
                    "It gains haste."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new RemoveFromPlayAction(
                    event.getPermanent(),
                    MagicLocationType.Exile
                ));
                game.doAction(new AddTriggerAction(DelayedTrigger(
                    event.getPermanent().getCard(),
                    event.getPlayer()
                )));
            }
        }
    }
]
