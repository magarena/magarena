[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{R}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to RN's power to target player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent sacrificed=event.getRefPermanent();
                game.doAction(new DealDamageAction(event.getSource(),it,sacrificed.getPower()));
            });
        }
    }
]
