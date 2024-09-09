import java.util.Collection;
import java.util.NoSuchElementException;

public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    public AVL() {

    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        for (T dataElement : data) {
            if (dataElement == null) {
                throw new IllegalArgumentException("Data is null.");
            }
            add(dataElement);
        }
    }

    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        root = addHelper(data, root);
    }

    private AVLNode<T> addHelper(T data, AVLNode<T> node) {
        if (node == null) {
            size++;
            return new AVLNode<T>(data);
        }
        if (data.compareTo(node.getData()) < 0) {
            node.setLeft(addHelper(data, node.getLeft()));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(addHelper(data, node.getRight()));
        }
        updateHeight(node);
        updateBF(node);
        return rotate(node);
    }

    private AVLNode<T> leftRotation(AVLNode<T> node) {
        AVLNode<T> rightChild = node.getRight();
        AVLNode<T> leftChild = rightChild.getLeft();
        rightChild.setLeft(node);
        node.setRight(leftChild);
        updateHeight(node);
        updateBF(node);
        updateHeight(rightChild);
        updateBF(rightChild);
        return rightChild;
    }

    private AVLNode<T> rotate(AVLNode<T> node) {
        if (node.getBalanceFactor() < -1) {
            AVLNode<T> right = node.getRight();
            if (right.getBalanceFactor() <= 0) {
                return leftRotation(node);
            } else {
                node.setRight(rightRotation(node.getRight()));
                return leftRotation(node);
            }
        } else if (node.getBalanceFactor() > 1) {
            AVLNode<T> left = node.getLeft();
            if (left.getBalanceFactor() >= 0) {
                return rightRotation(node);
            } else {
                node.setLeft(leftRotation(node.getLeft()));
                return rightRotation(node);
            }
        }
        return node;
    }

    private AVLNode<T> updateBF(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft();
        AVLNode<T> right = node.getRight();
        int leftHeight = -1;
        int rightHeight = -1;
        if (left != null) {
            leftHeight = left.getHeight();
        }
        if (right != null) {
            rightHeight = right.getHeight();
        }
        int balanceFactor = leftHeight - rightHeight;
        node.setBalanceFactor(balanceFactor);
        return node;
    }

    private void updateHeight(AVLNode<T> node) {
        AVLNode<T> left = node.getLeft();
        AVLNode<T> right = node.getRight();
        int leftHeight = -1;
        int rightHeight = -1;
        if (left != null) {
            leftHeight = left.getHeight();
        }
        if (right != null) {
            rightHeight = right.getHeight();
        }
        int height = Math.max(leftHeight, rightHeight) + 1;
        node.setHeight(height);
    }

    private AVLNode<T> rightRotation(AVLNode<T> node) {
        AVLNode<T> leftChild = node.getLeft();
        AVLNode<T> rightChild = leftChild.getRight();
        leftChild.setRight(node);
        node.setLeft(rightChild);
        updateHeight(node);
        updateBF(node);
        updateHeight(leftChild);
        updateBF(leftChild);
        return leftChild;
    }

    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        AVLNode<T> removedNode = new AVLNode<>(data);
        root = removeHelper(data, root, removedNode);
        return removedNode.getData();
    }

    private AVLNode<T> removeHelper(T data, AVLNode<T> node, AVLNode<T> removedNode) {
        if (node == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        if (node.getData().compareTo(data) < 0) {
            node.setRight(removeHelper(data, node.getRight(), removedNode));
        } else if (node.getData().compareTo(data) > 0) {
            node.setLeft(removeHelper(data, node.getLeft(), removedNode));
        } else {
            removedNode.setData(node.getData());
            size--;
            // 3 removal methods
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            if (node.getLeft() == null) {
                return node.getRight();
            }
            if (node.getRight() == null) {
                return node.getLeft();
            }
            if (node.getRight() != null && node.getLeft() != null) {
                // next smallest ; one right and then as left as possible
                AVLNode<T> replacedNode = new AVLNode<>(null);
                node.setLeft(removeSecondHelper(data, node.getLeft(), replacedNode));
                node.setData(replacedNode.getData());
            }
        }
        updateHeight(node);
        updateBF(node);
        return rotate(node);
    }

    private AVLNode<T> removeSecondHelper(T data, AVLNode<T> node, AVLNode<T> replacedNode) {
        if (node.getRight() == null) {
            replacedNode.setData(node.getData());
            return node.getLeft();
        }
        node.setRight(removeSecondHelper(data, node.getRight(), replacedNode));
        return node;
    }

    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        T result = containsHelper(data, root);
        if (result == null) {
            throw new NoSuchElementException("Data is not in tree.");
        }
        return result;
    }

    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null.");
        }
        return containsHelper(data, root) != null;
    }

    private T containsHelper(T data, AVLNode<T> node) {
        if (node == null) {
            return null;
        }
        if (data.compareTo(node.getData()) == 0) {
            return node.getData();
        } else if (data.compareTo(node.getData()) < 0) {
            return containsHelper(data, node.getLeft());
        } else {
            return containsHelper(data, node.getRight());
        }
    }

    public T findMedian() {
        // mod to find if size % 2 == 0; if even, throw exception
        // find bf of root and see which child is heavier
        // go to heavier child and pre-order traversal
        // increment counter till reach (size / 2 + 1)
        // return median
        if (root == null || size % 2 == 0) {
            throw new NoSuchElementException("Tree is empty or contains even number of data.");
        }
        AVLNode<Integer> count = new AVLNode<Integer>(0);
        AVLNode<T> answer = new AVLNode<>(null);
        inorderHelper(root, count, answer);
        return answer.getData();
    }

    private void inorderHelper(AVLNode<T> node, AVLNode<Integer> count, AVLNode<T> answer) {
        int median = size / 2 + 1;
        if (node == null) {
            return;
        }
        if (count.getData() != median) {
            inorderHelper(node.getLeft(), count, answer);
        }
        count.setData(count.getData() + 1);
        if (count.getData() == median) {
            answer.setData(node.getData());
            return;
        }
        if (count.getData() < median) {
            inorderHelper(node.getRight(), count, answer);
        }
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    public int size() {
        return size;
    }

    public AVLNode<T> getRoot() {
        return root;
    }
}
