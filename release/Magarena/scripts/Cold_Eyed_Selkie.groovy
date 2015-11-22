[
    new SelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        damage.getAmount(),
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    damage.getAmount(),
                    this,                            
                    "PN may\$ draw RN cards."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer(),event.getRefInt()));
            }       
        }
    }
]
