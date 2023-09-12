import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// DEQUE CLASS //////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

abstract class ANode<T> {
	ANode<T> next;
	ANode<T> prev;

	// counts the number of nodes
	public abstract int size();

	// EFFECT: adds the given node to either the head or tail of this list
	public void addInBetween(T value, ANode<T> prev, ANode<T> next) {
		new Node<T>(value, prev, next);
	}

	// EFFECT: removes a node from either the head or tail this list
	// returns the removed data
	public T remove() {
		this.next.prev = this.prev;
		this.prev.next = this.next;
		return this.nodeData();
	}

	// returns this node's data
	public abstract T nodeData();

}

class Node<T> extends ANode<T> {
	T data;

	Node(T data) {
		this.data = data;
		this.next = null;
		this.prev = null;
	}

	Node(T data, ANode<T> next, ANode<T> prev) {
		if (next == null || prev == null) {
			throw new IllegalArgumentException("node is null");
		}
		this.data = data;
		this.next = next;
		this.prev = prev;
		next.prev = this;
		prev.next = this;
	}

	// counts the number of nodes
	public int size() {
		return 1 + this.next.size();
	}

	// returns this data
	public T nodeData() {
		return this.data;
	}
}

class Sentinel<T> extends ANode<T> {
	Sentinel() {
		this.next = this;
		this.prev = this;
	}

	// circled back to the sentinel, which isn't counted in the size of a Deque
	public int size() {
		return 0;
	}

	// returns this node's data
	public T nodeData() {
		return null; // no data of a Sentinel
	}
}

class Deque<T> {
	Sentinel<T> header;

	Deque() {
		this.header = new Sentinel<T>();
	}

	Deque(Sentinel<T> header) {
		this.header = header;
	}

	// counts the number of nodes in this Deque
	public int size() {
		return this.header.next.size();
	}

	// EFFECT: modifies this Deque by inserting the given value of type T at the
	// front of this list
	public void addAtHead(T value) {
		this.header.addInBetween(value, this.header.next, this.header);
	}

	// EFFECT: modifies this Deque by inserting the given value of type T at the
	// tail of this list
	public void addAtTail(T value) {
		this.header.addInBetween(value, this.header, this.header.prev);
	}

	// EFFECT: removes the first node from this list
	// returns the removed data
	public T removeFromHead() {
		if (this.size() == 0) {
			throw new RuntimeException("cannot remove node from empty list");
		} else {
			return this.header.next.remove();
		}
	}

	// EFFECT: removes the last node from this list
	// returns the removed data
	public T removeFromTail() {
		if (this.size() == 0) {
			throw new RuntimeException("cannot remove node from empty list");
		} else {
			return this.header.prev.remove();
		}
	}

	// EFFECT: removes the given node from this Deque
	public void removeNode(ANode<T> node) {
		if (!node.equals(this.header)) {
			node.remove();
		}
	}

