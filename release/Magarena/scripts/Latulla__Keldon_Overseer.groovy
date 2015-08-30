[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                    new MagicPayManaCostEvent(source,"{X}{R}"),
                    new MagicTapEvent(source),
                    new MagicDiscardEvent(source, 2),
                ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                payedCost.getX(), 
                this,
                "SN deals RN damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(
                    event.getSource(),
                    it,
                    event.getRefInt()
                ));
            });
        }
    }
]
