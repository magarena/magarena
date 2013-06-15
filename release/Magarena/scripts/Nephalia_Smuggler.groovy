[
    new MagicPermanentActivation( 
        new MagicActivationHints(MagicTiming.Main),
        "Card"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{3}{U}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicTargetFilter<MagicPermanent> targetFilter = new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,source);
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                    targetFilter,true,MagicTargetHint.None,"another creature to exile");
            return new MagicEvent(
                source,
                targetChoice,
                MagicBounceTargetPicker.getInstance(),
                this,
                "Exile another target creature you control\$, then " +
                "return that card to the battlefield under your control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
                    game.doAction(new MagicRemoveCardAction(creature.getCard(),MagicLocationType.Exile));
                    game.doAction(new MagicPlayCardAction(creature.getCard(),event.getPlayer(),MagicPlayCardAction.NONE));
                }
            });
        }
    }
]
