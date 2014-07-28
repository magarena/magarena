[
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                new MagicMayChoice(MagicTargetChoice.SACRIFICE_CREATURE),
                MagicSacrificeTargetPicker.create(),
                this,
                "PN may\$ sacrifice a creature\$. If you do, tap SN and put a +1/+1 counter on it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicSacrificeAction(it));
                    final MagicPermanent perm = event.getPermanent();
                    game.doAction(new MagicTapAction(perm, true)); //tap
                    game.doAction(new MagicChangeCountersAction(perm, MagicCounterType.PlusOne, 1, true));
                });
            }
        }
    }
]
