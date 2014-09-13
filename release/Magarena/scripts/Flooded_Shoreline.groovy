def choice = new MagicTargetChoice("an Island you control");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Bounce"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
            new MagicPayManaCostEvent(source,"{U}{U}"),
            new MagicBounceChosenPermanentEvent(source,choice),
            new MagicBounceChosenPermanentEvent(source,choice)
            ];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                this,
                "Return target creature\$ to its owner's hand."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));            });
        }
    }
]
