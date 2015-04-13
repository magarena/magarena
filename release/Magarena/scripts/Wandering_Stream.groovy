[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 2 life for each basic land type among lands he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer()
            final int amount = player.getDomain();
            game.logAppendMessage(player," ("+amount+")");
            game.doAction(new ChangeLifeAction(player,amount*2));
        }
    }
]
