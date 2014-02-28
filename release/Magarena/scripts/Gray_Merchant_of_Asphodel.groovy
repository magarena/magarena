[ // enters effect Target opponent loses 2 life and PN gains 2 life.            
    new MagicWhenComesIntoPlayTrigger() {                                       
        @Override                                                               
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayed
            return new MagicEvent(                                              
                permanent,                                                      
                this,                                                           
                "Target opponent loses life and PN gains life equal to PN's devotion to black. ("+permanent.ge
            );                                                                  
        }                                                                       
        @Override                                                               
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Black); 
            final MagicPlayer player=event.getPlayer();                         
            game.doAction(new MagicChangeLifeAction(player.getOpponent(),-1*amount));
            game.doAction(new MagicChangeLifeAction(player, amount));           
        }                                                                       
    }                                                                           
]     