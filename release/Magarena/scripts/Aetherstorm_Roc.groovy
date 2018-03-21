def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new ChangeCountersAction(event.getPlayer(), MagicCounterType.Energy, -2));
        game.doAction(new ChangeCountersAction(event.getPermanent(), MagicCounterType.PlusOne, 1));
        game.doAction(new TapAction(event.getRefPermanent()));
    }
}

[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Target a creature to tap?", TARGET_CREATURE_YOUR_OPPONENT_CONTROLS),
                this,
                "Targeted \$\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getCounters(MagicCounterType.PlusOne) >= 2) {
                if (event.isYes()) {
                    event.processTargetPermanent(game, {
                        game.addEvent(new MagicEvent(
                            event.getSource(),
                            new MagicMayChoice("Pay {E}{E}?"),
                            it,
                            action,
                            "PN may\$ pay {E}{E}. If PN does, put a +1/+1 counter on SN " +
                            "and untap up to one target creature defending player controls."
                        ));
                    });
                } else {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        new MagicMayChoice("Pay {E}{E}?"),
                        MagicPermanent.NONE,
                        action,
                        "PN may\$ pay {E}{E}. If PN does, put a +1/+1 counter on SN " +
                        "and untap up to one target creature defending player controls."
                    ));
                }
            }
        }
    }
]

