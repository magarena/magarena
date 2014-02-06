[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (spell.isEnemy(permanent) && 
                    spell.hasType(MagicType.Artifact)) ?
                new MagicEvent(
                    permanent,
                    MagicMayChoice("Put a 1/1 green Insect creature token onto the battlefield?")
                    this,
                    "PN may\$ put a 1/1 green Insect creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("1/1 green Insect creature token")));
            }
        }
    }
]
