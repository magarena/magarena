[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice a creature. If PN can't, he or she sacrifices SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,SACRIFICE_CREATURE);
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new SacrificeAction(permanent));
            }
        }
    }
]
