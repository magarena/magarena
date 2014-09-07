[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (died.isCreature() && died.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(died.getPower()),
                    died,
                    this,
                    "RN deals damage equal to its power to target player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            event.processTargetPlayer(game, {
                final MagicDamage damage = new MagicDamage(permanent,it,permanent.getPower());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
