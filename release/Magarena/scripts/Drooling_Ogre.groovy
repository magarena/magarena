[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.hasType(MagicType.Artifact) ? new MagicEvent(
                permanent,
                cardOnStack.getController(),
                this,
                "PN gains control of SN."
            ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainControlAction(event.getPlayer(),event.getPermanent()));
        }
    }
]
