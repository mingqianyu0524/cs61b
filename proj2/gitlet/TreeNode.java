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

    public void removeNode(T data, Comparable<T> cmp) {
        TreeNode<T> node = new TreeNode<T>(data);
        TreeNode target = findTreeNode(cmp);
        if (target != null) {
            if (target.parent != null) {
                target.parent.children.remove(target);
            }
        } else {
            System.out.println("node not exist");
        }
    }

    /**
     * Find tree node given a customized comparable
     * @param cmp
     * @return
     */
    public TreeNode<T> findTreeNode(Comparable<T> cmp) {
        for (TreeNode<T> node : this) {
            T elData = node.data;
            if (cmp.compareTo(elData) == 0) {
                return node;
            }
        }
        return null;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<T>(this);
    }
}
