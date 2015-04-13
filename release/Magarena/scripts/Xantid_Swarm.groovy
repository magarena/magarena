[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent creature) {
            return new MagicEvent(
                permanent,
                game.getDefendingPlayer(),
                this,
                "PN can't cast spells this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangePlayerAction(
                event.getPlayer(),
                MagicPlayerState.CantCastSpells
            ));
        }
    }
]
