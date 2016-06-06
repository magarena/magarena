def T = new DamageIsDealtTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        return (damage.getSource().isCreaturePermanent() &&
                damage.isCombat() &&
                damage.getTarget() == permanent) ?
            new MagicEvent(
                permanent,
                damage.getSource(),
                this,
                "Destroy RN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new DestroyAction(event.getRefPermanent()));
    }
}

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until PN's next turn, whenever a creature deals combat damage to Vraska the Unseen, destroy that creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final DamageIsDealtTrigger trigger = T;
            outerGame.doAction(new AddTriggerAction(outerEvent.getPermanent(), trigger));
            // remove the trigger during player's next upkeep
            AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    if (upkeepPlayer.getId() == outerEvent.getPlayer().getId()) {
                        game.addDelayedAction(new RemoveTriggerAction(permanent, trigger));
                        game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                    }
                    return MagicEvent.NONE;
                }
            };
            outerGame.doAction(new AddTriggerAction(outerEvent.getPermanent(), cleanup));

        }
    }
]
