[
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            final int X = game.getNrOfPermanents(MagicSubType.Goblin);
            return new MagicEvent(
                card,
                new MagicMayChoice(NEG_TARGET_CREATURE),
                new MagicDamageTargetPicker(X),
                this,
                "PN may\$ have SN deal X damage to target creature\$, "+
                "where X is the number of Goblins on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final int X = game.getNrOfPermanents(MagicSubType.Goblin);
                    game.doAction(new DealDamageAction(event.getSource(),it,X));
                });
            }
        }
    }
]
