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
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "another target creature to exile"
            );
            return new MagicEvent(
                source,
                targetChoice,
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
                game.doAction(new MagicPlayCardAction(it.getCard(),event.getPlayer()));
            });
        }
    }
]
