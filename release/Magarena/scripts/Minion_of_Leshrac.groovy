[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice a creature other than SN. If PN can't, SN deals 5 damage to him or her. If it deals damage to PN this way, tap SN."
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
                final MagicDamage damage = new MagicDamage(permanent, player, 5);
                game.doAction(new DealDamageAction(damage));
                if (damage.getDealtAmount() > 0) {
                    game.doAction(new TapAction(permanent));
                }
            }
        }
    }
]
