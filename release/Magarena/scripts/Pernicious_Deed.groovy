[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [ 
                new MagicPayManaCostEvent(source,"{X}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                source,
                amount,
                this,
                "Destroy each artifact, creature and enchantment with converted mana cost RN or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source = event.getPermanent();
            final int amount = event.getRefInt();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(
                    source.getController(),
                    new MagicCMCPermanentFilter(
                        ARTIFACT_OR_CREATURE_OR_ENCHANTMENT,
                        Operator.LESS_THAN_OR_EQUAL,
                        amount
                    )
                );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
