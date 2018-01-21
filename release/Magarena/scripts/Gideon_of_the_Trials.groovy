def preventDamageTrigger = {
    return new IfDamageWouldBeDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource() == permanent)
                damage.prevent();
            return MagicEvent.NONE;
        }
    }
}

def cantLoseTrigger = {
    final MagicPlayer player ->
    return new IfPlayerWouldLoseTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final LoseGameAction loseAction) {
            if (loseAction.getPlayer().getId() == player.getId() &&
                loseAction.getPlayer().getPermanents().any({ it.hasType(MagicType.Planeswalker) && it.hasSubType(MagicSubType.Gideon) })) {

                loseAction.setPlayer(MagicPlayer.NONE);
            }
            return MagicEvent.NONE;
        }
    }
}

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PERMANENT,
                this,
                "Until your next turn, prevent all damage target permanent\$ would deal."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            outerEvent.processTargetPermanent(outerGame, {
                outerGame.doAction(new AddTriggerAction(it, preventDamageTrigger));
            }

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
            game.doAction(new AddTriggerAction(it, cleanup));
        }
    }
    ,
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with " +
                "\"As long as PN controls a Gideon planeswalker, PN can't lose the game and PN's opponents can't win the game\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            outerGame.doAction(new AddTriggerAction(cantLoseTrigger(event.getPlayer())));
        }
    }
]

