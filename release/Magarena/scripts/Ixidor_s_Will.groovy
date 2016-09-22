[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {2} for each Wizard on the battlefield. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final int amount = game.getNrOfPermanents(MagicSubType.Wizard) * 2;
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),it,MagicManaCost.create(amount)));
            });
        }
    }
]
