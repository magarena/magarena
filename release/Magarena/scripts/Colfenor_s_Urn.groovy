[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature() && 
                    otherPermanent.getToughness() >= 4) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    otherPermanent,
                    this,
                    "PN may\$ exile RN with SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicExileLinkAction(
                    event.getPermanent(),
                    event.getRefPermanent()
                ));
            }
        }
    },
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer endOfTurnPlayer) {
            return permanent.getExiledCards().size() >= 3 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN. If you do, return the cards exiled with it to the battlefield under their owner's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificeEvent(event.getPermanent());
            if (sac.isSatisfied()) {
                game.addEvent(sac);
                game.doAction(new MagicReturnLinkedExileAction(event.getPermanent(),MagicLocationType.Play));
            }
        }
    }
]
