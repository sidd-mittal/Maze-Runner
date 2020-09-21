/*=================================================================
Program name: Recursion Maze
Author: Sidd Mittal
Date: November 4th, 2019
Programming Language, version number: Java Version 1.8 
=================================================================
Problem Definition : Your problem is, given a starting point in the maze, to find the cheese and then an exit point (if possible).  If the search for the cheese and an exit is successful, you must print the maze showing the path (the shortest path) that has been found.
Input : A text file containing a 8x12 maze with spaces in between each character. The following characters exist: 'M' 'R' 'B' 'X' 'C' '.' Keep in mind - there are no spaces at the end of each line
Output : The original maze, the solution between the MOUSE and the CHEESE, the solution between the CHEESE and the EXIT, the total solution between the MOUSE and the EXIT, and the shortest path in integer value 
Process : Using recursion, we are able to figure out if moving north, east, south, west is actually feasible to begin with. Afterwards, we save the shortest path in integer value between mouse/cheese and cheese/exit. During this process, we create a visual representation of the shortest path as well. 
=================================================================
*/ 
import java.io.*;
//This class Objects creates an instance of the maze objects   
class Objects{

    public char[][] maze3;  //creates class attributes
	public char[][] maze2;  //creates class attributes 
	public char[][] maze;   //creates class attributes

    // Default Constructor method which initializes maze, maze2, maze3  
	public Objects() throws IOException {
		maze=readFile();
		maze2=readFile();
		maze3=readFile();
	} //end of Object (Default Constructor) 
	/**readFile method:
	 * This functional method reads from a file and returns a 2D array <type char>   
	 *
     * List of Local Variables
	 * br - a BufferedReader object used for reading a file <type BufferedReader>
	 * a  - an Integer which keeps track of the value of the .read function <type integer>
	 * i  - an Integer which keeps track of the number of columns in the array <type integer>
	 * j  - an Integer which keeps track of the number of rows in the array <type integer>
	 * maze - a 2D array which holds the original maze read from a text file <type char> 
	 * 
	 * @param 	none
	 * @throws 	IO Exception	
	 * @return 	2D Array <type char>
	 */	

	public static char[][] readFile() throws IOException {
		BufferedReader br = new BufferedReader (new FileReader("C:\\Users\\sidd1\\Documents\\ICS4U1\\Assignment1\\maze.txt"));
		int a; 
		int i = 0, j = 0;
		char [][] maze = new char [8][12];
		while ((a = br.read()) != -1) {
			char c = (char) a;
			if(i == 12) {
				j = j + 1; 
				i = 0;
			}
			if(c == 'B' || c == 'M' || c == 'R' || c == '.' || c == 'X' || c == 'C') {
				maze[j][i] = c; 
				i = i + 1;							
			}
		}
		br.close(); 
		return maze;
	} //end readFile method 
} //end class objects

public class Mouse {

	static char[][] mazeS1 = new char [8][12]; //static global variable which holds the solution of the maze from the mouse to the cheese
	static char[][] mazeS2 = new char [8][12]; //static global variable which holds the solution of the maze from the cheese to the exit
	static int x; //static global variable which holds the vertical distance of a placeholder value
	static int y; //static global variable which holds the horizontal distance of a placeholder value
	
	/**main method method:
	 * This procedural method is called automatically and is used to organize the calling of other methods defined in the class   
	 *
     * List of Local Variables
	 * mouseCordX - the vertical distance of the mouse in the array <type int> 
	 * mouseCordY - the horizontal distance of the mouse in the array <type int> 
	 * exitCordX - the vertical distance of the exit in the array <type int> 
	 * exitCordY - the horizontal distance of the exit in the array <type int> 
	 * cheeseCordX - the vertical distance of the cheese in the array <type int> 
	 * cheeseCordY - the horizontal distance of the cheese in the array <type int>  
	 * mdis1 - the minimum distance between the mouse and cheese <type int> 
	 * mdis2 - the minimum distance between the cheese and the exit <type int> 
	 * mazeS - the 8x12 array which holds the combined solution. i.e from the mouse to exit (all combined) <type char>
	 * 
	 * @param 	none
	 * @throws 	IO Exception	
	 * @return 	void 
	 */	
	
