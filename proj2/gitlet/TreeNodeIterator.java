package gitlet;

import java.util.Iterator;
import java.util.List;

public class TreeNodeIterator<T> implements Iterator<TreeNode<T>> {

    enum ProcessStage {
        ProcessParent, ProcessChildCurNode, ProcessChildSubNode
    }

    private TreeNode<T> current = null;
    private TreeNode<T> next;
    private ProcessStage doNext = null;
    private Iterator<TreeNode<T>> childrenIterator;
    private Iterator<TreeNode<T>> childrenSubNodeIter;

    public TreeNodeIterator(TreeNode<T> treeNode) {
        this.current = treeNode;
        this.doNext = ProcessStage.ProcessParent;
        this.childrenIterator = treeNode.children.iterator();
    }

    @Override
    public boolean hasNext() {
        switch (doNext) {
            case ProcessParent:
                this.next = this.current;
                this.doNext = ProcessStage.ProcessChildCurNode;
                return true;
            case ProcessChildCurNode:
                if (childrenIterator.hasNext()) {
                    this.next = childrenIterator.next();
                    return true;
                }
                break;
            case ProcessChildSubNode:
                break;
            default:
                return false;
        }
    }

    @Override
    public TreeNode<T> next() {
        return this.next;
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }
}
