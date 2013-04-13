[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    player.getOpponent(),
                    this,
                    "PN can't cast spells this turn."
                ):
                MagicEvent.NONE;           
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangePlayerStateAction(
                event.getPlayer(),
                MagicPlayerState.CantCastSpells
            ));
        }
    }
]
