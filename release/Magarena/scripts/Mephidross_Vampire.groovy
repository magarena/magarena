[
    new MagicStatic(
         MagicLayer.Type,
         MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
         @Override                                                                                                                                                                                 
         public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {         
             flags.add(MagicSubType.Vampire);
         }                                                                                                                                          
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
            return source.getController() == permanent.getController() &&
				source.isPermanent() && damage.getTarget().isCreature() &&
				source.isCreature()?
                new MagicEvent(
                    source,
                    source.getController(),
                    this,
                    "Put +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
        }
    }
]