	// returns true if the given node is in this Deque
	public boolean contains(ANode<T> node) {
		return this.header.equals(node) || this.header.next.equals(node);
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////// MAZE WORLD ///////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////

// represents a Vertex (cell) on the maze
class Vertex {
	// in logical coordinates, with the origin at the top left
	int x;
	int y;
	ArrayList<Edge> outEdges;

	Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * TEMPLATE FIELDS: ... this.x ... -- int ... this.y ... -- int ...
	 * this.outEdges ... -- ArrayList<Edge>
	 */
}

// represents an Edge of the maze (the wall between the to and from Vertices)
class Edge implements Comparable<Edge> {
	Vertex from;
	Vertex to;
	int weight;

	Edge(Vertex from, Vertex to, int weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	/*
	 * TEMPLATE FIELDS: ... this.from ... -- Vertex ... this.to ... -- Vertex ...
	 * this.weight ... -- int
	 * 
	 * METHODS: ... this.compareTo(Edge e) ... -- int
	 */

	// compares this edge to the given edge
	public int compareTo(Edge e) {
		return this.weight - e.weight;
	}
}

// represents the setup and functionality of the maze
class MazeWorld extends World {
	int width;
	int height;
	ArrayList<ArrayList<Vertex>> vertices; // a 2D array of all vertices
	ArrayList<Edge> edges; // all edges
	HashMap<Vertex, Vertex> representatives;
	static final int SCALE = 10;
	boolean bfs;
	boolean dfs;
	boolean manual;
	Posn currentPosition; // the player's current position

	// constructor that takes in the dimensions of the maze
	MazeWorld(int width, int height) {
		this.width = new Utils().checkRange(width, 2, 100, "Width: " + width + " is not a valid width.");
		this.height = new Utils().checkRange(height, 2, 60, "Height: " + height + " is not a valid height.");
		this.vertices = this.makeVertices();
		this.edges = this.makeEdges();
		this.currentPosition = new Posn(0, 0);
	}

	/*
	 * TEMPLATE FIELDS: ... this.width ... -- int ... this.height ... -- int ...
	 * this.vertices ... -- ArrayList<ArrayList<Vertex>> ... this.edges ... --
	 * ArrayList<Edge> ... this.representatives ... -- HashMap<Vertex, Vertex> ...
	 * SCALE ... -- int ... this.bfs ... -- boolean ... this.dfs ... -- boolean ...
	 * this.manual ... -- boolean ... this.currentPosition ... -- Posn
	 * 
	 * METHODS: ... this.makeScene() ... -- WorldScene ...
	 * this.drawVertices(WorldScene bg, int imageScale) ... -- WorldScene ...
	 * this.drawVertex(int imageScale) ... -- WorldImage ...
	 * this.drawStartVertex(int imageScale) ... -- WorldImage ...
	 * this.drawEndVertex(int imageScale) ... -- WorldImage ...
	 * this.drawEdges(WorldScene bg, int imageScale) ... -- WorldScene ...
	 * this.drawEdge(int imageScale) ... -- WorldImage ... this.makeVertices() ...
	 * -- ArrayList<ArrayList<Vertices>> ... this.makeEdges() ... -- ArrayList<Edge>
	 * ... this.generateMaze() ... -- ArrayList<Edge> ... this.union(HashMap<Vertex,
	 * Vertex> representatives, Vertex v1, Vertex v2) ... -- void ...
	 * this.find(HashMap<Vertex, Vertex> representatives, Vertex v) ... -- Vertex
	 * ... this.onKeyEvent(String key) ... -- void ... this.changePosition(String
	 * key) ... -- void ... this.onTick() ... -- void
	 */

	//////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////// DRAWING METHODS
	////////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	// configures the game's World
	public WorldScene makeScene() {
		WorldScene bg = new WorldScene(800, 800);
		bg.placeImageXY(new RectangleImage(800, 800, OutlineMode.SOLID, Color.black), 400, 400);
		int imageScale = 800 / this.width;
		this.drawVertices(bg, imageScale);
		this.drawEdges(bg, imageScale);
		return bg;
	}

	// draws all the vertices
	public WorldScene drawVertices(WorldScene bg, int imageScale) {
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				Vertex v = this.vertices.get(i).get(j);
				if (i == 0 && j == 0) {
					bg.placeImageXY(this.drawStartVertex(imageScale), v.x * imageScale + (imageScale / 2),
							v.y * imageScale + (imageScale / 2));
				} else if (i == this.width - 1 && j == this.height - 1) {
					bg.placeImageXY(this.drawEndVertex(imageScale), v.x * imageScale + (imageScale / 2),
							v.y * imageScale + (imageScale / 2));
				} else {
					bg.placeImageXY(this.drawVertex(imageScale), v.x * imageScale + (imageScale / 2),
							v.y * imageScale + (imageScale / 2));
				}
			}
		}
		return bg;
	}

	// draws a single Vertex
	public WorldImage drawVertex(int imageScale) {
		return new RectangleImage(imageScale - 2, imageScale - 2, OutlineMode.SOLID, Color.white);
	}

	// colors in the start Vertex
	public WorldImage drawStartVertex(int imageScale) {
		return new RectangleImage(imageScale - 2, imageScale - 2, OutlineMode.SOLID, Color.green);
	}

	// colors in the end Vertex
	public WorldImage drawEndVertex(int imageScale) {
		return new RectangleImage(imageScale - 2, imageScale - 2, OutlineMode.SOLID, Color.red);
	}

	// draws all the edges
	public WorldScene drawEdges(WorldScene bg, int imageScale) {
		edges = this.generateMaze();
		for (int i = 0; i < edges.size(); i++) {
			bg.placeImageXY(this.drawEdge(imageScale),
					(edges.get(i).from.x + edges.get(i).to.x) * imageScale / 2 + (imageScale / 2),
					(edges.get(i).from.y + edges.get(i).to.y) * imageScale / 2 + (imageScale / 2));
		}
		return bg;
	}

	// draws a single Edge
	public WorldImage drawEdge(int imageScale) {
		return new RectangleImage(imageScale - 2, imageScale - 2, OutlineMode.SOLID, Color.white);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////// CREATING THE GRID
	////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	// creates a 2D ArrayList of vertices
	public ArrayList<ArrayList<Vertex>> makeVertices() {
		ArrayList<ArrayList<Vertex>> vertices = new ArrayList<ArrayList<Vertex>>(Arrays.asList());
		for (int i = 0; i < this.width; i++) {
			ArrayList<Vertex> row = new ArrayList<Vertex>(Arrays.asList());
			for (int j = 0; j < this.height; j++) {
				row.add(new Vertex(i, j));
			}
			vertices.add(row);
		}
		return vertices;
	}

	// creates an ArrayList of edges
	public ArrayList<Edge> makeEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Random rand = new Random();

		// edges connecting two horizontal vertices
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height - 1; j++) {
				edges.add(new Edge(this.vertices.get(i).get(j), this.vertices.get(i).get(j + 1), rand.nextInt(100)));
			}
		}

