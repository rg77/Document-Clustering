import java.io.*;
import java.util.*;
 
class DocumentVector
{
    public static void main(String args[]) throws IOException
    {
        final int TOTAL_NO_DOCUMENT = 3; 
        String s,token;
        StringTokenizer   st;
        TreeMap<String,Double> hm = new TreeMap<String,Double>();
        Double sum =0.0;
        
        for(int i=1;i<=TOTAL_NO_DOCUMENT;i++)//reading from 3 files
        {
            FileReader fr =new FileReader("hello" + i + ".txt");//reading from 3 files
            BufferedReader br = new BufferedReader(fr);
        
        
            //dumping the words in the dictionary
            while((s=br.readLine()) != null)
            {   st = new StringTokenizer(s,";, \n\t");
                while(st.hasMoreTokens())
                {
                                
                    token = st.nextToken();
                    if(hm.get(token) == null)
                    {
                       hm.put(token,1.0);         
                    }
                    else
                    {
                        Double value = hm.get(token);
                        hm.put(token,value +1);  
                    }
                    
             //   System.out.println(token);
                
                }
            //System.out.println(s);
            }    
        
        
        fr.close();
        }
        
        Set<Map.Entry<String,Double>> set  = hm.entrySet();
        
        for(Map.Entry<String,Double> me : set)//dispalying key-value pairs 
        {
        
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue() );
        }
        
        for(Map.Entry<String,Double> me : set)//converting to idf values 
        {
            Double value = (Math.log( TOTAL_NO_DOCUMENT/me.getValue())) / (Math.log(2)) ;//to calculate to the base 2        
          //  System.out.print(me.getKey() + ":");
            me.setValue(value);
        }
        
        //creating array of values
        Double[] mapValues = new Double[hm.size()];
        int pos=0;
        
