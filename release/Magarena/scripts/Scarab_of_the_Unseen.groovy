[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Bounce"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT_YOU_OWN,
                this,
                "Return all Auras attached to target permanent you own\$ to their owners' hands. "+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new RemoveAllFromPlayAction(permanent.getAuraPermanents(), MagicLocationType.OwnersHand));
                game.doAction(new AddTriggerAction(
                    AtUpkeepTrigger.YouDraw(event.getSource(), event.getPlayer())
                ));
            });
        }
    }
]
