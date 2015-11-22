def act = {
    final MagicGame game, final MagicEvent event ->
    final int amount = event.getPlayer().getOpponent().getNrOfAttackers() + event.getPlayer().getNrOfAttackers();
    game.doAction(new ChangeTurnPTAction(event.getPermanent(),amount,amount));
}

def evt = {
    final MagicPermanent permanent ->
    return new MagicEvent(
        permanent,
        act,
        "SN gets +1/+1 until end of turn for each attacking creature."
    );
}

[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return (permanent.getEquippedCreature() == blocker) ? evt(blocker) : MagicEvent.NONE;
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent.getEquippedCreature() == attacker) ? evt(attacker) : MagicEvent.NONE;
        }
    },
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Soldier)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent,
                    this,
                    "PN may\$ attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new AttachAction(
                    event.getPermanent(),
                    event.getRefPermanent()
                ));
            }
        }
    }
]
