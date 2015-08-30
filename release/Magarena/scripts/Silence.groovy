[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Your opponents can't cast spells this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangePlayerStateAction(
                event.getPlayer().getOpponent(),
                MagicPlayerState.CantCastSpells
            ));
        }
    }
]
