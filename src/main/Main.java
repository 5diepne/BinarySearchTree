/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.management.Query;

class Student implements Comparable<Student> { // sắp xếp theo thứ tự trái phải cây nên implements

    int roll;

    public Student(int roll) {
        this.roll = roll;
    }

    @Override
    public int compareTo(Student o) {
        if (this.roll < o.roll) {
            return -1;
        }
        if (this.roll > o.roll) {
            return +1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return roll + "";
    }

}

class Node implements Comparable<Node> {

    Student data;
    Node left, right;

    public Node(Student o) {
        this.data = o;
        this.left = this.right = null;
    }

    @Override
    public int compareTo(Node o) {
        return this.data.compareTo(o.data);
    }

    @Override
    public String toString() {
        return data + "";
    }

}
// -------------------- tạo stack ------------------
class MyStack {

    LinkedList<Object> h;

    MyStack() {
        h = new LinkedList<>();
    }

    boolean isEmpty() {
        return (h.isEmpty());
    }

    void push(Object x) {
        h.add(x);
    }

    Object pop() {
        if (isEmpty()) {
            return (null);
        }
        return (h.removeLast());
    }
}

class BST { //Binary Search Tree

    Node root;

    public BST() {
        root = null;
    }

    boolean insert(Student o) {
        Node[] res = search(o);
        if (res[0] != null) {
            return false;
        }
        Node new_node = new Node(o);
        if (res[1] == null) // nghĩa là parent = null // cây đang empty
        {
            root = new_node;
        } else if (res[1].compareTo(new_node) == -1)// trên cây có data
        // -1 là parent < node mới
        {
            res[1].right = new_node;
        } else {
            res[1].left = new_node;
        }
        return true;
    }

    Node[] search(Student o) { // search trước khi insert để lấy ra nút cần tìm và nút parent của nó
        Node t = root; // khong tìm thấy thì node = null
        Node parent = null;
        while (t != null && t.data.compareTo(o) != 0) { //t!= null => NODE CUỐI CÙNG
            parent = t;
            if (t.data.compareTo(o) == -1) // so sánh data mới với cái đang có ( data hiện tại < data mới
            {
                t = t.right;
            } else {
                t = t.left;
            }
        }

        return new Node[]{t, parent};
    }

    public String descTraverse() { // duyệt cây giảm dần descending
        // làm đệ quy hoặc stack 
        return descTraverse(root); // giữ tính đóng gói // khong nên gọi trực tiếp
    }

    private String descTraverse(Node t) { // duyệt theo Right Node Left
        if (t == null) {
            return "";
        }
        return descTraverse(t.right) + t + " " + descTraverse(t.left);
    }

    String levelOrder() {
        String output = "";
        int level = 0;
        Queue<Node> waiting_list = new LinkedList<>();
        waiting_list.add(root); //enqueue
        if (root != null) {
            waiting_list.add(null);
        }
        while (!waiting_list.isEmpty()) { // rỗng thì dừng
            Node t = waiting_list.poll(); // dequeue
            if (t == null) {
                if (waiting_list.isEmpty()) {
                    break;
                }
                level++;
                waiting_list.add(null);
                continue;
            }
            output += t + "(" + level + ")";
            if (t.left != null) {
                waiting_list.add(t.left); // left trước right thì left ra trước với queue // stack thì ngược lại
            }
            if (t.right != null) {
                waiting_list.add(t.right);
            }
        }
        return output;
    }

    String levelOrderDepth() { // sai 
        String output = "";
        Stack<Node> waiting_list = new Stack<>();
        waiting_list.push(root); //push
        while (!waiting_list.isEmpty()) { // rỗng thì dừng
            Node t = waiting_list.pop(); // pop
            output += t + " ";
            if (t.left != null) {
                waiting_list.add(t.left); // left trước right thì left ra trước với queue // stack thì ngược lại
            }
            if (t.right != null) {
                waiting_list.add(t.right);
            }
        }
        return output;
    }

//    public Node search(Node p, int key){ // bằng đệ quy
//        if(p == null)
//            return null;
//        if(p.data == key) 
//            return p;// trả về địa chỉ của p
//        else if (p.data != key)
//    }
    
    
//-------------------------- Duyệt InOrder ---------------------------
    String inOrderTraversal() {
//        StringBuilder output = new StringBuilder();
        String output = "";
        MyStack stack = new MyStack();
        Node current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = (Node) stack.pop();
//            output.append(current).append(" "); // Append current node to the result
            output += current + " ";

            current = current.right;
        }
        return output.toString();
    }

//-------------------------- Duyệt PreOrder --------------------------- 
    String preOrderTraversal() {
//        StringBuilder output = new StringBuilder();
        String output = "";
        MyStack stack = new MyStack();
        if (root != null) {
            stack.push(root);
        }

        while (!stack.isEmpty()) {
            Node current = (Node) stack.pop();
//            output.append(current).append(" "); // Append current node to the result
            output += current + " ";

            // Push right child first, so it gets processed after the left child (LIFO)
            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }
        return output.toString();
    }

