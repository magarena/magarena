[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicTapTargetPicker(true,false),
                this,
                "Tap target creature\$. " +
                "It doesn't untap during its controller's next untap step. " +
                "Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicTapAction(creature,true));
                    game.doAction(new MagicChangeStateAction(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext,
                        true
                    ));
                    game.doAction(new MagicDrawAction(event.getPlayer()));
                }
            });
        }
    }
]
