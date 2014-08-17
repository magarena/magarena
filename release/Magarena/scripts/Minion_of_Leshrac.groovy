[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice a creature other than SN. If you can't, SN deals 5 damage to you. If it deals damage to you this way, tap SN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicPlayer player = event.getPlayer();
            final MagicTargetChoice targetChoice = MagicTargetChoice.Other("a creature to sacrifice", permanent);
            if (targetChoice.hasOptions(game, player, permanent, false)) {
                game.addEvent(new MagicSacrificePermanentEvent(permanent,player,targetChoice));
            } else {
                final MagicDamage damage = new MagicDamage(permanent, player, 5);
                game.doAction(new MagicDealDamageAction(damage));
                if (damage.getDealtAmount() > 0) {
                    game.doAction(new MagicTapAction(permanent));
                }
            }
        }
    }
]
