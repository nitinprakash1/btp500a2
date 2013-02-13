import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.*;
class Soup
{
	private LinkedList<Compound> soup = new LinkedList<Compound>(); // all combinations
	private TreeMap<Integer,String> History = new TreeMap<Integer,String>(); // history of unique compounds
	private char[] elements = {'H','N','O','C'};  // basic elements.
	
	
	/*
	 * A soup object is created. The constructor pushes 4 basic Compound objects into the Linked List.
	 * The user is asked to enter a Goal.
	 * The goal is passed as an argument to the Evolution method.
	 * */	
	public static void main(String []args)
	{
		Soup S = new Soup();
		String goal;		
		Scanner in = new Scanner(System.in);
				
		System.out.println("Enter the goal compound in the format CxHxNxOx : ");
		goal = in.nextLine();
		in.close();		
		S.Evolution(goal);	
		System.out.println("Below is the path taken. From History");
		S.printHistory();
		
	}
	/* 
	 * Constructor pushes 4 compound obects into the linked list
	 * */
	Soup()
	{
		soup.push( new Compound("H",1) );
		soup.push( new Compound("O",-2) );
		soup.push( new Compound("N",-3) );
		soup.push( new Compound("C",-4) );
	}
	
	/* 
	 * The while runs the evolution as long as the soup is not empty, the goal is not formed or 
	 * the number of iterations is less than 3 million.
	 * Boolean variable flag tells if the goal is reached on not.
	 * First compound from the linked list is popped and used for evolution.
	 * The checks performed on the compound are if the compound is the goal, if not
	 * then the compound is checked for stability. If stable then the compound is removed and pushed into 
	 * History tree map for future reference. if the compound was not stable then 
	 * the Combine method forms 4 new combinations in accordance with the rules.
	 * Every compound removed from the soup is checked against the content of the tree map 
	 * to see if it has already been processed.*/
	public boolean Evolution(String goal)
	{
		boolean flag = false;
		int i = 0;
		
		while( !soup.isEmpty() && !flag && i < 10000000)
		{
			Compound compound = soup.pop();
		
			if( !History.containsValue(formatCompound(compound.CompoundValue())) )
			{
				if(goal.equals(formatCompound(compound.CompoundValue())))
				{
					flag = true;System.out.println("Goal has been formed. Evolution complete!"); // success
				}
				else if( Heavier(compound,goal) || compound.BondValue() == 0 )
				{
					History.put(i,formatCompound(compound.CompoundValue())); // push into history
				}
				else if( !History.containsValue(formatCompound(compound.CompoundValue())) )
				{
					combine(compound);  // further evolution.
				}
			}
			i++;
			
			if(soup.isEmpty())
			{
				System.out.println("Goal not formed. The Evolution has stopped because Soup is empty");
			}
			else if(i>=10000000)
			{
				System.out.println("Goal not formed. The Evolution is taking too long. Too many iterations");
			}
			if(flag)
			{
				History.put(i,formatCompound(compound.CompoundValue())); // pushing goal into history
			}
			
		}
		
		
		return flag;
	}
	
	/* 
	 * The compound object is converted to a Char and is passed to this method.
	 * The 4 rules of evolution are applied based on which element is being added to the 
	 * compound.
	 * The rules are described in if-else statements.
	 * The new bond value is returned.
	 * */
	public int bondValue(char compound,Compound current_compound)
	{
		int bondValue = current_compound.BondValue(); // return bond value
		
		if( compound == 'H')
		{
			bondValue++;
		}
		else if( compound == 'O' &&  bondValue >= 0)
		{
			bondValue -= 2;
		}
		else if( compound == 'N' )
		{
			if(bondValue < 0)
			{
				bondValue -= 1;
			}
			else
			{
				bondValue -= 3;
			}
		}
		else if( compound == 'C' )
		{
			if((Counter(current_compound)%3) == 0 && bondValue >= 0)
			{
				bondValue -= 2;
			}
			else if(bondValue >= 0)
			{
				bondValue -= 4;
			}
			else
			{
				bondValue -= 2;
			}
		}
		
		if(bondValue < -4)
		{
			bondValue +=3;
			current_compound.setCompoundValue( (current_compound.CompoundValue() + "HHH") );
		}

		return bondValue;
	}
	
