[
    new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER
                    ),
                    new MagicDamageTargetPicker(1),
                    this,
                    "PN may\$ have SN deal 1 damage to target creature or player\$"
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage(event.getPermanent(),target,1);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }
    }
]
