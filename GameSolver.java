import java.util.ArrayList;
import java.io.*;

public class GameSolver
{
    private static String [] players;   //list of players from 1 to N
    private static int [] actions;      //list of number of actions available to each player
    private static float [] payoff;     //list of values of payoffs
    private static int NoOfPlayers;     //The total number of players
    private static StrategyProfile [] possibleProfiles; //all possible strategy profiles
    private static int NoOfStrategyProfiles;
    private static int [] temp;
    
    private static ArrayList [] strongDomStrat;
    private static ArrayList [] weakDomStrat;
    private static ArrayList [] veryWeakDomStrat;
    
    private static ArrayList NashEquilibria;    //stores the indices
    
    public static int findIndexOfProfile(int [] temp)
    {
        int i;
        for(i=0;i<NoOfStrategyProfiles;i++)
        {
            if(possibleProfiles[i].compareProfile(temp)==true)
            {
                return i;
            }
        }
        
        return -1;
        
    }
    
    public static float getPayoffOfProfile(int [] temp, int i) //to get Ui for given strategy profile
    {
        int index = findIndexOfProfile(temp);
        if(index==-1)
        {
            System.err.println("Error!");
        }
        
        return possibleProfiles[index].getPayoff(i);
        
    }
    
    public static void function(int j)
    {
        if(j==NoOfPlayers-1)
        {
            func(0,NoOfPlayers-1);
        }
        else if(j==0)
        {
            func(1,NoOfPlayers);
        }
        else
        {
            funct(0,j);
        }
    }
    
    public static void findNashEquilibria()
    {
        NashEquilibria = new ArrayList();
        int i,j,k;
        int alpha;
        
        float Ui,val;
        ArrayList list;
        
        main_for:        for(j=0;j<NoOfStrategyProfiles;j++)
        {
            
            list = possibleProfiles[j].getProfile();
            for(k=0;k<NoOfPlayers;k++)
            {
                temp[k] = (int)list.get(k);
            }
            
            for(i=0;i<NoOfPlayers;i++)
            {
                Ui = possibleProfiles[j].getPayoff(i);
                
                alpha = temp[i];
                
                for(temp[i]=temp[i]-1;temp[i]>=1;temp[i]--)
                {
                    val = getPayoffOfProfile(temp,i);
                    if(Ui < val)
                    {
                        continue main_for;
                    }
                    
                }
                
                temp[i] = alpha;
                
                for(temp[i]=temp[i]+1;temp[i]<=actions[i];temp[i]++)
                {
                    val = getPayoffOfProfile(temp,i);
                    if(Ui < val)
                    {
                        continue main_for;
                    }
                }
                
                temp[i] = alpha;
                
            }
            
            NashEquilibria.add(j);
            
        }
        
    }
    
    public static void findDominantStrategies(int i)  	//finds all dominant strategies for player 'i'
    {
        int strategy,j,k,check;
	int check1,check2,check3;
        float val,tempVal;
        int checkLess;

	int strong;


main_for:       for(strategy=1;strategy<=actions[i];strategy++)
        {

	    strong = 1;

            for(j=0;j<NoOfPlayers;j++)  //initialize temp[] to 1111...11
            {
                temp[j] = 1;
            }

		check3 = 0;
            
            //outer
          while(true)
          {
                    temp[i] = strategy;
                    checkLess = 0;

			check1 = 0;
			check2 = 0;

            val = getPayoffOfProfile(temp,i);
            for(j=strategy-1;j>=1;j--)
            {
                temp[i] = j;
                tempVal = getPayoffOfProfile(temp,i);
		
                if(val<tempVal)
                {   
                    checkLess = 1;
                    break;
                }

		if(val==tempVal)
		{
			strong = 0;
		}

		if(val>tempVal)
		{
			check1++;
		}


            }
            
            if(checkLess==1)
            {
                continue main_for;
            }
            
            temp[i] = strategy;
            
            for(j=strategy+1;j<=actions[i];j++)
            {
                temp[i] = j;
                tempVal = getPayoffOfProfile(temp,i);
                if(val<tempVal)
                {
                    checkLess = 1;
                    break;
                }

		if(val==tempVal)
		{
			strong = 0;
		}

		if(val>tempVal)
		{
			check2++;
		}

            }
            
            if(checkLess==1)
            {
                continue main_for;
            }

            if((check1==strategy-1) && (check2==actions[i]-strategy))
            {
                check3 = 1;
            }
            
            check = 1;
            for(k=0;k<NoOfPlayers;k++)
            {
                if(k==i)
                {
                    continue;
                }
                if(temp[k]!=actions[k])
                {
                    check = -1;
                    break;
                }
            }
            
            if(check==1)
            {
                /*add strategy to an ArrayList*/ 	//list.add(strategy);

		if(strong==1)
		{
			/*add to strongDomStrat*/	strongDomStrat[i].add(strategy);
		}

		if(check3==1)
		{
			/*add to weakDomStrat*/		weakDomStrat[i].add(strategy);
		}

		/*add to veryWeakDomStrat*/		veryWeakDomStrat[i].add(strategy);




                /*continue with outermost loop*/ continue main_for;

            }
            else
            {
                /*change temp[]*/    function(i);
                continue;
            }
          } 
       }
       
    }
    
