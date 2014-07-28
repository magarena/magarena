[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            // FIXME: getTarget may be MagicTargetNone when not cast from hand
            return payedCost.getTarget().isPermanent() ?
                new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    payedCost.getTarget(),
                    this,
                    "SN deals damage equal to RN's power to target creature or player\$."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPermanent sacrificed = event.getRefPermanent();
                final MagicDamage damage=new MagicDamage(event.getSource(),it,sacrificed.getPower());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
