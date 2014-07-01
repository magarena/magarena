[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+1 until end of turn for each basic land type among lands PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicPlayer player = event.getPlayer()
                final int amount = player.getDomain();
                game.logAppendMessage(player," ("+amount+")");
                game.doAction(new MagicChangeTurnPTAction(creature,amount,amount));
            });
        }
    }
]
