package magic.model;

import magic.data.IconImages;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicManaCostCondition;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicManaCost {

	private static final Map<String,MagicManaCost> COSTS_MAP=new HashMap<String,MagicManaCost>();
	private static final Map<String,MagicCondition> CONDS_MAP=new HashMap<String,MagicCondition>();

	private static final Pattern PATTERN=Pattern.compile("(\\{[A-Z\\d/]+\\})(?:\\{.+)?");

	private static final int SINGLE_PENALTY[]={0,1,1,3,6,9};
	private static final int DOUBLE_PENALTY[]={0,0,1,2,4,6};
	
	private static final ImageIcon COLORLESS_ICONS[]={
		IconImages.COST_ZERO,
		IconImages.COST_ONE,
		IconImages.COST_TWO,
		IconImages.COST_THREE,
		IconImages.COST_FOUR,
		IconImages.COST_FIVE,
		IconImages.COST_SIX,
		IconImages.COST_SEVEN,
		IconImages.COST_EIGHT,
		IconImages.COST_NINE
	};
	
	public static final MagicManaCost X=MagicManaCost.createCost("{X}");
	public static final MagicManaCost ZERO=MagicManaCost.createCost("{0}");
	public static final MagicManaCost ONE=MagicManaCost.createCost("{1}");
	public static final MagicManaCost BLACK=MagicManaCost.createCost("{B}");
	public static final MagicManaCost BLUE=MagicManaCost.createCost("{U}");
	public static final MagicManaCost GREEN=MagicManaCost.createCost("{G}");
	public static final MagicManaCost RED=MagicManaCost.createCost("{R}");
	public static final MagicManaCost WHITE=MagicManaCost.createCost("{W}");

	public static final MagicManaCost TWO=MagicManaCost.createCost("{2}");
	public static final MagicManaCost THREE=MagicManaCost.createCost("{3}");
	public static final MagicManaCost FOUR=MagicManaCost.createCost("{4}");
	public static final MagicManaCost FIVE=MagicManaCost.createCost("{5}");
	public static final MagicManaCost EIGHT=MagicManaCost.createCost("{8}");
	public static final MagicManaCost ONE_BLUE=MagicManaCost.createCost("{1}{U}");
	public static final MagicManaCost TWO_BLUE=MagicManaCost.createCost("{2}{U}");
	public static final MagicManaCost THREE_BLUE=MagicManaCost.createCost("{3}{U}");
	public static final MagicManaCost BLUE_BLUE=MagicManaCost.createCost("{U}{U}");
	public static final MagicManaCost TWO_BLUE_BLUE=MagicManaCost.createCost("{2}{U}{U}");
	public static final MagicManaCost ONE_GREEN=MagicManaCost.createCost("{1}{G}");
	public static final MagicManaCost TWO_GREEN=MagicManaCost.createCost("{2}{G}");
	public static final MagicManaCost THREE_GREEN=MagicManaCost.createCost("{3}{G}");
	public static final MagicManaCost TWO_GREEN_GREEN=MagicManaCost.createCost("{2}{G}{G}");
	public static final MagicManaCost TWO_GREEN_GREEN_GREEN=MagicManaCost.createCost("{2}{G}{G}{G}");
	public static final MagicManaCost X_RED=MagicManaCost.createCost("{X}{R}");
	public static final MagicManaCost ONE_RED=MagicManaCost.createCost("{1}{R}");
	public static final MagicManaCost TWO_RED=MagicManaCost.createCost("{2}{R}");
	public static final MagicManaCost THREE_RED=MagicManaCost.createCost("{3}{R}");
	public static final MagicManaCost FIVE_RED_RED=MagicManaCost.createCost("{5}{R}{R}");
	public static final MagicManaCost ONE_WHITE=MagicManaCost.createCost("{1}{W}");
	public static final MagicManaCost TWO_WHITE=MagicManaCost.createCost("{2}{W}");
	public static final MagicManaCost SEVEN_WHITE=MagicManaCost.createCost("{7}{W}");
	public static final MagicManaCost WHITE_WHITE=MagicManaCost.createCost("{W}{W}");
	public static final MagicManaCost ONE_WHITE_WHITE=MagicManaCost.createCost("{1}{W}{W}");
	public static final MagicManaCost X_WHITE=MagicManaCost.createCost("{X}{W}");
	public static final MagicManaCost BLACK_GREEN=MagicManaCost.createCost("{B}{G}");
	public static final MagicManaCost GREEN_WHITE=MagicManaCost.createCost("{G}{W}");
	public static final MagicManaCost BLACK_BLACK=MagicManaCost.createCost("{B}{B}");
	public static final MagicManaCost X_BLACK_BLACK=MagicManaCost.createCost("{X}{B}{B}");
	public static final MagicManaCost ONE_BLACK_BLACK=MagicManaCost.createCost("{1}{B}{B}");
	public static final MagicManaCost ONE_BLACK=MagicManaCost.createCost("{1}{B}");
	public static final MagicManaCost TWO_BLACK=MagicManaCost.createCost("{2}{B}");
	public static final MagicManaCost THREE_BLACK=MagicManaCost.createCost("{3}{B}");
	public static final MagicManaCost THREE_BLACK_BLACK=MagicManaCost.createCost("{3}{B}{B}");
	public static final MagicManaCost ONE_WHITE_BLACK=MagicManaCost.createCost("{1}{W}{B}");
	public static final MagicManaCost RED_OR_WHITE=MagicManaCost.createCost("{R/W}");
	public static final MagicManaCost BLACK_OR_RED=MagicManaCost.createCost("{B/R}");
	public static final MagicManaCost ONE_WHITE_OR_BLUE=MagicManaCost.createCost("{1}{W/U}");
	public static final MagicManaCost THREE_WHITE_BLUE=MagicManaCost.createCost("{3}{W}{U}");
	public static final MagicManaCost TWO_WHITE_WHITE_BLUE_BLUE=MagicManaCost.createCost("{2}{W}{W}{U}{U}");
	public static final MagicManaCost ONE_RED_GREEN=MagicManaCost.createCost("{1}{R}{G}");
	public static final MagicManaCost TWO_RED_GREEN=MagicManaCost.createCost("{2}{R}{G}");
	public static final MagicManaCost ONE_RED_RED_GREEN_GREEN=MagicManaCost.createCost("{1}{R}{R}{G}{G}");
	public static final MagicManaCost ONE_BLUE_BLACK=MagicManaCost.createCost("{1}{U}{B}");
	public static final MagicManaCost BLUE_BLUE_BLACK_BLACK=MagicManaCost.createCost("{U}{U}{B}{B}");
	public static final MagicManaCost ONE_GREEN_WHITE=MagicManaCost.createCost("{1}{G}{W}");
	public static final MagicManaCost GREEN_GREEN_WHITE_WHITE=MagicManaCost.createCost("{G}{G}{W}{W}");
	public static final MagicManaCost WHITE_OR_BLACK=MagicManaCost.createCost("{W/B}");
	public static final MagicManaCost WHITE_OR_BLACK_WHITE_OR_BLACK=MagicManaCost.createCost("{W/B}{W/B}");
		
	private final MagicCostManaType types[];
	private final int amounts[];
	private int typeCount;
	private int converted;
	private boolean hasX;
	private String costText;
	private List<ImageIcon> icons;
    private final MagicBuilderManaCost builderCost;	
	
	private MagicManaCost(final String costText) {
		this.costText=costText;
		types=new MagicCostManaType[MagicCostManaType.NR_OF_TYPES];
		amounts=new int[MagicCostManaType.NR_OF_TYPES];
		hasX=false;
		typeCount=0;
		converted=0;

		String text=costText;
		boolean ok=true;
		while (ok&&text.length()>0) {

			ok=false;
			final Matcher matcher=PATTERN.matcher(text);
			if (matcher.matches()) {
				final String type=matcher.group(1);
				if (addType(type)) {
					text=text.substring(type.length());
					ok=true;
				}
			}
		}
		if (!ok) {
			System.err.println("Invalid cost : "+costText);
		}

		buildIcons();

		builderCost=new MagicBuilderManaCost();
		addTo(builderCost);
	}
	
	private void addType(final MagicCostManaType type,final int amount) {

		converted+=amount;
		
		for (int index=0;index<typeCount;index++) {
			
			if (types[index]==type) {
				amounts[index]+=amount;
				return;
			}
		}
		
		types[typeCount]=type;
		amounts[typeCount]=amount;
		typeCount++;
	}
	
	private boolean addType(final String typeText) {

		final char symbol=typeText.charAt(1);
		if (symbol=='X') {
			hasX=true;
			return true;
		} else if (symbol>='0'&&symbol<='9') {
			addType(MagicCostManaType.Colorless,symbol-'0');
			return true;
		} else {
			for (final MagicCostManaType type : MagicCostManaType.values()) {
				
				if (type.getText().equals(typeText)) {
					addType(type,1);
					return true;
				}
			}
			return false;
		}
	}
			
	private void setText(final String aCostText) {
		this.costText=aCostText;
	}
	
	public String getText() {
		return costText;
	}
	
    @Override
    public String toString() {
		return costText;
	}
	
	public boolean hasX() {
		return hasX;
	}
				
	public MagicCondition getCondition() {
		MagicCondition cond = CONDS_MAP.get(costText);
        if (cond == null) {
            cond = new MagicManaCostCondition(this);
            CONDS_MAP.put(costText, cond);
        }
        return cond;
	}
	
	public List<MagicCostManaType> getCostManaTypes(final int x) {
		final List<MagicCostManaType> costManaTypes=new ArrayList<MagicCostManaType>();
		int colorless=x;

		for (int index=0;index<typeCount;index++) {
			
			final MagicCostManaType costManaType=types[index];
			int amount=amounts[index];
			if (costManaType==MagicCostManaType.Colorless) {
				colorless+=amount;
			} else {
				for (;amount>0;amount--) {
					
					costManaTypes.add(costManaType);
				}
			}
		}
		
		for (;colorless>0;colorless--) {
			
			costManaTypes.add(MagicCostManaType.Colorless);
		}
		
		return costManaTypes;
	}

	private void buildIcons() {
		
		icons=new ArrayList<ImageIcon>();
		if (hasX) {
			icons.add(IconImages.COST_X);
		}
		for (int i=0;i<typeCount;i++) {
		
			int amount=amounts[i];
			if (types[i]==MagicCostManaType.Colorless) {
				if (amount>9) {
					icons.add(COLORLESS_ICONS[9]);
					amount-=9;
				}
				icons.add(COLORLESS_ICONS[amount]);
			} else {
				final ImageIcon icon=types[i].getIcon();
				for (int a=amount;a>0;a--) {
					
					icons.add(icon);
				}
			}
		}				
	}
	
	public List<ImageIcon> getIcons() {

		return icons;
	}
	
	int getCostScore(final MagicPlayerProfile profile) {

		final int singleCounts[]=new int[MagicManaType.NR_OF_TYPES];
		int doubleCount=0;
		int maxSingleCount=0;
		for (int index=0;index<typeCount;index++) {
			
			if (types[index]!=MagicCostManaType.Colorless) {
				final MagicManaType profileTypes[]=types[index].getTypes(profile);
				final int amount=amounts[index];
				switch (profileTypes.length) {
					case 0:
						return 0;
					case 1:
						final int typeIndex=profileTypes[0].ordinal();
						singleCounts[typeIndex]+=amount;
						maxSingleCount=Math.max(maxSingleCount,singleCounts[typeIndex]);
						break;
					case 2:
						doubleCount+=amount;
						break;
				}
			} 
		}
		return 2*converted+3*(10-SINGLE_PENALTY[maxSingleCount]-DOUBLE_PENALTY[doubleCount]);
	}
	
	public int getConvertedCost() {
		
		return converted;
	}
		
	public MagicBuilderManaCost getBuilderCost() {
		
		return builderCost;
	}
	
	public void addTo(final MagicBuilderManaCost aBuilderCost) {
		
		for (int index=0;index<typeCount;index++) {
			aBuilderCost.addType(types[index],amounts[index]);
		}
		if (hasX) {
			aBuilderCost.setHasX();
		}	
		aBuilderCost.compress();
	}
	
	public void addTo(final MagicBuilderManaCost aBuilderCost,final int x) {
		
		for (int index=0;index<typeCount;index++) {
			aBuilderCost.addType(types[index],amounts[index]);
		}
		aBuilderCost.addType(MagicCostManaType.Colorless,x);
		aBuilderCost.compress();
	}
	
	public static MagicManaCost createCost(final String costText) {
		MagicManaCost cost=COSTS_MAP.get(costText);
		if (cost==null) {
			cost=new MagicManaCost(costText);
			COSTS_MAP.put(costText,cost);
		}
		return cost;
	}
}
