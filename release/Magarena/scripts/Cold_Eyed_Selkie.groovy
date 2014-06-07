[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource().isPermanent() &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        damage.getAmount(),
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    damage.getAmount(),
                    this,                            
                    "PN may\$ draw "+damage.getAmount()+" cards."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDrawAction(event.getPlayer(),event.getRefInt()));
            }       
        }
    }
]