	public static void main(String[] args) throws IOException {
		int mouseCordX, mouseCordY, exitCordX, exitCordY, cheeseCordX, cheeseCordY; 
		int mdis1=Integer.MAX_VALUE, mdis2=Integer.MAX_VALUE, dis=0;
		char[][] mazeS = new char [8][12]; //create an object of class Objects 
		Objects obj = new Objects();
		findCoords(obj.maze, 'R', 'M'); //sends obj.maze to findCoords method (this is using constructors) 
		mouseCordX=x;
		mouseCordY=y;
		findCoords(obj.maze, 'c', 'C');
		cheeseCordX=x;
		cheeseCordY=y;
		findCoords(obj.maze, 'x', 'X');
		exitCordX=x;
		exitCordY=y;
		mdis1=solveMaze(cheeseCordX, cheeseCordY, obj.maze, obj.maze2, mouseCordX, mouseCordY, dis, mdis1, true);
		dis=0;
		mdis2=solveMaze(exitCordX, exitCordY, obj.maze, obj.maze3, cheeseCordX, cheeseCordY, dis, mdis2, false);
		mazeS=complete(mazeS);
		System.out.println("Welcome to Sidd's Maze-Solving Assignment! This program will find the shortest path between the mouse and the cheese, and then the cheese to the mouse");
		System.out.println("It will print out the shortest distance between each point as well as a visual copy of the solved maze. Enjoy!" +'\n');
		System.out.println("'M' is the mouse, 'X' is the exit, 'C' is the cheese, '.' is the available path, and 'B' is the border" + '\n');
		System.out.println("The original maze is: ");
		printMaze(obj.maze); 
		System.out.println("");
		System.out.println("The solution from the MOUSE to the CHEESE is:");
		printMaze(mazeS1); 
		System.out.println("");
		System.out.println("The solution from the CHEESE to the EXIT is:");
		printMaze(mazeS2); 
		System.out.println("");
		System.out.println("The complete solution of the maze is: ");
		printMaze(mazeS);
		System.out.println();
		if (mdis1+mdis2 > 0) {
			System.out.println("The shortest distance from the mouse to the cheese to the exit is "+(mdis1+mdis2)+" steps.");
		}
		else 
			System.out.println("There is no solution. Please enter valid maze. There should be a valid route from the maze to the cheese to the exit.");
		System.out.println("Thank you for using my program, Mr.Cho!");
	} //end main method
	
	/**findCoords method:
	 * This procedural method finds the coordinates for the mouse, cheese, and exit and updates the global static variables 
	 *
     * List of Local Variables
     * i - keeps track of the number of rows in the array <type int> 
	 * j - keeps track of the number of columns in the array <type int> 
	 * 
	 * @param 	maze - receives the original maze 8x12 array 
	 * 			g1 - receives characters to findCoords method. 'R' 'M' 'C' or 'X' 
	 * 			g2 - receives characters to findCoords method. 'R' 'M' 'C' or 'X' 	
	 * @return 	void 
	 */
	
	public static void findCoords(char[][]maze, char g1, char g2) {
		for (int i=0; i<8; i++) {
			for (int j=0; j<12; j++) {
				if (maze[i][j]==g1 || maze[i][j]==g2) {
					x=i;
					y=j;
					return;
				}
			}
		}
		System.out.println("There is no solution");
		System.out.println("Please enter a valid maze" + '\n');
	}//end findCoords method 
	
	/**solveMaze method:
	 * This functional method returns the minimum distance between the mouse/cheese and the cheese/exit   
	 *
     * List of Local Variables: N/A
	 * 
	 * @param 	x - receives the vertical distance of the cheese in the maze <type int>
	 * 			y - receives the horizontal distance of the cheese in the maze <type int>
	 * 			maze - receives the original maze 8x12 array
	 * 			visited - receives the visited maze containing "*" or "." character 8x12 array
	 * 			mouseCordX - receives the vertical distance of the mouse in the maze <type int>
	 * 			mouseCordY - receives the horizontal distance of the mouse in the maze <type int>
	 * 			mdis - receives the minimum distance between either the mouse and the cheese or the cheese and the exit <type int> 
	 * 			trueFalse - receieves either true or false depending on which minimum distance has to be found. If true, it will calculate mouse to cheese, if false, it will calculate cheese to exit <type boolean>
	 * 
	 * @return 	mdis <type int> 
	 * 			dis <type int> 
	 */

