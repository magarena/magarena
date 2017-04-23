[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController() == cardOnStack.getGame().getTurnPlayer() && cardOnStack.getGame().isMainPhase() ? 4 : 2;
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                amount,
                this,
                "Target creature\$ gets +2/+2 until end of turn. If PN cast this spell during his or her main phase, that creature gets +4/+4 until end of turn instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getRefInt();
                game.logAppendValue(event.getPlayer(), amount);
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            })
        }
    }
]
