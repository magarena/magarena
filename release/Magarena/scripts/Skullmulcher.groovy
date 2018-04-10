def drawCards = {
    final MagicGame game,final MagicPermanent permanent ->
    if (permanent.hasCounters()) {
        game.doAction(new DrawAction(
            permanent.getController(),
            permanent.getCounters(MagicCounterType.PlusOne)
        ));
    }
}

[
    new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                CREATURE_YOU_CONTROL.except(permanent),
                MagicTargetHint.None,
                "a creature other than "+permanent+" to sacrifice"
            );
            if (permanent.getController().getNrOfPermanents(MagicType.Creature)>1) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "PN may\$ sacrifice a creature\$ to SN."
                );
            }
            drawCards(game, permanent);
            return MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new SacrificeAction(it));
                    game.doAction(new ChangeCountersAction(event.getPlayer(),permanent,MagicCounterType.PlusOne,1));
                    final MagicEvent newEvent=executeTrigger(game,permanent,MagicPayedCost.NO_COST);
                    if (newEvent.isValid()) {
                        game.addEvent(newEvent);
                    }
                });
            } else {
                drawCards(game,permanent);
            }
        }
    }
]
