[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                damage.getTargetPlayer(),
                this,
                "RN sacrifices a creature. " + 
                "If RN can't, PN create a 3/3 black, red, and green Cat Dragon creature token with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent cost = new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getRefPlayer(),
                MagicTargetChoice.A_CREATURE_YOU_CONTROL
            );
            if (cost.isSatisfied()) {
                game.addEvent(cost);
            } else {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("3/3 black, red, and green Cat Dragon creature token with flying")
                ));
            }
        }
    }
]
