def LAST_SOURCE = null;
[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
			
			if( permanent.isFriend(source) && source.isSpell() &&
                source.getCardDefinition().isSpell() && (!LAST_SOURCE || LAST_SOURCE != source)){
					LAST_SOURCE = source;
					return new MagicEvent(
						permanent,
						this,
						"PN puts two 1/1 red and white Soldier creature token with haste onto the battlefield."
					);
				}else{
                 return MagicEvent.NONE;
				}
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("RW Soldier"), 
                2
            ));
        }
    }
]
