[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = cardOnStack.getController().getDomain();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(amount,amount),
                this,
                "Target creature\$ gets -1/-1 until end of turn for each basic land type among lands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getPlayer().getDomain();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ChangeTurnPTAction(it,-amount,-amount));
            });
        }
    }
]
