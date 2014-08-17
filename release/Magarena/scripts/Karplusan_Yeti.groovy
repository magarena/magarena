[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target creature. " +
                "That creature deals damage equal to its power to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent SN = event.getPermanent();
                game.doAction(new MagicDealDamageAction(
                    new MagicDamage(
                        SN,
                        it,
                        SN.getPower()
                    )
                ));
                game.doAction(new MagicDealDamageAction(
                    new MagicDamage(
                        it,
                        SN,
                        it.getPower()
                    )
                ));
            });
        }
    }
]
