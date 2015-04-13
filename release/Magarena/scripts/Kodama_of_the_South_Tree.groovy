[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                this,
                "Each other creature PN controls gets +1/+1 and gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOU_CONTROL
            .except(event.getPermanent())
            .filter(event.getPlayer()) each {
                game.doAction(new ChangeTurnPTAction(it, 1, 1));
                game.doAction(new MagicGainAbilityAction(it, MagicAbility.Trample));
            }
        }
    }
]
