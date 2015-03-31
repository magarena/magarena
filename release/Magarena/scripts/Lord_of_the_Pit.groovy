[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice a creature other than SN. If you can't, SN deals 7 damage to you."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicTargetChoice targetChoice = MagicTargetChoice.Other("a creature to sacrifice", permanent);
            final MagicEvent sac = new MagicSacrificePermanentEvent(permanent,player,targetChoice)
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicDealDamageAction(permanent,player,7));
            }
        }
    }
]
