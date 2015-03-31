[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.isEnemy(permanent) && 
                    (spell.hasColor(MagicColor.White) || spell.hasColor(MagicColor.Blue)) &&
                    (spell.hasType(MagicType.Instant) || spell.hasType(MagicType.Sorcery)) 
                    )?
                new MagicEvent(
                    permanent,
                    spell.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
