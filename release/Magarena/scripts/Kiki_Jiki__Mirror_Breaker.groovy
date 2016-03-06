[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Copy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_NON_LEGENDARY_CREATURE_YOU_CONTROL,
                MagicCopyPermanentPicker.create(),
                this,
                "Put a token that's a copy of target nonlegendary creature\$ you control onto the battlefield. "+
                "That token has haste. Sacrifice it at the beginning of the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    it,
                    [MagicPlayMod.HASTE, MagicPlayMod.SACRIFICE_AT_END_OF_TURN]
                ));
            });
        }
    }
]
