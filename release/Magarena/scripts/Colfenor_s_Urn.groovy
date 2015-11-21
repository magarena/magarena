[
    new WhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (died.isOwner(permanent.getController()) &&
                    died.hasType(MagicType.Creature) &&
                    died.getToughness() >= 4) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    died.getCard(),
                    this,
                    "PN may\$ exile RN with SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ExileLinkAction(
                    event.getPermanent(),
                    event.getRefCard(),
                    MagicLocationType.Graveyard
                ));
            }
        }
    },
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer endOfTurnPlayer) {
            return permanent.getExiledCards().size() >= 3 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN. If PN does, return the cards exiled with it to the battlefield under their owner's control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificeEvent(event.getPermanent());
            if (sac.isSatisfied()) {
                game.addEvent(sac);
                game.doAction(new ReturnLinkedExileAction(event.getPermanent(),MagicLocationType.Play));
            }
        }
    }
]
