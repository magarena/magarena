def EFFECT = MagicRuleEventAction.create("Transform SN."); 

def ABILITY2 = MagicRuleEventAction.create("Put a 2/2 green Wolf creature token onto the battlefield.");

[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Loyalty) <= 2;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(
                 EFFECT.getEvent(source)
            ));               
        }
    },
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature. That creature deals damage equal to its power to him."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,3));
                game.doAction(new DealDamageAction(it,event.getPermanent(),it.getPower()));
            });
        }
    },
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
             return ABILITY2.getEvent(source);
        }
    }
]
