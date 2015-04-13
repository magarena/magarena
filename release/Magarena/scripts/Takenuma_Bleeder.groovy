def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.getPlayer().controlsPermanent(MagicSubType.Demon) == false) {
        game.doAction(new ChangeLifeAction(event.getPlayer(),-1));
    }
}

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        action,
        "If PN controls no Demons, lose 1 life."
    );
}

[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                event(permanent):
                MagicEvent.NONE;
        }
    },    
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return permanent.getController().controlsPermanent(MagicSubType.Demon) == false ?
                event(permanent):
                MagicEvent.NONE;
        }
    }
]
