def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.getPlayer().controlsPermanent(MagicSubType.Demon) == false) {
        game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
    }
}

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "If PN controls no Demons, lose 3 life."
    );
}

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedcost) {
            return permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                event(permanent):
                MagicEvent.NONE;
        }
    },    
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final RemoveFromPlayAction act) {
            return permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                event(permanent):
                MagicEvent.NONE;
        }
    }
]
