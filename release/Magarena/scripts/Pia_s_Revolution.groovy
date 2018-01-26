def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(), event.getPlayer(), 3));
    } else {
        game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
    }
}

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return !died.isToken() && died.hasType(MagicType.Artifact) && died.isOwner(permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    died.getCard(),
                    this,
                    "Return RN to PN's hand unless target opponent\$ has SN deal 3 damage to him or her."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Have ${event.getSource()} deal 3 damage to you?"),
                    event.getRefCard(),
                    action,
                    "\$"
                ));
            });
        }
    }
]

