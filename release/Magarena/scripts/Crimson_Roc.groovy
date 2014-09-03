[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return (permanent==blocker &&
                    blocked.isValid() &&
                    blocked.hasAbility(MagicAbility.Flying) == false) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+0 and first strike until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,0));
		game.doAction(new MagicGainAbilityAction(
                event.getPermanent(), 
                MagicAbility.FirstStrike
            ));
        }
    }
]
