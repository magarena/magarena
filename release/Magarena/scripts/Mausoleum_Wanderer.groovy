def choice = MagicTargetChoice.Negative("target instant or sorcery spell");
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "Counter target instant or sorcery spell\$ unless its controller pays {X}, where X is SN's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = event.getPermanent().getPower();
                game.logAppendX(event.getPlayer(), amount);
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(), it, MagicManaCost.create(amount)));
            });
        }
    }
]
