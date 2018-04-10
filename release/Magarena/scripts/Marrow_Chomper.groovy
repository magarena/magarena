[
    new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice=new MagicTargetChoice(
                CREATURE_YOU_CONTROL.except(permanent),
                MagicTargetHint.None,
                "a creature other than "+permanent+" to sacrifice"
            );
            return permanent.getController().getNrOfPermanents(MagicType.Creature) > 1 ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "PN may\$ sacrifice a creature\$ to SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent=event.getPermanent();
                event.processTargetPermanent(game, {
                    game.doAction(new SacrificeAction(it));
                    game.doAction(new ChangeCountersAction(event.getPlayer(),permanent,MagicCounterType.PlusOne,2));
                    game.doAction(new ChangeLifeAction(event.getPlayer(),2));
                    final MagicEvent newEvent=executeTrigger(game,permanent,MagicPayedCost.NO_COST);
                    if (newEvent.isValid()) {
                        game.addEvent(newEvent);
                    }
                });
            }
        }
    }
]
