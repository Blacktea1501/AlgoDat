// O. Bittel
// 22.09.2022
package dictionary;

import java.util.Iterator;
import java.util.NoSuchElementException; // Import for Iterator

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys,
 * or by a Comparator provided at set creation time, depending on which constructor is used.
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 *
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V> {


    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }


    private Node<K, V> root = null;
    private int size = 0;
    private V oldValue = null;

    // Helper class for removeMin simulation, not directly related to AVL balancing
    private static class MinEntry<K, V> {
        K key;
        V value;
    }

    // Recursive helper for remove, extracts minimum node
    private Node<K, V> getRemMinR(Node<K, V> node, MinEntry<K, V> minEntry) {
        assert node != null;
        if (node.left == null) {
            // Found the minimum node
            minEntry.key = node.key;
            minEntry.value = node.value;
            // Replace node with its right child (might be null)
            Node<K,V> rightChild = node.right;
            if (rightChild != null) {
                rightChild.parent = node.parent; // Update parent pointer
            }
            return rightChild; // Return the right child to the caller
        } else {
            node.left = getRemMinR(node.left, minEntry);
            // After recursive call returns, re-set parent if node.left exists
            if (node.left != null) {
                 node.left.parent = node;
            }
        }
        // Balance the current node after modification in the left subtree
        return balance(node); // Return balanced node
    }


    private int getHeight(Node<K, V> node) {
        return node == null ? -1 : node.height;
    }

    private int getBalance(Node<K, V> node) {
        // If node is null, it's balanced (height -1 - (-1) = 0)
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    // Balances the subtree rooted at node and returns the new root of the balanced subtree.
    private Node<K, V> balance(Node<K, V> node) {
        if (node == null) return null; // Nothing to balance

        // Update height first, as rotations depend on child heights
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        int currentBalance = getBalance(node);

        // Case 1: Left Heavy (Balance Factor > 1)
        if (currentBalance > 1) {
            // Left-Left Case: Simple Right Rotation
            if (getBalance(node.left) >= 0) {
                node = rotateRight(node);
            }
            // Left-Right Case: Left Rotation on left child, then Right Rotation on node
            else {
                node = rotateLeftRight(node);
            }
        }
        // Case 2: Right Heavy (Balance Factor < -1)
        else if (currentBalance < -1) {
            // Right-Right Case: Simple Left Rotation
            if (getBalance(node.right) <= 0) {
                node = rotateLeft(node);
            }
            // Right-Left Case: Right Rotation on right child, then Left Rotation on node
            else {
                node = rotateRightLeft(node);
            }
        }
        // Else: node is balanced (balance is -1, 0, or 1) - do nothing

        return node; // Return the potentially new root of the balanced subtree
    }

    // Performs a right rotation on the subtree rooted at p.
    // Returns the new root of the rotated subtree.
    private Node<K, V> rotateRight(Node<K, V> p) {
        assert p.left != null; // Precondition for right rotation
        Node<K, V> x = p.left;
        Node<K, V> t2 = x.right; // Subtree T2
        Node<K, V> parent = p.parent; // Keep track of the original parent of p

        // Perform rotation
        x.right = p;
        p.parent = x; // p's new parent is x

        p.left = t2;
        if (t2 != null) {
            t2.parent = p; // t2's new parent is p
        }

        x.parent = parent; // x's parent becomes p's original parent
        if (parent != null) {
            // Update the child pointer of the original parent
            if (parent.left == p) {
                parent.left = x;
            } else {
                parent.right = x;
            }
        }
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1; // or getHeight(p)

        return x; // Return the new root of the rotated subtree
    }

    // Performs a left rotation on the subtree rooted at p.
    // Returns the new root of the rotated subtree.
    private Node<K, V> rotateLeft(Node<K, V> p) {
        assert p.right != null; // Precondition for left rotation
        Node<K, V> x = p.right;
        Node<K, V> t2 = x.left; // Subtree T2
        Node<K, V> parent = p.parent; // Keep track of the original parent of p

        // Perform rotation
        x.left = p;
        p.parent = x; // p's new parent is x

        p.right = t2;
        if (t2 != null) {
            t2.parent = p; // t2's new parent is p
        }

        // Link x to the original parent of p
        x.parent = parent;
        if (parent != null) {
            // Update the child pointer of the original parent
            if (parent.left == p) {
                parent.left = x;
            } else {
                // Assuming p was the right child if not the left
                parent.right = x;
            }
        }
        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1; // or getHeight(p)
        return x; // Return the new root of the rotated subtree
    }

    private Node<K, V> rotateLeftRight(Node<K, V> node) {
        assert node.left != null;
        node.left = rotateLeft(node.left);
        if (node.left != null) {
             node.left.parent = node;
        }
        return rotateRight(node);
    }

    private Node<K, V> rotateRightLeft(Node<K, V> node) {
        assert node.right != null;
        node.right = rotateRight(node.right);
        if (node.right != null) {
            node.right.parent = node;
        }
        return rotateLeft(node);
    }

    /**
     * Pretty prints the tree (Original Version)
     */
    public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }

    @Override
    public V insert(K key, V value) {
        oldValue = null; // Reset oldValue for this insertion attempt
        root = insertR(key, value, root);
        // The root's parent should always be null after insertion and balancing.
        if (root != null) {
            root.parent = null;
        }
        return oldValue; // Return value replaced, or null if new key
    }

    // Recursive helper for insert. Returns the new root of the subtree.
    private Node<K, V> insertR(K key, V value, Node<K, V> node) {
        if (node == null) {
            // Found insertion point
            node = new Node<>(key, value);
            oldValue = null; // Key was not present before
            size++;
        } else if (key.compareTo(node.key) < 0) {
            // Go left
            node.left = insertR(key, value, node.left);
            // After recursive call returns, set parent pointer for the potentially new left child
            if (node.left != null) {
                node.left.parent = node;
            }
        } else if (key.compareTo(node.key) > 0) {
            // Go right
            node.right = insertR(key, value, node.right);
             // After recursive call returns, set parent pointer for the potentially new right child
            if (node.right != null) {
                node.right.parent = node;
            }
        } else {
            // Key already exists, update value
            oldValue = node.value;
            node.value = value;
            // No need to balance here if only value is updated
            return node; // Return node without balancing if only value changed
        }

        // Balance the current node after insertion in a subtree
        return balance(node); // Return the (potentially new) root of this subtree
    }


    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    // Recursive helper for search
    private V searchR(K key, Node<K, V> node) {
        if (node == null) {
            return null; // Key not found
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return searchR(key, node.left); // Search in left subtree
        } else if (cmp > 0) {
            return searchR(key, node.right); // Search in right subtree
        } else {
            return node.value; // Key found
        }
    }

    @Override
    public V remove(K key) {
        oldValue = null; // Reset oldValue for this removal attempt
        root = removeR(key, root);
        // Ensure root's parent is null after removal
         if (root != null) {
            root.parent = null;
         }
        return oldValue;
    }

    // Recursive helper for remove. Returns the new root of the subtree.
    private Node<K, V> removeR(K key, Node<K, V> node) {
        if (node == null) {
            oldValue = null; // Key not found
            return null; // Return null as the subtree root
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            // Key is in the left subtree
            node.left = removeR(key, node.left);
            if (node.left != null) {
                 node.left.parent = node;
            }
        } else if (cmp > 0) {
            // Key is in the right subtree
            node.right = removeR(key, node.right);
             if (node.right != null) {
                 node.right.parent = node;
             }
        } else {
            // Node to be removed found
            oldValue = node.value; // Store value to return

            // Case 1: Node has 0 or 1 child
            if (node.left == null || node.right == null) {
                Node<K, V> child = (node.left != null) ? node.left : node.right;
                // If child exists, update its parent pointer BEFORE returning it
                if (child != null) {
                    child.parent = node.parent; // Link child to grandparent
                }
                size--; // Decrease size
                node = child; // Replace node with its child (or null) - MUST reassign node to return it
            }
            // Case 2: Node has 2 children
            else {
                // Find the inorder successor (smallest node in the right subtree)
                MinEntry<K, V> minEntry = new MinEntry<>();

                // Remove successor from right subtree and get the modified right subtree root
                node.right = getRemMinR(node.right, minEntry);
                // After removal, ensure parent of new right root is set
                if (node.right != null) {
                    node.right.parent = node;
                }

                // Replace node's data with successor's data
                node.key = minEntry.key;
                node.value = minEntry.value;
                // Size was already decremented in getRemMinR indirectly if successful
                size--;
            }
        }

        // Balance the current node (or the node that replaced it) after removal/modification
        // This needs to happen *after* the node is potentially replaced or modified
        return balance(node); // Return the balanced node
    }


    @Override
    public int size() {
        return size;
    }

    // --- prettyPrint method converted to toString  ---

    @Override
    public String toString() {
        String s = "";
        s = buildR(s, 0, root);
        return s;
    }


    private String buildR(String s, int level, Node<K, V> node) {
        s = s + buildLevel(level);
        if (node == null) {
            return s + "\n";
        } else {
            s = s + node.key + " " + node.value + "^" + ((node.parent == null) ? "null" : node.parent.key) + "\n";
            if (node.left != null || node.right != null) {
                s = buildR(s, level + 1, node.left);
                s = buildR(s, level + 1, node.right);
            }
        }
        return s;
    }

    private String buildLevel(int level) {
        return level == 0 ? "" : "  ".repeat(Math.max(0, level - 1)) + "|__";
    }

    // --- Iterator ---

    // Finds the leftmost node in the subtree rooted at p
    private Node<K, V> leftmost(Node<K, V> p) {
        if (p == null) return null;
        while (p.left != null) {
            p = p.left;
        }
        return p;
    }

    // Finds the successor of node p in the tree
    private Node<K, V> successor(Node<K, V> p) {
        if (p == null) return null;

        // If there's a right subtree, successor is the leftmost node there
        if (p.right != null) {
            return leftmost(p.right);
        }

        // Otherwise, go up until we come from a left child
        Node<K, V> current = p;
        Node<K, V> parent = p.parent;
        while (parent != null && current == parent.right) {
            current = parent;
            parent = parent.parent;
        }
        // parent is the successor (or null if p was the maximum node)
        return parent;
    }


    @Override
    public Iterator<Entry<K, V>> iterator() {
        // Start iterator at the smallest key (leftmost node)
        Node<K, V> current = leftmost(root);
        return new Iterator<>() {
            private Node<K, V> nextNode = current; // Node to be returned by next()

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in dictionary");
                }
                Node<K, V> nodeToReturn = nextNode;
                nextNode = successor(nextNode);
                return new Entry<>(nodeToReturn.key, nodeToReturn.value);
            }
        };
    }
}