	public static int solveMaze(int x, int y, char[][]maze, char[][]visited, int mouseCordX, int mouseCordY, int dis, int mdis, boolean trueFalse) { 
		if (mouseCordX==x && mouseCordY==y){
			if (dis<=mdis) {
				copyArray(visited, trueFalse);
				return dis;
			}
			return mdis;
		}
		if (visited[mouseCordX][mouseCordY]!='R' && visited[mouseCordX][mouseCordY]!='C' && visited[mouseCordX][mouseCordY]!='M' && visited[mouseCordX][mouseCordY]!='X') {
			visited[mouseCordX][mouseCordY]=('+');
		}
		//travel left
		if (checkPath(mouseCordX-1, mouseCordY, maze, visited)) {
			mdis=solveMaze(x, y, maze, visited, mouseCordX-1, mouseCordY, dis+1, mdis, trueFalse);
		}
		//travel down
		if (checkPath(mouseCordX, mouseCordY-1, maze, visited)) {
			mdis=solveMaze(x, y, maze, visited, mouseCordX, mouseCordY-1, dis+1, mdis, trueFalse);
		}
		//travel right 
		if (checkPath(mouseCordX+1, mouseCordY, maze, visited)) {
			mdis=solveMaze(x, y, maze, visited, mouseCordX+1, mouseCordY, dis+1, mdis, trueFalse);
		}
		//travel up 
		if (checkPath(mouseCordX, mouseCordY+1, maze, visited)) {
			mdis=solveMaze(x, y, maze, visited, mouseCordX, mouseCordY+1, dis+1, mdis, trueFalse);
		}
		if (visited[mouseCordX][mouseCordY]!='R' && visited[mouseCordX][mouseCordY]!='C' && visited[mouseCordX][mouseCordY]!='M' && visited[mouseCordX][mouseCordY]!='X') {
			visited[mouseCordX][mouseCordY]=('.');
		}
		return mdis;
	} // end solveMaze method
	
	/**checkPath method:
	 * This functional method returns either true or false depending on whether or not a path option is feasible   
	 *
     * List of Local Variables: N/A
	 * 
	 * @param 	mouseCordX - receives the vertical distance of the mouse in the array <type int>
	 * 			mouseCordY - receives the horizontal distance of the mouse in the array <type int>
	 * 			maze - receives the original maze read from the file 8x12 array <type char> 
	 * 			visited - recieves the visited maze 8x12 array <type char> 
	 * 
	 * @return 	true or false <type boolean>
	 */

	public static boolean checkPath(int mouseCordX, int mouseCordY, char [][]maze, char [][]visited) {
		if (mouseCordX<0 || mouseCordX>7 || mouseCordY<0 || mouseCordY>11) {
			return false;
		}
		if (maze[mouseCordX][mouseCordY]!='B') {
			if (visited[mouseCordX][mouseCordY]!='+') {
				return true;
			}
		}
		return false;
	} //end checkPath method
	
	/**complete method:
	 * This functional method returns the complete solution of both mazes. Essentially, it will combine both solutions (from the mouse to cheese and the cheese to the exit) 
	 *
     * List of Local Variables: 
     * i - keeps track of the number of rows in in the original maze 8x12 array <type int> 
     * j - keeps track of the number of columns in the original maze 8x12 array <type int> 
	 * 
	 * @param 	maze - receives the original maze 8x12 array, as well as the solution mazes (mazeS`1 and mazeS2) <type char>  
	 * 
	 * @return 	maze <type char> 
	 */
	public static char[][] complete(char maze[][]) {
		for (int i=0; i<8; i++) {
			for (int j=0; j<12; j++) {
				if (mazeS1[i][j]=='+') {
					maze[i][j]='+';
				}
				else {
					maze[i][j]=mazeS2[i][j];
				}
			}
		}
		return maze;
	} //end complete method

	/**printMaze method:
	 * This procedural method will print the maze to the console  
	 *
     * List of Local Variables: 
     * i - keeps track of the number of rows in the array <type int> 
     * j - keeps track of the number of columns in the array <type int> 
	 * 
	 * @param 	maze - receives the original maze 8x12 array, as well as the 2 solution mazes to print out to the console.  
	 * 
	 * @return 	void  
	 */
	
	public static void printMaze(char [][] maze) {
		for (int i=0; i<8; i++) {
			for (int j=0; j<12; j++) {
				System.out.print(maze[i][j]+ " ");
			}
			System.out.println();
		}	
	} //end printMaze method
	
	/**copyArray method:
	 * This procedural method will copy the corresponding array to the corresponding solution. i.e. It will ensure that the solution between the mouse and cheese is properly printed. 
	 *
     * List of Local Variables: 
     * i - keeps track of the number of rows in the array <type int> 
     * j - keeps track of the number of columns in the array <type int> 
	 * 
	 * @param 	maze - receives the visited maze <type char> 
	 * 
	 * @return 	void  
	 */
	
	public static void copyArray(char[][] maze, boolean trueFalse) {
		for (int i=0; i<8; i++) {
			for (int j=0; j<12; j++) {
				if (trueFalse) {
					mazeS1[i][j]=maze[i][j];
				}
				else {
					mazeS2[i][j]=maze[i][j];
				}		
			}
		}
	} //end copyArray method
} //end public class Mouse




