
def ToxinBury =	new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isCreature()) ?
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
            game.doAction(MagicChangeStateAction.Set(
                        event.getRefPermanent(),
                        MagicPermanentState.CannotBeRegenerated
                    ));
			game.doAction(new MagicDestroyAction(event.getRefPermanent()));
        }
    };  
[    
	new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(ToxinBury);
        }
    }
]
