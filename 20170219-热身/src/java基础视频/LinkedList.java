package java基础视频;

import java.util.Objects;

/**
 * 双向链表
 * 基于链表实现
 * @author LJ
 *
 */

public class LinkedList<T> implements List<T>{
	private int size;
	private Node<T> head;
	private Node<T> last;
	
	public LinkedList() {
		this.head = new Node<T>(null); // 空节点
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public boolean add(T o) {
		if(this.last == null) { // 只有头结点的时候
			this.last = new Node<T>(o);
			this.last.pre = this.head;
			this.head.next = this.last;
		}else {
			Node<T> oldLast = this.last;
			this.last = new Node<T>(o);
			this.last.pre = oldLast;
			oldLast.next = this.last;
		}
		this.size++;
		return true;
	}

	@Override
	public void add(int index, T o) {
		checkIndex(index);
		Node<T> node = head;
		for(int i=0;i<=index;i++) {
			node = node.next;
		}
		Node<T> newNode = new Node<T>(o);
		Node<T> pre = node.pre;
		pre.next = newNode;
		newNode.next = node;
		newNode.pre = pre;
		node.pre = newNode;
	}

	private void checkIndex(int index) {
		if(index < 0 || index > size()-1) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public T get(int index) {
		Node<T> node = head;
		for(int i=0;i<=index;i++) {
			node = node.next;
		}
		return node.data;
	}

	@Override
	public T set(int index, T o) {
		Node<T> node = head;
		Node<T> newNode = new Node<T>(o);
		for(int i=0;i<=index;i++) {
			node = node.next;
		}
		node.pre.next = newNode;
		newNode.pre = node.pre;
		newNode.next = node;
		node.pre = newNode;
		return newNode.data;
	}

	@Override
	public boolean remove(T o) {
		Node<T> node = head;
		while(node.next != null) { // 非head
			node = node.next;
			if(Objects.equals(node.data, o)) {
				node.pre.next = node.next;
				if(node.next != null) { // 当前节点的下一个节点不是头结点时，若是头结点，就不用考虑pre；
					node.next.pre = node.pre;
				}
				if(node.next == null) { // 当前节点是last节点时，删除时需要修改last节点
					this.last = node.pre;
				}
				this.size --;
				return true;
			}
		}		
		return false;
	}

	@Override
	public T remove(int index) {
		checkIndex(index);
		Node<T> node = getNode(index);
		Node<T> pre = node.pre;
		Node<T> next = node.next;
		pre.next = next;
		if(next != null) {
			next.pre = pre;
		}
		if(next == null) {
			this.last = pre;
		}
		this.size--;
		return node.data;
	}
	
	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		int i = 0;
		Node<T> node = head;
		while(node.next != null) {
			node = node.next;
			array[i] = node.data;
			i++;
		}
		return array;
	}

	@Override
	public boolean contains(Object o) {
		Node<T> node = head;
		while(node.next != null) {
			node = node.next;
			if(Objects.equals(node, o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		this.head.next = null;
		this.last = null;
		this.size = 0;
	}
	
	private Node<T> getNode(int index) {
		Node<T> node = head;
		for(int i=0;i<=index;i++) {
			node = node.next;
			if(node == null) {
				return null;
			}
		}
		return node;
	}
	
	@Override
	public int indexOf(T o) {
		int index = 0;
		Node<T> node = head;
		while(node.next != null) {
			node = node.next;
			if(Objects.equals(node.data, o)) {
				return index;
			}
			index ++;
		}
		return -1;
	}
	

	public static class Node<T>{
		// 静态内部类和LinkedList类绑定，而不是与实例绑定
		// 因为此内部类只供自己用，故可以不写get和set方法；
		
		T data;
		Node<T> next;
		Node<T> pre;
		
		Node(T data){
			this.data = data;
		}
	}


}
