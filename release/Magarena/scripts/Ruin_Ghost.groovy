[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Flicker"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{W}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                MagicTargetFilterFactory.LAND_YOU_CONTROL,
                MagicTargetHint.None,
                "target land to exile"
            );
            return new MagicEvent(
                source,
                targetChoice,
                MagicBounceTargetPicker.create(),
                this,
                "Exile target land you control\$, then " +
                "return that card to the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new MagicRemoveCardAction(it.getCard(),MagicLocationType.Exile));
                game.doAction(new MagicPlayCardAction(it.getCard(),event.getPlayer()));
            });
        }
    }
]
