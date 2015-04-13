[    
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().hasSubType(MagicSubType.Sliver) &&
                    damage.isCombat() &&
                    damage.isTargetCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "Destroy RN. It can't be regenerated."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(ChangeStateAction.Set(
                event.getRefPermanent(),
                MagicPermanentState.CannotBeRegenerated
            ));
            game.doAction(new MagicDestroyAction(event.getRefPermanent()));
        }
    }
]
