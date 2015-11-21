[
    new WhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return died != permanent && died.isCreature() && died.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(died.getPower()),
                    died,
                    this,
                    "RN deals damage equal to its power to target player\$. ("+died.getPower()+")"
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPermanent permanent = event.getRefPermanent();
                game.doAction(new DealDamageAction(permanent,it,permanent.getPower()));
            });
        }
    }
]
