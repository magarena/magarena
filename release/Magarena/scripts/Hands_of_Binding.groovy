[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature\$ an opponent controls. " +
                "That creature doesn't untap during its controller's next untap step. " +
                "Cipher."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicTapAction(creature,true));
                    game.doAction(MagicChangeStateAction.Set(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                    game.doAction(new MagicCipherAction(
                        event.getCardOnStack(), 
                        event.getPlayer()
                    ));
                }
            });
        }
    }
]