	/* 
	 * Counter is used to count the number of C atoms to determine the correct change 
	 * that has to be made to the bond value. The change to the bond value depends on whether the
	 * total number of C atoms is divisible by 3 or not.
	 * Current_compound is converted to a char array which makes it easier to analyse the 
	 * compound element by element and count every occurance of C atom*/
	public int Counter(Compound current_compound)
	{
		int count = 0;
		char[] compoundarray = current_compound.CompoundValue().toCharArray();
		
		for(int i = 0 ; i < current_compound.CompoundValue().length() ; i++)
		{
			if(compoundarray[i] == 'C')
			{
				count++;
			}
		}
		
		return count;
	}
	/* 
	 * This method compares the atom count of the current compound to the 
	 * atom count of the goal to determine if the compound will be formed 
	 * in that path or not. If the atom count of the current compound is more than the
	 * atom count of the goal then the goal wont be formed.
	 * The compound and the goal are both converted to char arrays.
	 * Then each element in each of the array is scanned and the atom count is figured out for
	 * every element individually for the compound and the goal 
	 * and compared with each other.
	 * the method returns true if the goal cannot be formed in that path.*/
	public boolean Heavier(Compound compound, String goal)
	{
		
		char[] compound_array = compound.CompoundValue().toCharArray();
		char[] goal_array = goal.toCharArray();
		
		boolean flag = false;
		
		int compoundH = 0,compoundN = 0,compoundO = 0,compoundC = 0;
		int goalH = 0,goalN = 0,goalO = 0,goalC = 0;
		
		for( int i = 0; i < compound.CompoundValue().length() ; i++)
		{
			if(compound_array[i] == 'H')
			{
				compoundH++;
			}
			else if(compound_array[i] == 'O')
			{
				compoundO++;
			}
			else if(compound_array[i] == 'N')
			{
				compoundN++;
			}
			else
			{
				compoundC++;
			}
		} 
		for( int i = 0; i < goal.length() ; i++)
		{
			if(goal_array[i] == 'C')
			{
				if( ( Character.getNumericValue(goal_array[i+1]) >= 0 ||  Character.getNumericValue(goal_array[i+1]) <= 9))
				{
					goalC = Character.getNumericValue(goal_array[i+1]); // converting char to integer
				}
				else
				{
					goalC = 1;
				}
			}
			else if(goal_array[i] == 'H')
			{
				if(( Character.getNumericValue(goal_array[i+1]) >= 0 ||  Character.getNumericValue(goal_array[i+1]) <= 9))
				{
					goalH = Character.getNumericValue(goal_array[i+1]);
				}
				else
				{
					goalH = 1;
				}
				
			}
			else if(goal_array[i] == 'N')
			{
				if(( Character.getNumericValue(goal_array[i+1]) >= 0 ||  Character.getNumericValue(goal_array[i+1]) <= 9))
				{
					goalN = Character.getNumericValue(goal_array[i+1]);
				}
				else
				{
					goalN = 1;
				}
			}
			else if(goal_array[i] == 'O')
			{
				if( (goal.length() - 1) < i)
				{
					if(( Character.getNumericValue(goal_array[i+1]) >= 0 ||  Character.getNumericValue(goal_array[i+1]) <= 9))
					{
						goalO = Character.getNumericValue(goal_array[i+1]);
					}
				}
				else
				{
					goalO = 1;
				}
			}
			
		}

		if( compoundH > goalH || compoundO > goalO || compoundN > goalN || compoundC > goalC )
		{
			flag = true; // goal cannot be formed in this path.
		}	
		
		return flag;
	}
	
	/*
	 * This method just reformats the compound to a more easily followable format.
	 * Converts CCCHHOONN to C3H2O2N2
	 * This makes it easier to compare the current compound to the contents of History
	 * to determine if the compound was already processed. This prevents duplication and the order 
	 * of the atoms in the compound doesnt matter.
	 * The unformatted compound is converted into a char array called unformatted_array.
	 * The each atom in the array is scanned and the count corresponding to that atom is 
	 * incremented when the atom is encountered in the array.
	 * The formatted string is then formed by concatenating the atom and then the atom count to form 
	 * one string which represents the formatted compound structure.
	 * The formatted compound is then returned.
	 */
	public String formatCompound(String unformatted)
	{
		String formatted = "";
		int h = 0,n = 0,o = 0,c = 0;
		char[] unformatted_array = unformatted.toCharArray();
		
		for( int i = 0; i < unformatted.length() ; i++)
		{
			if(unformatted_array[i] == 'H')
			{
				h++;
			}
			else if(unformatted_array[i] == 'O')
			{
				o++;
			}
			else if(unformatted_array[i] == 'N')
			{
				n++;
			}
			else
			{
				c++;
			}
		} 
		
		if(c > 1)
		{
			formatted += "C" + c;
		}
		else if(c == 1)
		{
			formatted += "C";
		}
		
		if(h > 1)
		{
			formatted += "H" + h;
		}
		else if(h == 1)
		{
			formatted += "H";
		}
		
		if(n > 1)
		{
			formatted += "N" + n;
		}
		else if(n == 1)
		{
			formatted += "N";
		}
		
		if(o > 1)
		{
			formatted += "O" + o;
		}
		else if(o == 1)
		{
			formatted += "O";
		}
	
		return formatted;
	}
	
	/* 
	 * The combine method is used to add the four basic elements to the current unstable compound(parent)
	 * to form 4 more new compounds(children). The four elements are stored in the char array called elements.
	 * The elements are added in accordance with the rules of evolution.
	 * */
	public void combine(Compound parent)
	{
		for(int i = 0; i < 4 ; i++)
		{
			soup.push( new Compound(parent.CompoundValue() + elements[i],bondValue(elements[i],parent) ) );
		}
	}
	
	/* 
	 * Printing the elements from the Tree map.
	 * Code referenced from :
	 *   http://stackoverflow.com/questions/5757202/how-would-i-print-all-values-in-a-treemap
	 *   */
	public void printHistory()
	{	
		for (Entry<Integer, String> entry : History.entrySet()) 
		{
		     System.out.println("Key: " + entry.getKey() + ". Compound: " + entry.getValue());
		}
	}
	
}

