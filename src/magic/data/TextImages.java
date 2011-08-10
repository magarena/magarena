package magic.data;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TextImages {
	
	private static final Map<String,ImageIcon> TEXT_ICONS=new HashMap<String,ImageIcon>();
	
	static {
		
		TEXT_ICONS.put("{0}",IconImages.COST_ZERO);
		TEXT_ICONS.put("{1}",IconImages.COST_ONE);
		TEXT_ICONS.put("{2}",IconImages.COST_TWO);
		TEXT_ICONS.put("{3}",IconImages.COST_THREE);
		TEXT_ICONS.put("{4}",IconImages.COST_FOUR);
		TEXT_ICONS.put("{5}",IconImages.COST_FIVE);
		TEXT_ICONS.put("{6}",IconImages.COST_SIX);
		TEXT_ICONS.put("{7}",IconImages.COST_SEVEN);
		TEXT_ICONS.put("{8}",IconImages.COST_EIGHT);
		TEXT_ICONS.put("{9}",IconImages.COST_NINE);
		TEXT_ICONS.put("{X}",IconImages.COST_X);
		
		TEXT_ICONS.put("{B}",IconImages.COST_BLACK);
		TEXT_ICONS.put("{U}",IconImages.COST_BLUE);
		TEXT_ICONS.put("{G}",IconImages.COST_GREEN);
		TEXT_ICONS.put("{R}",IconImages.COST_RED);
		TEXT_ICONS.put("{W}",IconImages.COST_WHITE);
		TEXT_ICONS.put("{B/G}",IconImages.COST_BLACK_GREEN);
		TEXT_ICONS.put("{B/R}",IconImages.COST_BLACK_RED);
		TEXT_ICONS.put("{U/B}",IconImages.COST_BLUE_BLACK);
		TEXT_ICONS.put("{U/R}",IconImages.COST_BLUE_RED);
		TEXT_ICONS.put("{G/U}",IconImages.COST_GREEN_BLUE);
		TEXT_ICONS.put("{G/W}",IconImages.COST_GREEN_WHITE);
		TEXT_ICONS.put("{R/G}",IconImages.COST_RED_GREEN);
		TEXT_ICONS.put("{R/W}",IconImages.COST_RED_WHITE);
		TEXT_ICONS.put("{W/B}",IconImages.COST_WHITE_BLACK);
		TEXT_ICONS.put("{W/U}",IconImages.COST_WHITE_BLUE);

		TEXT_ICONS.put("{f}",IconImages.FORWARD2);
		TEXT_ICONS.put("{O}",IconImages.TARGET);
		TEXT_ICONS.put("{T}",IconImages.TAPPED);
		TEXT_ICONS.put("{S}",IconImages.CANNOTTAP);
		TEXT_ICONS.put("{r}",IconImages.REGENERATED);
		TEXT_ICONS.put("{s}",IconImages.SLEEP);
		TEXT_ICONS.put("{c}",IconImages.COMBAT);
		TEXT_ICONS.put("{b}",IconImages.BLOCKED);
		TEXT_ICONS.put("{D}",IconImages.DAMAGE);
		TEXT_ICONS.put("{P}",IconImages.PREVENT);
		TEXT_ICONS.put("{t}",IconImages.TOKEN);
		TEXT_ICONS.put("{+}",IconImages.PLUS);
		TEXT_ICONS.put("{-}",IconImages.MINUS);
		TEXT_ICONS.put("{C}",IconImages.CHARGE);
		TEXT_ICONS.put("{F}",IconImages.FEATHER);
		TEXT_ICONS.put("{L}",IconImages.LOSE);
	}
	
	public static ImageIcon getIcon(final String text) {
		
		return TEXT_ICONS.get(text);
	}
}