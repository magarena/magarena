
[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target = damage.getTarget();
            if (damage.getSource() == permanent &&
                   damage.isCombat() &&
                   target.isPlayer()) {                
                return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    (MagicPlayer) damage.getTarget(),
                    this,
                    "RN reveals his or her hand."
                )

            } else return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getRefPlayer();
            final List<MagicCard> showList = player.getHand();
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(showList),
                event.getRefPlayer(),
                MagicEvent.NO_ACTION,
                "RN revealed his or her hand."
            ));
        }
    }
]
