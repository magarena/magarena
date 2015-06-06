def PreventCreatureDamage = new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().hasType(MagicType.Creature)) {
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all damage that would be dealt by creatures this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddTurnTriggerAction(PreventCreatureDamage));
        }
    }
]

