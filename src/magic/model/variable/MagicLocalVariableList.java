package magic.model.variable;

import java.util.ArrayList;
import java.util.Collection;

public class MagicLocalVariableList extends ArrayList<MagicLocalVariable> {

	private static final long serialVersionUID = 1L;
	
	public MagicLocalVariableList() {}
	
	public MagicLocalVariableList(final Collection<MagicLocalVariable> localVariables) {
		addAll(localVariables);
	}
}
