def drawCards = {
    final MagicGame game,final MagicPermanent permanent ->
    if (permanent.hasCounters()) {
        game.doAction(new MagicDrawAction(
            permanent.getController(),
            permanent.getCounters(
            MagicCounterType.PlusOne)
        ));
    }
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final MagicTargetFilter<MagicPermanent> targetFilter=new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                    targetFilter,false,MagicTargetHint.None,"a creature other than "+permanent+" to sacrifice");
            if (player.getNrOfPermanentsWithType(MagicType.Creature)>1) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "You may\$ sacrifice a creature\$ to SN."
                );
            }
            drawCards(game, permanent);
            return MagicEvent.NONE;
        }

        @Override
        public boolean usesStack() {
            return false;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicSacrificeAction(creature));
                        game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,1,true));
                        final MagicEvent newEvent=executeTrigger(game,permanent,permanent.getController());
                        if (newEvent.isValid()) {
                            game.addEvent(newEvent);
                        }
                    }
                });
            } else {
                drawCards(game,permanent);
            }
        }
    }
]
