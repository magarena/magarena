[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source),
                new MagicPayManaCostEvent(source,"{4}{C}")
            ];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                MagicCopyPermanentPicker.create(),
                this,
                "PN puts a token onto the battlefield that's a copy of target creature he or she controls.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
