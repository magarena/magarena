def choice = new MagicTargetChoice("a non-Ooze creature to sacrifice");

[
    new MagicPermanentActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}{G}"),
                new MagicSacrificePermanentEvent(source,choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN puts an X/X green Ooze creature token onto the battlefield, where X is the sacrificed creature's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int x = event.getRefPermanent().getPower();
            game.logAppendValue(event.getPlayer(), x);
            game.doAction(new PlayTokenAction(event.getPlayer(), MagicCardDefinition.create(
                CardDefinitions.getToken("green Ooze creature token"),
                {
                    it.setPowerToughness(x, x);
                    it.setValue(x);
                }
            )));
        }
    }
]
