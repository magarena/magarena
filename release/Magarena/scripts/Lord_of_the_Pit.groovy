[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice a creature other than SN. If PN can't, SN deals 7 damage to him or her."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL)
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new DealDamageAction(permanent,player,7));
            }
        }
    }
]
