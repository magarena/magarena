[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeLifeAction(player, 2 * permanent.getKicker()));
            }
            return MagicEvent.NONE;
        }
    }
]
