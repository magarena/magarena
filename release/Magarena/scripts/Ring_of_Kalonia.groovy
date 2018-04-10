[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return equipped.isValid() && equipped.hasColor(MagicColor.Green) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on creature equipped by SN if it's green."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent equipped=permanent.getEquippedCreature();
            if (equipped.isValid() && equipped.hasColor(MagicColor.Green)) {
                game.doAction(new ChangeCountersAction(event.getPlayer(),equipped,MagicCounterType.PlusOne,1));
            }
        }
    }
]
