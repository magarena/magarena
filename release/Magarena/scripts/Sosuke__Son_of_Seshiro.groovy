[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().getController() == permanent.getController() &&
                    damage.getSource().isPermanent() &&
                    damage.getSource().hasSubType(MagicSubType.Warrior) &&
                    damage.isCombat() &&
                    damage.getTarget().isPermanent() &&
                    damage.getTarget().isCreature()) ?
                new MagicEvent(
                    damage.getSource(),
                    permanent.getController(),
                    damage.getTarget(),
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAddTriggerAction(
                event.getRefPermanent(),
                MagicAtEndOfCombatTrigger.Destroy
            ));
        }
    }
]
