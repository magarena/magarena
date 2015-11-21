[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_ATTACKING_CREATURE,
                MagicPumpTargetPicker.create(),
                amount,
                this,
                "Target attacking creature\$ gets +RN/+0 until end of turn. "+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getRefInt();
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,amount,0));
                game.doAction(new AddTriggerAction(
                    AtUpkeepTrigger.YouDraw(
                        event.getSource(), 
                        event.getPlayer()
                    )
                ));
            });
        }
    }
]
