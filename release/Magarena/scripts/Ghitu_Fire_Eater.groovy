[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent permanent=event.getPermanent();
                game.doAction(new MagicDealDamageAction(
                    permanent,
                    it,
                    permanent.getPower()
                ));
            });
        }
    }
]
