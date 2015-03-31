def choice = new MagicTargetChoice("a Human to sacrifice");

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN sacrifices a human. If you can't, tap SN and it deals 9 damage to you."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPlayer player=event.getPlayer();
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                permanent,
                player,
                choice
            );
            if (sac.isSatisfied()) {
                game.addEvent(sac);
            } else {
                game.doAction(new MagicTapAction(permanent));
                game.doAction(new MagicDealDamageAction(permanent, player, 9));
            }
        }
    }
]
