[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+0 and gains trample until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getPermanent();
            game.doAction(new MagicChangeTurnPTAction(creature,3,0));
            game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
        }
    }
]