        for(Map.Entry<String,Double> me : set)
        {
             mapValues[pos] =  me.getValue() ;  
             pos++;          
                       //storing the set value in an array      
        }
        
        
        for(Map.Entry<String,Double> me : set)//dispalying key-value pairs 
        {
      
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue() );
        }
    
    
       //creating string of keys
       String[] mapKeys = new String[hm.size()];
       pos =0;
       for( String key : hm.keySet() )
       {
        mapKeys[pos++]=key;
       }
       
       //creating vector for each document
       
       Vector< Vector<Double> > docuVector = new Vector<Vector<Double>>(TOTAL_NO_DOCUMENT);//vector of (vectors of double for each          document )
       
       Vector< Double > documentMagnitudeVector = new Vector< Double >(TOTAL_NO_DOCUMENT);//storing magnitude of each document vector
       
       Double queryMagnitude=0.0;//storing magnitude of query vector
       
       //Where Magic Happens !!
       for(int i=1;i<=TOTAL_NO_DOCUMENT;i++)
       {   
            Vector<Double> v = new Vector<Double>(hm.size());
            
            for(int j=0 ; j<hm.size() ; j++)//initialising each element to 0
            {
                    v.addElement(0.0);
            }
            
            FileReader fr =new FileReader("hello" + i + ".txt");//reading from 3 files
            BufferedReader br = new BufferedReader(fr);
        
            //checking frequency of words in dictionary and creating vector for each document
            while((s=br.readLine()) != null)
            {   st = new StringTokenizer(s,";, \n\t");
                while(st.hasMoreTokens())
                {
                    token = st.nextToken();
                    int index = Arrays.binarySearch(mapKeys,token);
                    double vectorValue = v.elementAt(index);
                    v.setElementAt(vectorValue + 1,index);
                    
                }        
            }
            
            //after this, storing tdf*idf values in the vector
           
            pos=0;
            sum=0.0;
            for(Double val : v)
            {
                //System.out.println("val: " + val + "pos: " + pos + "mapvalues: " + mapValues[pos]); 
                Double newVal = val* mapValues[pos];
                v.setElementAt(newVal,pos);
                sum += newVal*newVal;//calculating the magnitude for each vector
                ++pos;
            }

            //adding this vector to vector containing document vectors
            docuVector.add(v);
                                    
            //now storing the magnitude of this  docuVector
            documentMagnitudeVector.add(Math.sqrt(sum));    
            
         }   
           
         
         //input query
         
         // 1. Create a Scanner using the InputStream available.
        Scanner scanner = new Scanner( System.in );

        // 2. Don't forget to prompt the user
        System.out.print( "Input the query : " );

        // 3. Use the Scanner to read a line of text from the user.
        String query = scanner.nextLine();
         
        Vector<Double> queryVector = new Vector<Double>(hm.size());
        
        st = new StringTokenizer(query,";, \n\t");
        for(int j=0 ; j<hm.size() ; j++)//initialising each element to 0 in the queryVector
        {
                    queryVector.addElement(0.0);
        }
        
        
        Double maxFrequency = -1.0;//element to store max frequency of query vector
        while(st.hasMoreTokens()) 
        {
                token = st.nextToken();
                int index = Arrays.binarySearch(mapKeys,token);
                double vectorValue = queryVector.elementAt(index);
                queryVector.setElementAt(vectorValue + 1,index);
                if(queryVector.get(index) > maxFrequency) //storing max frequency of query 
                    maxFrequency  =     queryVector.get(index);        
        
        }         
        
        //after this, storing tdf*idf/maxFrequency values in the queryVector
           
            pos=0;
            sum = 0.0;
            for(Double val : queryVector)
            {
                //System.out.println("val: " + val + "pos: " + pos + "mapvalues: " + mapValues[pos]); 
                Double newVal = (val * mapValues[pos] ) / maxFrequency;
                queryVector.setElementAt(newVal,pos);
                sum += newVal*newVal;//calculating the magnitude for query vector 
                ++pos;
            }
    
            queryMagnitude = Math.sqrt(sum);
           
         
         
         
         //displaying the vector
         System.out.println("\n printing document vectors");
         
         for(Vector<Double> v:docuVector)
         {
            for(Double val : v)
            {
                System.out.print(" " + val);
            }
            
            System.out.println("\n");
         }
        
        //displaying the query vector
        System.out.println("\nPrinting the query vector:\n");
         for(Double val : queryVector)
            {
                System.out.print(" " + val);
            }         
        
        //  
        
        //display magnitude of query
        System.out.println("\nmagnitude of query vector: " + queryMagnitude);
        
        //displaying the magnitude vector
         System.out.println("\nPrinting the document magnitude vector:\n");
        System.out.println("no of ele in docu magni: " + documentMagnitudeVector.size());
        for(Double val : documentMagnitudeVector)
            {
                System.out.print(" " + val);
            }       
            System.out.println();
            
            /*
            	Calculating Cosine Similarity Measure !
	            We have docuVector[] && queryVector and
	            We have documentMagnitudeVector && queryMagnitude
        	    We'll do :
        	    (docuVector[i]*queryVector)/(documentMagnitudeVector[i]*queryMagnitude)
            */
            
           TreeMap<Double,Integer> cosSim = new TreeMap<Double,Integer>(Collections.reverseOrder());
 
   	   for(int i=0;i<docuVector.size();i++){
  	   	Vector<Double> currDocVec=docuVector.get(i);
  	   	Double cosSimCurr=0.0;
  	   	for(int j=0;j<currDocVec.size();j++){
  	   		cosSimCurr+=(currDocVec.get(j)*queryVector.get(j));
  	   	}
  	   	cosSimCurr/=(documentMagnitudeVector.get(i)*queryMagnitude);
  	   	//System.out.println(cosSimCurr);
//  	   	cosSim.add(cosSimCurr);
		cosSim.put(cosSimCurr,i+1);
  	   }
  	   System.out.println("Cosine Similarity Measure : ");
  	
          Set<Map.Entry<Double,Integer>> set1  = cosSim.entrySet();
  	    for(Map.Entry<Double,Integer> me : set1)
        	{
        
        	    System.out.println(me.getKey());
        	
        	}
	    System.out.println("Documents to view in Order : ");  	   
	    for(Map.Entry<Double,Integer> me:set1)
	    	System.out.println(me.getValue());
        }      
 }