		// edges connecting two vertical vertices
		for (int i = 0; i < this.width - 1; i++) {
			for (int j = 0; j < this.height; j++) {
				edges.add(new Edge(this.vertices.get(i).get(j), this.vertices.get(i + 1).get(j), rand.nextInt(100)));
			}
		}

		return edges;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////// KRUSKAL'S ALGORITHM
	////////////////////////////////////////////////////////////////////////////////////////////////// //////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	// generate maze using Kruskal's algorithm
	public ArrayList<Edge> generateMaze() {
		ArrayList<Edge> worklist = this.edges;
		Collections.sort(worklist); // sort all edges by weight
		ArrayList<Edge> edgesInTree = new ArrayList<Edge>(Arrays.asList());
		HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>();
		this.representatives = representatives;

		// initialize every node's representative to itself
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				representatives.put(this.vertices.get(i).get(j), this.vertices.get(i).get(j));
			}
		}

		// while there is more than one tree
		int n = this.width * this.height;
		while (edgesInTree.size() < n - 1) {
			Edge cheapest = worklist.remove(0);
			Vertex x = cheapest.from;
			Vertex y = cheapest.to;
			if (!this.find(representatives, x).equals(this.find(representatives, y))) {
				// if representatives aren't equal
				edgesInTree.add(cheapest);
				union(representatives, find(representatives, x), find(representatives, y));
			}
		}
		return edgesInTree;
	}

	// union two edges
	public void union(HashMap<Vertex, Vertex> representatives, Vertex v1, Vertex v2) {
		// set v2's representative's representative to v1's representative
		representatives.put(find(representatives, v1), find(representatives, v2));
	}

	// find the given vertex in the HashMap and return its value
	public Vertex find(HashMap<Vertex, Vertex> representatives, Vertex v) {
		if (v.equals(representatives.get(v))) {
			return v;
		} else {
			return find(representatives, representatives.get(v));
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////// SOLVE THE MAZE
	////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	boolean searchHelp(Vertex from, Vertex to, Collection<Vertex> worklist) {
		Deque<Vertex> alreadySeen = new Deque<Vertex>();

		// Initialize the worklist with the from vertex
		worklist.add(from);
		// As long as the worklist isn't empty...
		while (!worklist.isEmpty()) {
			Vertex next = worklist.remove();
			if (next.equals(to)) {
				return true; // Success!
			} else if (alreadySeen.contains(next)) {
				// do nothing: we've already seen this one
			} else {
				// add all the neighbors of next to the worklist for further processing
				for (Edge e : next.outEdges) {
					worklist.add(e.to);
				}
				// add next to alreadySeen, since we're done with it
				alreadySeen.addAtHead(next);
			}
		}
		// We haven't found the to vertex, and there are no more to try
		return false;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////// BIG-BANG HANDLERS
	////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	// "b" -- perform a breadth-first search
	// "d" -- perform a depth-first search
	// "m" -- player manually solves the maze
	// "r" -- resets the game and generates a new maze
	public void onKeyEvent(String key) {
		if (this.manual) {
			this.changePosition(key);
		}
		if (key.equals("b")) {
			this.bfs = true;
		} else if (key.equals("d")) {
			this.dfs = true;
		} else if (key.equals("m")) {
			this.manual = true;
		} else if (key.equals("r")) {
			this.edges = this.makeEdges();
			this.makeScene();
		}
	}

	// changes the player's current position
	public void changePosition(String key) {
		if (key.equals("up") && this.currentPosition.y != 0) {
			this.currentPosition = new Posn(this.currentPosition.x, this.currentPosition.y - 1);
		} else if (key.equals("down") && this.currentPosition.y != this.height - 1) {
			this.currentPosition = new Posn(this.currentPosition.x, this.currentPosition.y + 1);
		} else if (key.equals("left") && this.currentPosition.x != 0) {
			this.currentPosition = new Posn(this.currentPosition.x - 1, this.currentPosition.y);
		} else if (key.equals("right") && this.currentPosition.x != this.width - 1) {
			this.currentPosition = new Posn(this.currentPosition.x + 1, this.currentPosition.y);
		}
	}
}

// validates arguments that the player passes in through constructor
class Utils {
	// checks that the given value is valid and if not, return an exception
	int checkRange(int val, int min, int max, String msg) {
		if (val >= min && val <= max) {
			return val;
		} else {
			throw new IllegalArgumentException(msg);
		}
	}
}

// class for examples of MazeWorlds and testing purposes
class ExamplesMaze {
	ExamplesMaze() {
	}

	MazeWorld maze1 = new MazeWorld(2, 2);
	MazeWorld maze2 = new MazeWorld(3, 4);
	MazeWorld maze3 = new MazeWorld(100, 60);

	// configures the initial test conditions
	void initTestConditions() {
		this.maze1 = new MazeWorld(2, 2);
		this.maze2 = new MazeWorld(3, 4);
		this.maze3 = new MazeWorld(100, 60);
	}

	// to run the game
	void testBigBang(Tester t) {
		double TICK_RATE = 0.1;
		this.maze3.bigBang(800, 800, TICK_RATE);
	}

	// tests that the constructor correctly validates/invalidates parameters
	void testConstructor(Tester t) {
		t.checkConstructorException(new IllegalArgumentException("Width: 1 is not a valid width."), "MazeWorld", 1, 10);
		t.checkConstructorException(new IllegalArgumentException("Height: 300 is not a valid height."), "MazeWorld", 5,
				300);
	}

	// tests the compareTo method
	void testCompareTo(Tester t) {
		Edge e1 = new Edge(new Vertex(0, 0), new Vertex(0, 1), 1);
		Edge e2 = new Edge(new Vertex(1, 0), new Vertex(0, 1), 2);
		Edge e3 = new Edge(new Vertex(0, 0), new Vertex(0, 1), 3);
		Edge e4 = new Edge(new Vertex(1, 1), new Vertex(2, 1), 2);
		t.checkExpect(e1.compareTo(e2), -1);
		t.checkExpect(e3.compareTo(e2), 1);
		t.checkExpect(e2.compareTo(e4), 0);
	}

	/*
	 * // tests the makeScene method void testMakeScene(Tester t) {
	 * this.initTestConditions(); WorldScene scene = new MazeWorld(2,
	 * 2).makeScene(); WorldScene bg = new WorldScene(1000, 600);
	 * bg.placeImageXY(new RectangleImage(1000, 600, OutlineMode.SOLID,
	 * Color.black), 500, 300); bg.placeImageXY(new RectangleImage(498, 498,
	 * OutlineMode.SOLID, Color.white), 250, 250); bg.placeImageXY(new
	 * RectangleImage(498, 498, OutlineMode.SOLID, Color.white), 250, 750);
	 * bg.placeImageXY(new RectangleImage(498, 498, OutlineMode.SOLID, Color.white),
	 * 750, 250); bg.placeImageXY(new RectangleImage(498, 498, OutlineMode.SOLID,
	 * Color.white), 750, 750); bg.placeImageXY(new RectangleImage(498, 498,
	 * OutlineMode.SOLID, Color.white), 500, 250); bg.placeImageXY(new
	 * RectangleImage(498, 498, OutlineMode.SOLID, Color.white), 250, 500);
	 * bg.placeImageXY(new RectangleImage(498, 498, OutlineMode.SOLID, Color.white),
	 * 750, 500); t.checkExpect(scene, bg); }
	 * 
	 * // tests the drawVertices method void testDrawVertices(Tester t) {
	 * this.initTestConditions(); WorldScene bg = new WorldScene(1000, 600);
	 * WorldScene bg2 = new WorldScene(1000, 600); bg2.placeImageXY(new
	 * RectangleImage(498, 498, OutlineMode.SOLID, Color.white), 250, 250);
	 * bg2.placeImageXY(new RectangleImage(498, 498, OutlineMode.SOLID,
	 * Color.white), 250, 750); bg2.placeImageXY(new RectangleImage(498, 498,
	 * OutlineMode.SOLID, Color.white), 750, 250); bg2.placeImageXY(new
	 * RectangleImage(498, 498, OutlineMode.SOLID, Color.white), 750, 750);
	 * t.checkExpect(new MazeWorld(2, 2).drawVertices(bg, 500), bg2); }
	 * 
	 * // tests the drawEdges method void testDrawEdges(Tester t) {
	 * this.initTestConditions(); WorldScene bg = new WorldScene(1000, 600);
	 * WorldScene bg2 = new WorldScene(1000, 600); bg2.placeImageXY(new
	 * RectangleImage(1000, 600, OutlineMode.OUTLINE, Color.black), 500, 250);
	 * bg2.placeImageXY(new RectangleImage(498, 498, OutlineMode.SOLID,
	 * Color.white), 250, 500); bg2.placeImageXY(new RectangleImage(498, 498,
	 * OutlineMode.SOLID, Color.white), 250, 500); bg2.placeImageXY(new
	 * RectangleImage(498, 498, OutlineMode.SOLID, Color.white), 500, 750);
	 * t.checkExpect(new MazeWorld(2, 2).drawEdges(bg, 500), bg2); }
	 */

	// tests the drawVertex method
	void testDrawVertex(Tester t) {
		this.initTestConditions();
		t.checkExpect(new MazeWorld(2, 2).drawVertex(500),
				new RectangleImage(498, 498, OutlineMode.SOLID, Color.white));
		t.checkExpect(new MazeWorld(4, 4).drawVertex(250),
				new RectangleImage(248, 248, OutlineMode.SOLID, Color.white));
	}

	// tests the drawStartVertex method
	void testDrawStartVertex(Tester t) {
		this.initTestConditions();
		t.checkExpect(new MazeWorld(2, 2).drawStartVertex(500),
				new RectangleImage(498, 498, OutlineMode.SOLID, Color.green));
		t.checkExpect(new MazeWorld(4, 4).drawStartVertex(250),
				new RectangleImage(248, 248, OutlineMode.SOLID, Color.green));
	}

	// tests the drawEndVertex method
	void testDrawEndVertex(Tester t) {
		this.initTestConditions();
		t.checkExpect(new MazeWorld(2, 2).drawEndVertex(500),
				new RectangleImage(498, 498, OutlineMode.SOLID, Color.red));
		t.checkExpect(new MazeWorld(4, 4).drawEndVertex(250),
				new RectangleImage(248, 248, OutlineMode.SOLID, Color.red));
	}

	// tests the drawEdge method
	void testDrawEdge(Tester t) {
		this.initTestConditions();
		t.checkExpect(new MazeWorld(2, 2).drawEdge(500), new RectangleImage(498, 498, OutlineMode.SOLID, Color.white));
		t.checkExpect(new MazeWorld(4, 4).drawEdge(250), new RectangleImage(248, 248, OutlineMode.SOLID, Color.white));
	}

	// tests the makeVertices method
	void testMakeVertices(Tester t) {
		this.initTestConditions();
		t.checkExpect(this.maze1.makeVertices(),
				new ArrayList<ArrayList<Vertex>>(
						Arrays.asList(new ArrayList<Vertex>(Arrays.asList(new Vertex(0, 0), new Vertex(0, 1))),
								new ArrayList<Vertex>(Arrays.asList(new Vertex(1, 0), new Vertex(1, 1))))));
		t.checkExpect(this.maze2.makeVertices(), new ArrayList<ArrayList<Vertex>>(Arrays.asList(
				new ArrayList<Vertex>(
						Arrays.asList(new Vertex(0, 0), new Vertex(0, 1), new Vertex(0, 2), new Vertex(0, 3))),
				new ArrayList<Vertex>(
						Arrays.asList(new Vertex(1, 0), new Vertex(1, 1), new Vertex(1, 2), new Vertex(1, 3))),
				new ArrayList<Vertex>(
						Arrays.asList(new Vertex(2, 0), new Vertex(2, 1), new Vertex(2, 2), new Vertex(2, 3))))));
	}

	// tests the changePosition method
	void testChangePosition(Tester t) {
		this.initTestConditions();
		t.checkExpect(this.maze1.currentPosition, new Posn(0, 0));
		this.maze1.changePosition("down");
		t.checkExpect(this.maze1.currentPosition, new Posn(0, 1));
		this.maze1.changePosition("right");
		t.checkExpect(this.maze1.currentPosition, new Posn(1, 1));
	}
}