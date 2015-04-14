[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Flicker"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                Other("target creature you control", source),
                MagicBounceTargetPicker.create(),
                this,
                "Exile another target creature you control\$, then " +
                "return that card to the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new MagicRemoveCardAction(it.getCard(),MagicLocationType.Exile));
                game.doAction(new PlayCardAction(it.getCard(),event.getPlayer()));
            });
        }
    }
]
