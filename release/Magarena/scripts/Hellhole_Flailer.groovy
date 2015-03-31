[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicPermanent permanent=source;
            return [
                new MagicPayManaCostEvent(source,"{2}{B}{R}"),
                new MagicSacrificeEvent(permanent)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent source=event.getPermanent();
                game.doAction(new MagicDealDamageAction(source,it,source.getPower()));
            });
        }
    }
]
