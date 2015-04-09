def choice = new MagicTargetChoice("target attacking creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount = source.getController().getNrOfPermanents(UNTAPPED_MOUNTAIN_YOU_CONTROL);
            return new MagicEvent(
                source,
                choice,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target attacking creature\$ equal to the number of untapped Mountains PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getNrOfPermanents(UNTAPPED_MOUNTAIN_YOU_CONTROL)
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
                game.logAppendMessage(event.getPlayer()," ("+amount+")");
            });
        }
    }
]
