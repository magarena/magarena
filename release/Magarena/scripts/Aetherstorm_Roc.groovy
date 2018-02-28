def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1));
    if (event.isYes()) {
        event.processTargetPermanent(game, {
            game.doAction(new TapAction(it));
        });
    }
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return (permanent.getController().getCounters(MagicCounterType.Energy) >= 2) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Pay {E}{E}?"),
                    this,
                    "PN may\$ pay {E}{E}."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicPayEnergyEvent(event.getSource(), 2));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice("Tap a creature?", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                    action,
                    "If PN does, put a +1/+1 counter on SN " +
                    "and tap up to one target creature defending player controls\$\$."
                ));
            }
        }
    }
]

