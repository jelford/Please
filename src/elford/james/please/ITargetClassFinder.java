package elford.james.please;

import java.util.List;

import elford.james.please.codegen.tinytypes.ClassName;


public interface ITargetClassFinder {
	public List<ClassName> findTargetClassNames();
}
