[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            return (damage.isSource(permanent) && damage.isCombat() && target.isPlayer()) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "RN reveals his or her hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRevealAction(event.getRefPlayer().getHand()));
        }
    }
]
