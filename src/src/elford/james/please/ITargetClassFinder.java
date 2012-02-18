package src.elford.james.please;

import java.util.List;

import src.elford.james.please.codegen.tinytypes.ClassName;

public interface ITargetClassFinder {
	public List<ClassName> findTargetClassNames();
}
