[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    new MagicMayChoice(MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER),
                    new MagicDamageTargetPicker(otherPermanent.getPower()),
                    otherPermanent,
                    this,
                    "PN may\$ have RN deal damage equal to its power to target creature or player\$."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getRefPermanent();
            if (event.isYes()) {
                event.processTarget(game, {
                    final MagicDamage damage = new MagicDamage(permanent,it,permanent.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                });
            }
        }
    }
]
