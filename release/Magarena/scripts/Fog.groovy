[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt this turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.PreventAllCombatDamage));
            game.doAction(new MagicChangePlayerStateAction(player.getOpponent(),MagicPlayerState.PreventAllCombatDamage));
        }
    }
]
