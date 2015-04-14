[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy each nonland permanent with converted mana cost equal to the number of charge counters on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source=event.getPermanent();
            final int amount=source.getCounters(MagicCounterType.Charge);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(
                    source.getController(),
                    new MagicCMCPermanentFilter(
                        NONLAND_PERMANENT,
                        Operator.EQUAL,
                        amount
                    )
                );
            game.doAction(new DestroyAction(targets));
        }
    }
]
