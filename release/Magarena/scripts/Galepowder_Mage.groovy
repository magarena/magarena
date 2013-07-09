[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature &&
                    game.getNrOfPermanents(MagicType.Creature) > 1) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE,
                    MagicExileTargetPicker.create(),
                    this,
                    "Exile target creature\$. Return that card to the " +
                    "battlefield under its owner's control at the beginning of the next end step."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicExileUntilEndOfTurnAction(creature));
                }
            });
        }
    }
]
