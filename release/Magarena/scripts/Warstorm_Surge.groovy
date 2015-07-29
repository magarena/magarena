[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() && otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(otherPermanent.getPower()),
                    otherPermanent,
                    this,
                    "RN deals damage equal to its power to target creature or player\$. ("+otherPermanent.getPower()+")"
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            event.processTarget(game, {
                game.doAction(new DealDamageAction(permanent,it,permanent.getPower()));
           });
        }
    }
]
