[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    game.getDefendingPlayer(),
                    this,
                    "PN can't cast spells this turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangePlayerStateAction(
                event.getPlayer(),
                MagicPlayerState.CantCastSpells
            ));
        }
    }
]
