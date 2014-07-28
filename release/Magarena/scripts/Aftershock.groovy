[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_CREATURE_OR_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact, creature, or land\$ SN deals 3 damage to PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),3);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
