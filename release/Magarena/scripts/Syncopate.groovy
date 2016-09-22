[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                payedCost.getX(),
                this,
                "Counter target spell\$ unless its controller pays {RN}. " +
                "If that spell is countered this way, exile it instead of putting it into its owner's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicCounterUnlessEvent(
                    event.getSource(),
                    it,
                    MagicManaCost.create(event.getRefInt()),
                    MagicLocationType.Exile
                ));
            });
        }
    }
]