    // delete tree có 2 loại: copy và merge
    // delete <= 1 con và =2 con ( 2 trường hợp)
    boolean deleteByCopyRight(Student x) {
        Node[] res = search(x);
        Node pos = res[0];
        Node parent = res[1];
        if (pos == null) //  found x.rollnum
        {
            return false;
        }

        if ((pos.left != null) && (pos.right != null)) {
            Node t = pos.right;
            Node tParent = pos;
            while (t.left != null) {
                tParent = t;
                t = t.left;
            }
            pos.data = t.data;
            parent = tParent;
            pos = t;
        }
        //th1
        Node posChild = pos.left;
        if (posChild == null) {
            posChild = pos.right;
        }
        if (parent == null) {
            root = posChild;
        } else if (pos.data.compareTo(parent.data) >= 0) {
            parent.right = posChild;
        } else {
            parent.left = posChild;
        }

        return true;
    }

    boolean deleteByCopyLeft(Student x) {
        Node[] res = search(x);
        Node pos = res[0];
        Node parent = res[1];
        if (pos == null) //  found x.rollnum
        {
            return false;
        }
        if ((pos.left != null) && (pos.right != null)) {
            int count = 0;
            Node t = pos.left;
            Node tParent = pos;
            while (t.right != null) {
                tParent = t;
                t = t.right;
            }
            pos.data = t.data;
            parent = tParent;
            pos = t;
        }

        //th1
        Node posChild = pos.left;
        if (posChild == null) {
            posChild = pos.right;
        }
        if (parent == null) {
            root = posChild;
        } else if (pos.data.compareTo(parent.data) > 0) {
            parent.right = posChild;
        } else {
            parent.left = posChild;
        }

        return true;
    }

// --------------- delete theo Merge ------------------
    boolean deleteByMergingLeft(Student x) {
        Node[] res = search(x);
        Node pos = res[0];
        Node parent = res[1];
        if (pos == null) //  found x.rollnum
        {
            return false;
        }

        if ((pos.left != null) && (pos.right != null)) {
            Node t = pos.left;
            Node parentT = pos;
            while (t.right != null) {
                parentT = t;
                t = t.right;
            }
            t.right = pos.right;
            if (pos.compareTo(parent) > 0) {
                parent.right = pos.left;
            } else {
                parent.left = pos.left;
            }
            return true;
        }

        Node posChild = pos.left;
        if (posChild == null) {
            posChild = pos.right;
        }
        if (parent == null) {
            root = posChild;
        } else if (pos.data.compareTo(parent.data) > 0) {
            parent.right = posChild;
        } else {
            parent.left = posChild;
        }

        return true;
    }

    boolean deleteByMergingRight(Student x) {
        Node[] res = search(x);
        Node pos = res[0];
        Node parent = res[1];
        if (pos == null) //  found x.rollnum
        {
            return false;
        }

        if ((pos.left != null) && (pos.right != null)) {
            Node t = pos.right;
            Node parentT = pos;
            while (t.left != null) {
                parentT = t;
                t = t.left;
            }
            t.left = pos.left;
            if (pos.compareTo(parent) > 0) {
                parent.right = pos.right;
            } else {
                parent.left = pos.right;
            }
            return true;
        }

        //th1
        Node posChild = pos.left;
        if (posChild == null) {
            posChild = pos.right;
        }
        if (parent == null) {
            root = posChild;
        } else if (pos.data.compareTo(parent.data) > 0) {
            parent.right = posChild;
        } else {
            parent.left = posChild;
        }

        return true;
    }
    // ===============================================================

}

public class Main {

    public static void main(String[] args) {
        BST T = new BST();
        T.insert(new Student(5));
        T.insert(new Student(7));
        T.insert(new Student(3));
        T.insert(new Student(4));
        T.insert(new Student(8));
        T.insert(new Student(6)); // khong tìm thấy thì node = null
        System.out.println("Descending Traverse " + T.descTraverse());
        System.out.println("Level Order: " + T.levelOrder());
        // System.out.println("Level Order: " + T.levelOrderDepth()); //duyệt k đúng
        System.out.println("In-Order Traversal: " + T.inOrderTraversal());
        System.out.println("Pre-Order Traversal: " + T.preOrderTraversal());

//        // delete by merge right
//        System.out.println(T.deleteByMergingRight(new Student(5)));
    }

}
