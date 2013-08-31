[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.SACRIFICE_CREATURE,
                MagicSacrificeTargetPicker.create(),
                this,
                "Sacrifice a creature. PN gains life " +
                "equal to sacrificed creature's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final int toughness=creature.getToughness();
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),toughness));
                }
            });
        }
    }
]
