package gitlet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    public T data;
    public TreeNode<T> parent;
    public List<TreeNode<T>> children;

    /**
     * Construct the root node
     */
    public TreeNode(T data) {
        this.data = data;
        this.parent = null;
        this.children = new LinkedList<>();
    }

    /**
     * Add a child TreeNode to the current TreeNode
     * @param data
     * @return child TreeNode
     */
    public TreeNode<T> addChild(T data) {
        TreeNode<T> childNode = new TreeNode<T>(data);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<T>(this);
    }
}
