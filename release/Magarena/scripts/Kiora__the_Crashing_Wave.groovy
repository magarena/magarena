def preventDamageTrigger = new IfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (damage.getSource() == permanent || damage.getTarget() == permanent) {
            // Replacement effect. Generates no event or action.
            damage.prevent();
        }
        return MagicEvent.NONE;
    }
}

def AddLand = {
    final int pIdx ->
    return new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.incMaxLands();
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.getTurnPlayer().getIndex() == pIdx;
        }
    };
}

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice("target permanent an opponent controls"),
                this,
                "Until PN's next turn, prevent all damage that would be dealt to and dealt by target permanent PN's opponent controls\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerEvent.processTargetPermanent(outerGame, {
                outerGame.doAction(new AddTriggerAction(it, preventDamageTrigger));

                final AtUpkeepTrigger cleanup = new AtUpkeepTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                        if (upkeepPlayer.getId() == outerEvent.getPlayer().getId()) {
                            game.addDelayedAction(new RemoveTriggerAction(permanent, preventDamageTrigger));
                            game.addDelayedAction(new RemoveTriggerAction(permanent, this));
                        }
                        return MagicEvent.NONE;
                    }
                }
                outerGame.doAction(new AddTriggerAction(it, cleanup));
            });
        }
    },
    new MagicPlaneswalkerActivation(-1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card. PN may play an additional land this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer()));
            game.doAction(new AddStaticAction(AddLand(event.getPlayer().getIndex())));
        }
    }
]

