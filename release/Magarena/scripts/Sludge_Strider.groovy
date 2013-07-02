def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetPlayer(game,new MagicPlayerAction() {
            public void doAction(final MagicPlayer player) {
                game.doAction(new MagicChangeLifeAction(player,-1));
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
            }
        });
    }
} as MagicEventAction

def event = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        new MagicMayChoice(
            new MagicPayManaCostChoice(MagicManaCost.create("{1}")),
            MagicTargetChoice.NEG_TARGET_PLAYER
        ),
        action,
        "You may\$ pay {1}\$. If you do, target player\$ loses 1 life and you gain 1 life."
    );
}

[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isArtifact() &&
                    otherPermanent.isFriend(permanent)) ?
                event(permanent) :  MagicEvent.NONE;
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            final MagicPermanent left = act.getPermanent();
            return (left != permanent &&
                    left.isArtifact() &&
                    left.isFriend(permanent)) ?
                event(permanent) : MagicEvent.NONE;
        }
    }
]