    public static void funct(int i, int j)  //call with i=0. (For the j-mid case)
    {
        if(i<NoOfPlayers)
        {
            if(temp[i]+1>actions[i])
            {
                temp[i] = 1;
                if((i+1)<j)
                {
                    funct(i+1,j);
                }
                if((i+1)==j)
                {
                    funct(i+2,j);
                }
            }
            else
            {
                temp[i] = temp[i] + 1;
            }
        }
    }
    
    public static void func(int i, int N)      //during call, pass i = 0
    {
        if(i<N)   //added to remove ArrayIndexOutOfBoundsException
        {
            if(temp[i]+1>actions[i])        //'func' is meant to produce all possible
            {                               //strategy profiles in 'temp'
                temp[i] = 1;
                func(i+1,N);
            }
            else
            {
                temp[i] = temp[i] + 1;
            }
        }
    }
    
    public static void main(String[] args) throws IOException
    {
        int i,index;
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        
        reader.readLine();  //First line contains details in Gambit format
        String alpha = reader.readLine();   //alpha contains the second line
        reader.readLine();    //Third line is empty
        String beta = reader.readLine();    //beta contains the fourth line
        String [] splitBeta = beta.split(" ");
        
        payoff = new float[splitBeta.length];
        for(i=0;i<payoff.length;i++)
        {
            payoff[i] = Float.parseFloat(splitBeta[i]);
        }
        //At this stage, the array 'payoff' contains the values of payoffs
        
        int startIndex = alpha.indexOf('{');
        int endIndex = alpha.indexOf('}');
        String playerList = alpha.substring(startIndex+2,endIndex-1);
        
        int lastStartIndex = alpha.lastIndexOf('{');
        int lastEndIndex = alpha.lastIndexOf('}');
        String actionList = alpha.substring(lastStartIndex+2,lastEndIndex-1);
        
        String [] action = actionList.split(" ");
        String test = playerList.replaceAll("\" \"", ":");
        String test2 = test.substring(1,test.length()-1);
        players = test2.split(":");
        
        //At this stage, 'players' array contains the names of players from 1 to N
        
        actions = new int[action.length];
        
        NoOfStrategyProfiles = 1;
        
        for(i=0;i<action.length;i++)
        {
            actions[i] = Integer.parseInt(action[i]);
            NoOfStrategyProfiles = NoOfStrategyProfiles*actions[i];
        }

        //At this stage, 'actions' array contains the list of number of actions for players 1 to N
        
        NoOfPlayers = players.length;
        
        possibleProfiles = new StrategyProfile[NoOfStrategyProfiles];
        for(i=0;i<NoOfStrategyProfiles;i++)
        {
            possibleProfiles[i] = new StrategyProfile(NoOfPlayers);
        }
        
        //Now to store values in 'possibleProfiles'
        
        temp = new int[NoOfPlayers];
        for(i=0;i<NoOfPlayers;i++)  //initializing 'temp' with 111...1
        {
            temp[i] = 1;
        }
        
        int k=0,j;
        for(i=0;i<NoOfStrategyProfiles;i++)
        {
            for(j=0;j<NoOfPlayers;j++)
            {
                possibleProfiles[i].addStrategy(temp[j]);
                possibleProfiles[i].addPayoff(payoff[k]);
                k++;
            }
            func(0,NoOfPlayers);
        }
        
        //At this stage, all possible strategy profiles and payoffs are stored
        //in user-defined class instances
       
        for(i=0;i<NoOfPlayers;i++)
        {
            temp[i] = 1;
        }
        
        
        strongDomStrat = new ArrayList[NoOfPlayers];
        weakDomStrat = new ArrayList[NoOfPlayers];
        veryWeakDomStrat = new ArrayList[NoOfPlayers];
        
        for(i=0;i<NoOfPlayers;i++)
        {
            strongDomStrat[i] = new ArrayList();
            weakDomStrat[i] = new ArrayList();
            veryWeakDomStrat[i] = new ArrayList();
            
            findDominantStrategies(i);
        }
        
        /*NOTE: 0 or 1 strongly dominant strategy for a given player
         *      0 or 1 weakly dominant strategy for a given player
         *      any number of very weakly dominant strategies for a given player
         */
        
        for(i=0;i<NoOfPlayers;i++)
        {
            if(strongDomStrat[i].isEmpty())
            {
                System.out.println("No Strongly Dominant Strategy For " + players[i]);
            }
            else
            {
                System.out.println("Strongly Dominant Strategy For " + players[i] + " Is: " + strongDomStrat[i].get(0));
            }
        }
        
        for(i=0;i<NoOfPlayers;i++)
        {
            if(weakDomStrat[i].isEmpty())
            {
                System.out.println("No Weakly Dominant Strategy For " + players[i]);
            }
            else
            {
                System.out.println("Weakly Dominant Strategy For " + players[i] + " Is: " + weakDomStrat[i].get(0));
            }
        }
        
        for(i=0;i<NoOfPlayers;i++)
        {
            if(veryWeakDomStrat[i].isEmpty())
            {
                System.out.println("No Very Weakly Dominant Strategy For " + players[i]);
            }
            else
            {
                System.out.println("Very Weakly Dominant Strategies For " + players[i] + " Are As Follows:");
                for(j=0;j<veryWeakDomStrat[i].size();j++)
                {
                    System.out.print(veryWeakDomStrat[i].get(j) + " ");
                }
                System.out.println();
            }
        }
        
        //Nash Equilibria
        
        findNashEquilibria();
        
        if(NashEquilibria.isEmpty())
        {
            System.out.println("No Nash Equilibria Exist!");
        }
        else
        {
            System.out.println("Nash Equilibria For The Input Game Are As Follows:");
            for(i=0;i<NashEquilibria.size();i++)
            {
                index = (int)NashEquilibria.get(i);
                System.out.println(possibleProfiles[index].getProfile());
            }   
        }
        
    }
}

class StrategyProfile       //The strategy profile and the corresponding payoffs
{
    private static int size;
    private ArrayList payoffs;
    private ArrayList profile;
    
    StrategyProfile(int n)  //constructor
    {
        size = n;
        payoffs = new ArrayList(size);
        profile = new ArrayList(size);
    }
    
    ArrayList getProfile()
    {
        return this.profile;
    }
    
    boolean compareProfile(int [] temp) //compares strategy profile to temp[] values
    {
        int i;
        for(i=0;i<size;i++)
        {
            if(profile.get(i)!=temp[i])
            {
                return false;
            }
        }
        return true;        //returns true if equal, false otherwise
    }
    
    ArrayList getPayoffs()
    {
        return this.payoffs;
    }
    
    //following functions used to store and get individual values
    void addPayoff(float payoff)
    {
        payoffs.add(payoff);
    }
    
    float getPayoff(int index)
    {
        return (float)payoffs.get(index);
    }
    
    void addStrategy(int s)
    {
        profile.add(s);
    }
    
    int getStrategy(int index)
    {
        return (int)profile.get(index);
    }
}


