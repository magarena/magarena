[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int X = cardOnStack.getGame().getNrOfPermanents(MagicSubType.Zombie);
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(X, X),
                this,
                "Target creature\$ gets -X/-X until end of turn, where X is the number of Zombies on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int X = game.getNrOfPermanents(MagicSubType.Zombie);
                game.doAction(new ChangeTurnPTAction(it, -X, -X));
            });
        }
    }
]
