[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(NEG_TARGET_CREATURE),
                    MagicExileTargetPicker.create(),
                    this,
                    "PN may\$ sacrifice SN. " +
                    "If you do, gain control of target creature\$."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                event.processTargetPermanent(game, {
                    game.doAction(new GainControlAction(event.getPlayer(),it));
                });
            }
        }
    }
]
